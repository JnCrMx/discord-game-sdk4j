package de.jcm.discordgamesdk.activity;

import java.util.function.Consumer;

/**
 * <p>Which type of invitation to open/send in
 * {@link de.jcm.discordgamesdk.OverlayManager#openActivityInvite(ActivityActionType)} or
 * {@link de.jcm.discordgamesdk.OverlayManager#openActivityInvite(ActivityActionType, Consumer)}.</p>
 * <p>Note that the native enum starts at index {@code 1} while this enum starts at index {@code 0}.
 * This should not bother you too much, but might be useful to know.</p>
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/overlay#data-models-activityactiontype-enum">
 *     https://discordapp.com/developers/docs/game-sdk/overlay#data-models-activityactiontype-enum</a>
 */
public enum ActivityActionType
{
	/**
	 * Send/open a join invitation.
	 */
	JOIN,
	/**
	 * Send/open a spectate invitation.
	 */
	SPECTATE
}
