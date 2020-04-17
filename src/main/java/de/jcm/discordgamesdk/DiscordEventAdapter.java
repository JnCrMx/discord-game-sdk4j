package de.jcm.discordgamesdk;

import de.jcm.discordgamesdk.user.DiscordUser;
import de.jcm.discordgamesdk.user.Relationship;

/**
 * Adapter class for Discord events.
 */
public abstract class DiscordEventAdapter
{
	/**
	 * Fires when the user attempts to join a game by accepting an invite.
	 * @param secret The join or the match secret of the activity
	 * @see de.jcm.discordgamesdk.activity.ActivitySecrets#setJoinSecret(String)
	 * @see de.jcm.discordgamesdk.activity.ActivitySecrets#setMatchSecret(String)
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/activities#onactivityjoin">
	 *     https://discordapp.com/developers/docs/game-sdk/activities#onactivityjoin</a>
	 */
	public void onActivityJoin(String secret)
	{
	}

	/**
	 * Fires when the user attempts to spectate a game by accepting an invite or using the "spectate"-button.
	 * @param secret The spectate secret of the activity
	 * @see de.jcm.discordgamesdk.activity.ActivitySecrets#setSpectateSecret(String)
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/activities#onactivityspectate">
	 *     https://discordapp.com/developers/docs/game-sdk/activities#onactivityspectate</a>
	 */
	public void onActivitySpectate(String secret)
	{
	}

	/**
	 * Fires when a user asked to join the user by using "Ask to join" button.
	 * @param user User that requested to join
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/activities#onactivityjoinrequest">
	 *     https://discordapp.com/developers/docs/game-sdk/activities#onactivityjoinrequest</a>
	 */
	public void onActivityJoinRequest(DiscordUser user)
	{
	}

	/**
	 * <p>Fires when the current user changes their user information (avatar, username, etc.).</p>
	 * <p><i>Also</i> fires after initialization of the {@link UserManager} and therefore indicates
	 * that {@link UserManager#getCurrentUser()} is ready to be called.</p>
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/users#oncurrentuserupdate">
	 *     https://discordapp.com/developers/docs/game-sdk/users#oncurrentuserupdate</a>
	 */
	public void onCurrentUserUpdate()
	{
	}

	/**
	 * <p>Fires when the overlay is toggled (locked / unlocked).</p>
	 * <p>Apparently <i>also</i> fires after initialization of the {@link OverlayManager} and therefore
	 * tells you the initial overlay state.</p>
	 * @param locked Current state of the overlay
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/overlay#ontoggle">
	 *     https://discordapp.com/developers/docs/game-sdk/overlay#ontoggle</a>
	 */
	public void onOverlayToggle(boolean locked)
	{

	}

	/**
	 * <p>Fires when there is a new cached version of the user's relationships.</p>
	 * <p><i>Also</i> fires after initialization of the {@link RelationshipManager} and therefore
	 * indicates that its methods can be used to fetch relationship information.</p>
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/relationships#onrefresh">
	 *     https://discordapp.com/developers/docs/game-sdk/relationships#onrefresh</a>
	 */
	public void onRelationshipRefresh()
	{

	}

	/**
	 * Fires when information about a relationship (also user, presence, etc.)
	 * in the filtered list changes.
	 * @param relationship New relationship information
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/relationships#onrelationshipupdate">
	 *     https://discordapp.com/developers/docs/game-sdk/relationships#onrelationshipupdate</a>
	 */
	public void onRelationshipUpdate(Relationship relationship)
	{

	}
}
