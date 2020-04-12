package de.jcm.discordgamesdk;

import java.util.function.Consumer;

public class Core implements AutoCloseable
{
	public static final Consumer<Result> DEFAULT_CALLBACK = result ->
	{
		if(result!=Result.OK)
			throw new GameSDKException(result);
	};

	private long pointer;

	private ActivityManager activityManager;

	public Core(CreateParams params)
	{
		this.pointer = create(params.getPointer());

		this.activityManager = new ActivityManager(getActivityManager(pointer));
	}

	private native long create(long paramPointer);
	private native void destroy(long pointer);

	private native long getActivityManager(long pointer);

	private native void runCallbacks(long pointer);

	public ActivityManager activityManager()
	{
		return activityManager;
	}

	public void runCallbacks()
	{
		runCallbacks(pointer);
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
