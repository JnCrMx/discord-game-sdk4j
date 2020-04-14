package de.jcm.discordgamesdk;

import cz.adamh.utils.NativeUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Core implements AutoCloseable
{
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

	public static final Consumer<Result> DEFAULT_CALLBACK = result ->
	{
		if(result!=Result.OK)
			throw new GameSDKException(result);
	};

	public static final BiConsumer<LogLevel, String> DEFAULT_LOG_HOOK= (level, message) ->
	{
		System.out.printf("[%s] %s\n", level, message);
	};

	private long pointer;

	private ActivityManager activityManager;

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
	}

	private native Object create(long paramPointer);
	private native void destroy(long pointer);

	private native long getActivityManager(long pointer);

	private native void runCallbacks(long pointer);

	private native void setLogHook(long pointer, int minLevel, BiConsumer<LogLevel, String> logHook);

	public ActivityManager activityManager()
	{
		return activityManager;
	}

	public void runCallbacks()
	{
		runCallbacks(pointer);
	}

	public void setLogHook(LogLevel minLevel, @NotNull BiConsumer<LogLevel, String> logHook)
	{
		setLogHook(pointer, minLevel.ordinal(), logHook);
	}

	@Override
	public void close()
	{
		destroy(pointer);
	}

	public long getPointer()
	{
		return pointer;
	}
}
