package de.jcm.discordgamesdk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * The main component for accessing Discord's game SDK.
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/discord#functions-in-the-sdk">
 *     https://discordapp.com/developers/docs/game-sdk/discord#functions-in-the-sdk</a>
 * @author JCM
 */
public class Core implements AutoCloseable
{
	/**
	 * Extracts and initializes the native library.
	 * This method also loads Discord's native library.
	 * <p>
	 * The JNI library is extracted from the classpath (e.g. the currently running JAR)
	 * using {@link Class#getResourceAsStream(String)}.
	 * Its path inside the JAR must be of the pattern {@code /native/{os}/{arch}/{object name}}
	 * where {@code os} is either "windows" or "linux", {@code arch} is the system architecture as in
	 * the system property {@code os.arch} and {@code object name} is the name of the native object
	 * (e.g. "discord_game_sdk_jni.dll" on Windows or "libdiscord_game_sdk_jni.so" on Linux.
	 * <p>
	 * You may call this method more than once which unloads the old shared object and loads the new one.
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
		String name = "discord_game_sdk_jni";
		String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
		String arch = System.getProperty("os.arch").toLowerCase(Locale.ROOT);

		String objectName;

		if(osName.contains("windows"))
		{
			osName = "windows";
			objectName = name + ".dll";

			// the Discord native library needs to be loaded before our JNI library on Windows
			System.load(discordLibrary.getAbsolutePath());
		}
		else if(osName.contains("linux"))
		{
			osName = "linux";
			objectName = "lib" + name + ".so";
		}
		else if(osName.contains("mac os"))
		{
			osName = "macos";
			objectName = "lib" + name + ".dylib";
		}
		else
		{
			throw new RuntimeException("cannot determine OS type: "+osName);
		}

		/*
		Some systems (e.g. Mac OS X) might report the architecture as "x86_64" instead of "amd64".
		While it would be possible to store the MacOS dylib as "x86_x64" instead of "amd64",
		I personally prefer to keep the system architecture consistent.
		 */
		if(arch.equals("x86_64"))
			arch = "amd64";

		String path = "/native/"+osName+"/"+arch+"/"+objectName;
		InputStream in = Core.class.getResourceAsStream(path);
		if(in == null)
			throw new RuntimeException(new FileNotFoundException("cannot find native library at "+path));

		File tempDir = new File(System.getProperty("java.io.tmpdir"), "java-"+name+System.nanoTime());
		if(!tempDir.mkdir())
			throw new RuntimeException(new IOException("Cannot create temporary directory"));
		tempDir.deleteOnExit();

		File temp = new File(tempDir, objectName);
		temp.deleteOnExit();

		try
		{
			Files.copy(in, temp.toPath());
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}

