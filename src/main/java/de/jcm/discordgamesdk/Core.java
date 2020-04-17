package de.jcm.discordgamesdk;

import cz.adamh.utils.NativeUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * The main component for accessing Discord's game SDK.
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/discord#functions-in-the-sdk">
 *     https://discordapp.com/developers/docs/game-sdk/discord#functions-in-the-sdk</a>
 * @author JCM
 */
public class Core implements AutoCloseable
{
	/**
	 * <p> Loads and initializes the native library.
	 * This method also loads Discord's native library. </p>
	 *
	 * <p>You may call this method more than once which unloads the old shared object and loads the new one.</p>
	 *
	 * @param discordLibrary Location of Discord's native library.
	 *                       <p>On Windows the filename (last component of the path) must be
	 *                       "discord_game_sdk.dll" or an {@link UnsatisfiedLinkError} will occur.</p>
	 *                       <p>On Linux the filename does not matter.</p>
	 *
	 * @throws UnsatisfiedLinkError if Discord's native library can not be loaded
	 */
	public static void init(File discordLibrary)
	{
		try
		{
			System.loadLibrary("discord_game_sdk_jni");
		}
		catch(UnsatisfiedLinkError e)
		{
			try
			{
				if(System.getProperty("os.name").toLowerCase().contains("windows"))
				{
					System.load(discordLibrary.getAbsolutePath());
					NativeUtils.loadLibraryFromJar("/"+"discord_game_sdk_jni"+".dll");
				}
				else
				{
					NativeUtils.loadLibraryFromJar("/"+"lib"+"discord_game_sdk_jni"+".so");
				}
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
		initDiscordNative(discordLibrary.getAbsolutePath());
	}

	private static native void initDiscordNative(String discordPath);

	/**
	 * <p>Default callback to use for operation returning a {@link Result}.</p>
	 *
	 * <p>Checks if the result is {@link Result#OK} and throws a {@link GameSDKException} if it is not.</p>
	 */
	public static final Consumer<Result> DEFAULT_CALLBACK = result ->
	{
		if(result!=Result.OK)
			throw new GameSDKException(result);
	};

	/**
	 * <p>Default log hook. Simply prints the log message
	 * in pattern "<code>[level] message</code>" to {@link System#out}.</p>
	 */
	public static final BiConsumer<LogLevel, String> DEFAULT_LOG_HOOK= (level, message) ->
	{
		System.out.printf("[%s] %s\n", level, message);
	};

	private long pointer;

	private ActivityManager activityManager;
	private UserManager userManager;
	private OverlayManager overlayManager;
	private RelationshipManager relationshipManager;

	/**
	 * Creates an instance of the SDK from {@link CreateParams} and
	 * sets the log hook to {@link Core#DEFAULT_LOG_HOOK}.
	 *
	 * Example:
	 * <pre>{@code
	 *  try(CreateParams params = new CreateParams())
	 *  {
	 *      params.setClientID(<client ID of your application>);
	 *      params.setFlags(CreateParams.getDefaultFlags());
	 *      try(Core core = new Core(params))
	 *      {
	 *          // do something with your Core
	 *      }
	 *  }}</pre>
	 *
	 * @param params Parameters to create Core from.
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/discord#create">
	 *     https://discordapp.com/developers/docs/game-sdk/discord#create</a>
	 */
	public Core(CreateParams params)
	{
		Object ret = create(params.getPointer());
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			pointer = (long) ret;
		}

		setLogHook(LogLevel.DEBUG, DEFAULT_LOG_HOOK);

		this.activityManager = new ActivityManager(getActivityManager(pointer));
		this.userManager = new UserManager(getUserManager(pointer));
		this.overlayManager = new OverlayManager(getOverlayManager(pointer));
		this.relationshipManager = new RelationshipManager(getRelationshipManager(pointer));
	}

	private native Object create(long paramPointer);
	private native void destroy(long pointer);

	private native long getActivityManager(long pointer);
	private native long getUserManager(long pointer);
	private native long getOverlayManager(long pointer);
	private native long getRelationshipManager(long pointer);

	private native void runCallbacks(long pointer);

	private native void setLogHook(long pointer, int minLevel, BiConsumer<LogLevel, String> logHook);

	/**
	 * <p>Returns the {@link ActivityManager} associated with this core.</p>
	 * <p>An ActivityManager is used to set the User's activity/status.</p>
	 * @return An {@link ActivityManager}
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/discord#getactivitymanager">
	 *     https://discordapp.com/developers/docs/game-sdk/discord#getactivitymanager</a>
	 */
	public ActivityManager activityManager()
	{
		return activityManager;
	}

	/**
	 * <p>Returns the {@link UserManager} associated with this core.</p>
	 * <p>A UserManager is used to receive information about Discord users.</p>
	 * @return A {@link UserManager}
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/discord#getusermanager">
	 *     https://discordapp.com/developers/docs/game-sdk/discord#getusermanager</a>
	 */
	public UserManager userManager()
	{
		return userManager;
	}

	/**
	 * <p>Returns the {@link OverlayManager} associated with this core.</p>
	 * <p>An OverlayManager is used to control the overlay for this game.</p>
	 * @return An {@link OverlayManager}
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/discord#getoverlaymanager">
	 *     https://discordapp.com/developers/docs/game-sdk/discord#getoverlaymanager</a>
	 */
	public OverlayManager overlayManager()
	{
		return overlayManager;
	}

	/**
	 * <p>Returns the {@link RelationshipManager} associated with this core.</p>
	 * <p>A RelationshipManager is used to receive information about the user's
	 * relationships with other Discord users (e.g. friends).</p>
	 * @return A {@link RelationshipManager}
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/discord#getrelationshipmanager">
	 *     https://discordapp.com/developers/docs/game-sdk/discord#getrelationshipmanager</a>
	 */
	public RelationshipManager relationshipManager()
	{
		return relationshipManager;
	}

	/**
	 * <p>Listens for new events and runs pending callbacks.</p>
	 * <p>This method should be called in a main loop every few millis.</p>
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/discord#setloghook">
	 *     https://discordapp.com/developers/docs/game-sdk/discord#runcallbacks</a>
	 */
	public void runCallbacks()
	{
		runCallbacks(pointer);
	}

	/**
	 * Registers a log function.
	 * @param minLevel Minimal level of message to receive.
	 * @param logHook Hook to send log messages to.
	 * @see Core#DEFAULT_LOG_HOOK
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/discord#setloghook">
	 *     https://discordapp.com/developers/docs/game-sdk/discord#setloghook</a>
	 */
	public void setLogHook(LogLevel minLevel, @NotNull BiConsumer<LogLevel, String> logHook)
	{
		setLogHook(pointer, minLevel.ordinal(), logHook);
	}

	/**
	 * <p>Closes and destroys the instance.</p>
	 * <p>This should be called at the end of the program.</p>
	 *
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/discord#destroy">
	 *     https://discordapp.com/developers/docs/game-sdk/discord#destroy</a>
	 */
	@Override
	public void close()
	{
		destroy(pointer);
	}

	/**
	 * <p>Return the pointer to the native structure.</p>
	 * <p>This is <b>not</b> an API method. Do <b>not</b> call it.</p>
	 * @return A native pointer.
	 */
	public long getPointer()
	{
		return pointer;
	}
}
