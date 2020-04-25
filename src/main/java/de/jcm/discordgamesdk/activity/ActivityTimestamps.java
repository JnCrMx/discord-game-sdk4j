package de.jcm.discordgamesdk.activity;

import java.time.Instant;

/**
 * A structure used to make "elapsed" and "remaining" durations.
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/activities#data-models-activitytimestamps-struct">
 *     https://discordapp.com/developers/docs/game-sdk/activities#data-models-activitytimestamps-struct</a>
 */
public class ActivityTimestamps
{
	private final long pointer;

	ActivityTimestamps(long pointer)
	{
		this.pointer = pointer;
	}

	/**
	 * <p>Sets the time the user started playing.</p>
	 * <p>This causes an "elapsed" duration to show up.</p>
	 * @param start Start time
	 */
	public void setStart(Instant start)
	{
		setStart(pointer, start.getEpochSecond());
	}

	/**
	 * Gets the time the user started playing.
	 * @return Start time
	 */
	public Instant getStart()
	{
		return Instant.ofEpochSecond(getStart(pointer));
	}

	/**
	 * <p>Sets the time the user will be done.</p>
	 * <p>This causes an "remaining" duration to show up.</p>
	 * @param end End time
	 */
	public void setEnd(Instant end)
	{
		setEnd(pointer, end.getEpochSecond());
	}

	/**
	 * Gets the time the user will be done.
	 * @return End time
	 */
	public Instant getEnd()
	{
		return Instant.ofEpochSecond(getEnd(pointer));
	}

	private native void setStart(long pointer, long start);
	private native long getStart(long pointer);

	private native void setEnd(long pointer, long end);
	private native long getEnd(long pointer);
}
