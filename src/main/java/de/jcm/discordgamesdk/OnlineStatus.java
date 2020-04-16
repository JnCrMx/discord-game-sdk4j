package de.jcm.discordgamesdk;

/**
 * The Discord status of a user (usually party of their {@link Presence}).
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/relationships#data-models-status-enum">
 *     https://discordapp.com/developers/docs/game-sdk/relationships#data-models-status-enum</a>
 */
public enum OnlineStatus
{
	/**
	 * The user is offline.
	 */
	OFFLINE,
	/**
	 * The user is online.
	 */
	ONLINE,
	/**
	 * The user is AFK or in idle mode.
	 */
	IDLE,
	/**
	 * The user has enabled the DND (do not disturb) mode.
	 */
	DO_NO_DISTURB
}
