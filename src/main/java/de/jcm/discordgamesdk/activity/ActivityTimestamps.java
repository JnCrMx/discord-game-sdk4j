package de.jcm.discordgamesdk.activity;

import java.time.Instant;

public class ActivityTimestamps
{
	private long pointer;

	ActivityTimestamps(long pointer)
	{
		this.pointer = pointer;
	}

	public void setStart(Instant start)
	{
		setStart(pointer, start.getEpochSecond());
	}
	public Instant getStart()
	{
		return Instant.ofEpochSecond(getStart(pointer));
	}

	public void setEnd(Instant end)
	{
		setEnd(pointer, end.getEpochSecond());
	}
	public Instant getEnd()
	{
		return Instant.ofEpochSecond(getEnd(pointer));
	}

	private native void setStart(long pointer, long start);
	private native long getStart(long pointer);

	private native void setEnd(long pointer, long end);
	private native long getEnd(long pointer);
}
