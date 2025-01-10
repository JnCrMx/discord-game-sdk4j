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
	 * <p>If the end timestamp ({@link ActivityTimestamps#setEnd}) is set as well,
	 * this cause a progress indicator to show up.</p>
	 * <p>Otherwise, this causes an "elapsed" duration to show up.</p>
	 * @param start Start time
	 */
	public void setStart(Instant start)
	{
		this.start = start.getEpochSecond();
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
	 * <p>If the start timestamp ({@link ActivityTimestamps#setStart}) is set as well,
	 * this cause a progress indicator to show up.</p>
	 * <p>This causes a "remaining" duration to show up.</p>
	 * @param end End time
	 */
	public void setEnd(Instant end)
	{
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

	/**
	 * <p>Sets both start and end time of the activity simultaneously.</p>
	 * <p>This causes a progress indicator to show up.</p>
	 * <p>This method exists purely for convenience and is equivalent to calling {@link ActivityTimestamps#setStart}
	 * and {@link ActivityTimestamps#setEnd} individually.</p>
	 * @param start Start time
	 * @param end End time
	 */
	public void setStartAndEnd(Instant start, Instant end) {
		this.start = start.getEpochSecond();
		this.end = end.getEpochSecond();
	}

	/**
	 * Clears the start time.
	 */
	public void clearStart() {
		this.start = null;
	}

	/**
	 * Clears the end time.
	 */
	public void clearEnd() {
		this.end = null;
	}

	/**
	 * <p>Clears both start and end time simultaneously.</p>
	 * <p>This method exists purely for convenience and is equivalent to calling {@link ActivityTimestamps#clearStart}
	 * and {@link ActivityTimestamps#clearEnd} individually.</p>
	 */
	public void clear() {
		this.start = null;
		this.end = null;
	}
}
