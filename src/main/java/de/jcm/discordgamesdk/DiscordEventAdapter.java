package de.jcm.discordgamesdk;

/**
 * Adapter class for Discord events.
 */
public class DiscordEventAdapter
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
	 */
	public void onCurrentUserUpdate()
	{
	}
}
