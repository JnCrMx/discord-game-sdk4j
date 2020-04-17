package de.jcm.discordgamesdk.activity;

/**
 * Type of replies to "Ask to join" requests.
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/activities#data-models-activityjoinrequestreply-enum">
 *     https://discordapp.com/developers/docs/game-sdk/activities#data-models-activityjoinrequestreply-enum</a>
 */
public enum ActivityJoinRequestReply
{
	/**
	 * No. Deny the request.
	 */
	NO,
	/**
	 * Yes. Accept the request.
	 */
	YES,
	/**
	 * Ignore the request.
	 */
	IGNORE
}
