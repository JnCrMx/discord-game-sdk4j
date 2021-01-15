package de.jcm.discordgamesdk;

import de.jcm.discordgamesdk.lobby.Lobby;
import de.jcm.discordgamesdk.lobby.LobbyMemberTransaction;
import de.jcm.discordgamesdk.lobby.LobbyTransaction;
import de.jcm.discordgamesdk.user.DiscordUser;
import de.jcm.discordgamesdk.user.Relationship;

import java.util.function.BiConsumer;

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

	/**
	 * Fires when a Lobby the current user is connected to is updated.
	 * This happens when the Lobby owner calls
	 * {@link LobbyManager#updateLobby(long, LobbyTransaction)}.
	 * @param lobbyId ID of the Lobby that has been updated
	 * @see LobbyManager#updateLobby(long, LobbyTransaction)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#onlobbyupdate">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#onlobbyupdate</a>
	 */
	public void onLobbyUpdate(long lobbyId)
	{

	}

	/**
	 * Fires when a Lobby the current user is connected to is deleted.
	 * This happens when the Lobby owner calls {@link LobbyManager#deleteLobby(long)}.
	 * @param lobbyId ID of the Lobby that has been deleted
	 * @param reason Reason for deletion, an undocumented system message
	 * @see LobbyManager#deleteLobby(long)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#onlobbydelete">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#onlobbydelete</a>
	 */
	public void onLobbyDelete(long lobbyId, int reason)
	{

	}

	/**
	 * Fires when a new member join a Lobby the current user is connected to.
	 * @param lobbyId ID of the Lobby that the member joined
	 * @param userId User ID of the member
	 * @see LobbyManager#connectLobby(long, String, BiConsumer)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#onmemberconnect">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#onmemberconnect</a>
	 */
	public void onMemberConnect(long lobbyId, long userId)
	{

	}

	/**
	 * Fires when metadata for a Lobby member is updated.
	 * <p>
	 * The event also fires for metadata updates related to Lobby networking.
	 * @param lobbyId ID of the Lobby the member is in
	 * @param userId User ID of the member whose metadata has been updated
	 * @see LobbyManager#updateMember(long, long, LobbyMemberTransaction)
	 * @see LobbyManager#connectNetwork(long)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#onmemberupdate">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#onmemberupdate</a>
	 */
	public void onMemberUpdate(long lobbyId, long userId)
	{

	}

	/**
	 * Fires when a member leaves a Lobby the current user is connected to.
	 * @param lobbyId ID of the Lobby the member was in
	 * @param userId User ID of the member
	 * @see LobbyManager#disconnectLobby(long)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#onmemberdisconnect">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#onmemberdisconnect</a>
	 */
	public void onMemberDisconnect(long lobbyId, long userId)
	{

	}

	/**
	 * Fires when a message is sent to a Lobby.
	 * This only applies to messages sent via
	 * {@link LobbyManager#sendLobbyMessage(long, byte[])}.
	 * @param lobbyId ID of the Lobby that the message was sent to
	 * @param userId User ID of the member that sent the message
	 * @param data The message
	 * @see LobbyManager#sendLobbyMessage(long, byte[])
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#onlobbymessage">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#onlobbymessage</a>
	 */
	public void onLobbyMessage(long lobbyId, long userId, byte[] data)
	{

	}

	/**
	 * Fires when a member starts or stops speaking in a Lobby voice chat.
	 * This event fires only if the current user is connected to a Lobby voice chat
	 * in the first place (see {@link LobbyManager#connectVoice(long)}).
	 * @param lobbyId ID of the Lobby the users if talking in
	 * @param userId User ID of the member that started or stopped speaking
	 * @param speaking {@code true} if the member has started speaking, {@code false} if they stopped
	 * @see LobbyManager#connectVoice(long)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#onspeaking">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#onspeaking</a>
	 */
	public void onSpeaking(long lobbyId, long userId, boolean speaking)
	{

	}

	/**
	 * Fires when a message over the Lobby networking layer is received.
	 * A message can be sent with {@link LobbyManager#sendNetworkMessage(Lobby, long, byte, byte[])}.
	 * <p>
	 * Preconditions for this event to fire:
	 * <ol>
	 *     <li>The current user is connected to a lobby
	 *     (see {@link LobbyManager#connectLobby(long, String, BiConsumer)})
	 *     <li>The current user is connected to the Lobby's network
	 *     (see {@link LobbyManager#connectNetwork(long)})
	 *     <li>The channel on which the message has been sent is open
	 *     (see {@link LobbyManager#openNetworkChannel(long, byte, boolean)})
	 * </ol>
	 * See <a href="https://discord.com/developers/docs/game-sdk/lobbies#integrated-networking">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#integrated-networking</a> and
	 * <a href="https://discord.com/developers/docs/game-sdk/lobbies#example-networking-the-easy-way">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#example-networking-the-easy-way</a>
	 * for more details and examples.
	 * @param lobbyId ID of the Lobby the message was sent in
	 * @param userId User ID of the member that sent the message
	 * @param channelId ID of the channel on which the message was sent
	 * @param data The message
	 * @see LobbyManager#connectNetwork(long)
	 * @see LobbyManager#openNetworkChannel(long, byte, boolean)
	 * @see LobbyManager#sendNetworkMessage(long, long, byte, byte[])
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#onnetworkmessage">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#onnetworkmessage</a>
	 */
	public void onNetworkMessage(long lobbyId, long userId, byte channelId, byte[] data)
	{

	}

	/**
	 * Fires when you receive a message from another user/peer.
	 * Only fires if you already have an open channel for that peer
	 * (see {@link NetworkManager#openChannel(long, byte, boolean)}.
	 * @param peerId Peer ID of the sender
	 * @param channelId ID of the channel the message was sent on
	 * @param data The message/data sent
	 * @see <a href="https://discord.com/developers/docs/game-sdk/networking#onmessage">
	 *     https://discord.com/developers/docs/game-sdk/networking#onmessage</a>
	 */
	public void onMessage(long peerId, byte channelId, byte[] data)
	{

	}

	/**
	 * Fires when your current networking route changes.
	 * <p>
	 * You probably want to notify all peer you are connect to
	 * about the new route, so they can call
	 * {@link NetworkManager#updatePeer(long, String)} accordingly.
	 * <p>
	 * One way of broadcasting your route is through Lobby member metadata
	 * (see {@link LobbyMemberTransaction#setMetadata(String, String)}).
	 * @param routeData Your new route
	 * @see <a href="https://discord.com/developers/docs/game-sdk/networking#onrouteupdate">
	 *     https://discord.com/developers/docs/game-sdk/networking#onrouteupdate</a>
	 */
	public void onRouteUpdate(String routeData)
	{

	}
}