		System.load(temp.getAbsolutePath());
		initDiscordNative(discordLibrary.getAbsolutePath());
	}

	/**
	 * Loads Discord's SDK library.
	 * <p>
	 * This does not extract nor load the JNI native library.
	 * If you want to do that, please use {@link Core#init(File)}
	 * which extracts and loads the JNI native and then calls this method.
	 * @param discordPath Location of Discord's native library.
	 *                    <p>On Windows the filename (last component of the path) must be
	 *                    "discord_game_sdk.dll" or an {@link UnsatisfiedLinkError} will occur.</p>
	 *                    <p>On Linux the filename does not matter.</p>
	 */
	public static native void initDiscordNative(String discordPath);

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

	private final long pointer;

	private final CreateParams createParams;
	private final AtomicBoolean open = new AtomicBoolean(true);
	private final ReentrantLock lock = new ReentrantLock();

	private final ActivityManager activityManager;
	private final UserManager userManager;
	private final OverlayManager overlayManager;
	private final RelationshipManager relationshipManager;
	private final ImageManager imageManager;
	private final LobbyManager lobbyManager;
	private final NetworkManager networkManager;
	private final VoiceManager voiceManager;

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
		this.createParams = params;
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

		this.activityManager = new ActivityManager(getActivityManager(pointer), this);
		this.userManager = new UserManager(getUserManager(pointer), this);
		this.overlayManager = new OverlayManager(getOverlayManager(pointer), this);
		this.relationshipManager = new RelationshipManager(getRelationshipManager(pointer), this);
		this.imageManager = new ImageManager(getImageManager(pointer), this);
		this.lobbyManager = new LobbyManager(getLobbyManager(pointer), this);
		this.networkManager = new NetworkManager(getNetworkManager(pointer), this);
		this.voiceManager = new VoiceManager(getVoiceManager(pointer), this);
	}

	private native Object create(long paramPointer);
	private native void destroy(long pointer);

	private native long getActivityManager(long pointer);
	private native long getUserManager(long pointer);
	private native long getOverlayManager(long pointer);
	private native long getRelationshipManager(long pointer);
	private native long getImageManager(long pointer);
	private native long getLobbyManager(long pointer);
	private native long getNetworkManager(long pointer);
	private native long getVoiceManager(long pointer);

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
	 * <p>Returns the {@link ImageManager} associated with this core.</p>
	 * <p>An ImageManager is used to fetch images and information
	 * about images (dimensions) from Discord (mainly avatars).</p>
	 * @return An {@link ImageManager}
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/discord#getimagemanager">
	 *     https://discordapp.com/developers/docs/game-sdk/discord#getimagemanager</a>
	 */
	public ImageManager imageManager()
	{
		return imageManager;
	}

	/**
	 * <p>Returns the {@link LobbyManager} associated with this core.</p>
	 * <p>A LobbyManager is used to create, manage and connect to Discord Lobbies.</p>
	 * @return A {@link LobbyManager}
	 * @see <a href="https://discord.com/developers/docs/game-sdk/discord#getlobbymanager">
	 *     https://discord.com/developers/docs/game-sdk/discord#getlobbymanager</a>
	 */
	public LobbyManager lobbyManager()
	{
		return lobbyManager;
	}

	/**
	 * Returns the {@link NetworkManager} associated with this core.
	 * <p>
	 * A NetworkManager can be used to open network channels over
	 * Discord on which you can send arbitrary messages.
	 * @return A {@link NetworkManager}
	 * @see <a href="https://discord.com/developers/docs/game-sdk/discord#getnetworkmanager">
	 *     https://discord.com/developers/docs/game-sdk/discord#getnetworkmanager</a>
	 */
	public NetworkManager networkManager()
	{
		return networkManager;
	}

	/**
	 * Returns the {@link VoiceManager} associated with this core.
	 * <p>
	 * A VoiceManager is used to control Discord Lobby voice channels.
	 * It can be used to configure input modes, to mute and deaf the current user,
	 * to locally mute other users and to locally adjust their volume.
	 * @return A {@link VoiceManager}
	 * @see LobbyManager#connectVoice(long)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/discord#getvoicemanager">
	 *     https://discord.com/developers/docs/game-sdk/discord#getvoicemanager</a>
	 */
	public VoiceManager voiceManager()
	{
		return voiceManager;
	}

	/**
	 * <p>Listens for new events and runs pending callbacks.</p>
	 * <p>This method should be called in a main loop every few millis.</p>
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/discord#setloghook">
	 *     https://discordapp.com/developers/docs/game-sdk/discord#runcallbacks</a>
	 */
	public void runCallbacks()
	{
		execute(()->runCallbacks(pointer));
	}

	/**
	 * Registers a log function.
	 * @param minLevel Minimal level of message to receive.
	 * @param logHook Hook to send log messages to.
	 * @see Core#DEFAULT_LOG_HOOK
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/discord#setloghook">
	 *     https://discordapp.com/developers/docs/game-sdk/discord#setloghook</a>
	 */
	public void setLogHook(LogLevel minLevel, BiConsumer<LogLevel, String> logHook)
	{
		execute(()->setLogHook(pointer, minLevel.ordinal(), Objects.requireNonNull(logHook)));
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
		if(open.compareAndSet(true, false))
		{
			lock.lock();
			try
			{
				destroy(pointer);
			}
			finally
			{
				lock.unlock();
			}
			createParams.close();
		}
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

	void execute(Runnable runnable)
	{
		execute((Supplier<Void>) () ->
		{
			runnable.run();
			return null;
		});
	}

	<T> T execute(Supplier<T> provider)
	{
		if(!open.get())
			throw new IllegalStateException("Core is closed");

		lock.lock();
		try
		{
			return provider.get();
		}
		finally
		{
			lock.unlock();
		}
	}
}
