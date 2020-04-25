package de.jcm.discordgamesdk.user;

import de.jcm.discordgamesdk.activity.Activity;

/**
 * Representation of a user's current presence (usually provided with a {@link Relationship}).
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/relationships#data-models-presence-struct">
 *     https://discordapp.com/developers/docs/game-sdk/relationships#data-models-presence-struct</a>
 */
public class Presence
{
	private final OnlineStatus status;
	private final Activity activity;

	Presence(OnlineStatus status, Activity activity)
	{
		this.status = status;
		this.activity = activity;
	}

	/**
	 * Gets the online/Discord status of the user.
	 * @return An OnlineStatus
	 */
	public OnlineStatus getStatus()
	{
		return status;
	}

	/**
	 * Gets the current Activity of the user.
	 * @return An Activity
	 */
	public Activity getActivity()
	{
		return activity;
	}

	/**
	 * <p>Generates a string representation of the presence containing all its attributes.</p>
	 * <p>This is just one of <i>IntelliJ IDEA</i>'s default {@code toString()}-Methods,
	 * so don't expect anything special.</p>
	 * @return A string representation of the presence
	 */
	@Override
	public String toString()
	{
		return "Presence{" +
				"status=" + status +
				", activity=" + activity +
				'}';
	}
}
