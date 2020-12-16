package de.jcm.discordgamesdk.activity;

/**
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/activities#data-models-activitytype-enum">
 *     https://discordapp.com/developers/docs/game-sdk/activities#data-models-activitytype-enum</a>
 * @see <a href="https://discordapp.com/developers/docs/topics/gateway#activity-object-activity-types">
 *     https://discordapp.com/developers/docs/topics/gateway#activity-object-activity-types</a>
 */
public enum ActivityType
{
	/**
	 * <p>The user is playing a game or doing nothing (default).</p>
	 * <p>You can see if the user is playing something by checking if
	 * {@link Activity#getApplicationId()} returns an actual ID rather than {@code 0}.</p>
	 */
	PLAYING,
	STREAMING,
	LISTENING,
	UNUSED,
	CUSTOM,
	COMPETING
}
