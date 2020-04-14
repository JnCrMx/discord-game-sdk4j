package de.jcm.discordgamesdk;

import de.jcm.discordgamesdk.activity.Activity;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Manager to control the player's current activity.
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/activities">
 *     https://discordapp.com/developers/docs/game-sdk/activities</a>
 */
public class ActivityManager
{
	private long pointer;

	ActivityManager(long pointer)
	{
		this.pointer = pointer;
	}

	/**
	 * <p>Updates the user's current presence to a new activity.</p>
	 * <p>The {@link Core#DEFAULT_CALLBACK} is used to process the returned {@link Result}.</p>
	 * @param activity New activity for the user.
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/activities#updateactivity">
	 *     https://discordapp.com/developers/docs/game-sdk/activities#updateactivity</a>
	 */
	public void updateActivity(Activity activity)
	{
		updateActivity(activity, Core.DEFAULT_CALLBACK);
	}

	/**
	 * <p>Updates the user's current presence to a new activity.</p>
	 * <p>A custom callback is used to process the returned {@link Result}.</p>
	 * @param activity New activity for the user.
	 * @param callback Callback to process the returned {@link Result}.
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/activities#updateactivity">
	 *     https://discordapp.com/developers/docs/game-sdk/activities#updateactivity</a>
	 */
	public void updateActivity(Activity activity, @NotNull Consumer<Result> callback)
	{
		updateActivity(pointer, activity.getPointer(), callback);
	}

	public void clearActivity()
	{
		clearActivity(Core.DEFAULT_CALLBACK);
	}

	public void clearActivity(@NotNull Consumer<Result> callback)
	{
		clearActivity(pointer, callback);
	}

	private native void updateActivity(long pointer, long activityPointer, Consumer<Result> callback);
	private native void clearActivity(long pointer, Consumer<Result> callback);
}
