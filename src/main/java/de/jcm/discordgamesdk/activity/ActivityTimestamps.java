package de.jcm.discordgamesdk.activity;

import java.time.Instant;

/**
 * A structure used to make "elapsed" and "remaining" durations.
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/activities#data-models-activitytimestamps-struct">
 *     https://discordapp.com/developers/docs/game-sdk/activities#data-models-activitytimestamps-struct</a>
 */
public class ActivityTimestamps
{
	private Long start;
	private Long end;

	/**
	 * <p>Sets the time the user started playing.</p>
	 * <p>This causes an "elapsed" duration to show up.</p>
	 * @param start Start time
	 */
	public void setStart(Instant start)
	{
		this.start = start.getEpochSecond();
		this.end = null;
	}

	/**
	 * Gets the time the user started playing.
	 * @return Start time
	 */
	public Instant getStart()
	{
		return Instant.ofEpochSecond(start);
	}

	/**
	 * <p>Sets the time the user will be done.</p>
	 * <p>This causes an "remaining" duration to show up.</p>
	 * @param end End time
	 */
	public void setEnd(Instant end)
	{
		this.start = null;
		this.end = end.getEpochSecond();
	}

	/**
	 * Gets the time the user will be done.
	 * @return End time
	 */
	public Instant getEnd()
	{
		return Instant.ofEpochSecond(end);
	}
}
