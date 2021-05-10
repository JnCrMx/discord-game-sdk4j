package de.jcm.discordgamesdk;

import de.jcm.discordgamesdk.lobby.LobbyMemberTransaction;

import java.util.function.BiConsumer;

/**
 * Manager for the communication over Discord's networking layer.
 * <p>
 * Not to be confused with the networking functions in {@link LobbyManager}.
 * This manager is for low-level networking, which means that
 * you need to manually update routes, exchange peer IDs, etc.
 * <p>
 * Basic plan to setup and use networking:
 * <ol>
 *     <li>Send your peer ID ({@link #getPeerId()}) to all peers you want to communicate with.
 *     You can e.g. do this with Lobby metadata.
 *     <li>Wait for a route update ({@link DiscordEventAdapter#onRouteUpdate(String)} and send
 *     your route to all peers you want to communicate with.
 *     Keep in mind that {@link DiscordEventAdapter#onRouteUpdate(String)} can fire,
 *     before you do {@link #getPeerId()}, so store the route somewhere for later use.
 *     <li>Your route can update at any time, so you need to notify all other peers
 *     about route updates.
 *     <li>When you have peer ID and route of a peer,
 *     open a connection to the peer with {@link #openPeer(long, String)}.
 *     <li>If the peer is already open and you receive a route update,
 *     tell Discord about it with {@link #updatePeer(long, String)}.
 *     <li>Open a channel to send data on with {@link #openChannel(long, byte, boolean)}.
 *     Both communication partners must open the same channel(s) with the same configuration(s).
 *     <li>Finally send messages with {@link #sendMessage(long, byte, byte[])}.
 *     Messages sent before the opposite peer is not ready (has not opened the channel),
 *     will be not be received by {@link DiscordEventAdapter#onMessage(long, byte, byte[])}, but
 *     produce a log message (see {@link Core#setLogHook(LogLevel, BiConsumer)}) instead.
 *     <li>You receive messages from other peers that you are connected to
 *     and opened channels for in {@link DiscordEventAdapter#onMessage(long, byte, byte[])}.
 *     <li>Flush the outgoing queue repeatedly with {@link #flush()} or no messages will be sent.
 *     <li>When you are done, first close the channel with {@link #closeChannel(long, byte)}...
 *     <li>and then the connection to the peer with {@link #closePeer(long)}.
 * </ol>
 * @see <a href="https://discord.com/developers/docs/game-sdk/networking">
 *     https://discord.com/developers/docs/game-sdk/networking</a>
 * @see <a href="https://discord.com/developers/docs/game-sdk/networking#connecting-to-each-other">
 *     https://discord.com/developers/docs/game-sdk/networking#connecting-to-each-other</a>
 * @see <a href="https://discord.com/developers/docs/game-sdk/networking#example-connecting-to-another-player-in-a-lobby">
 *     https://discord.com/developers/docs/game-sdk/networking#example-connecting-to-another-player-in-a-lobby</a>
 */
public class NetworkManager
{
	private final long pointer;
	private final Core core;

	NetworkManager(long pointer, Core core)
	{
		this.pointer = pointer;
		this.core = core;
	}

	/**
	 * Gets the peer ID of the current user.
	 * <p>
	 * A peer ID is used to open connections to other users and needs to be
	 * somehow sent to all other peers that you want to communicate with.
	 * This can e.g. be done with Lobby metadata;
	 * see {@link LobbyMemberTransaction#setMetadata(String, String)} and
	 * {@link LobbyManager#updateMember(long, long, LobbyMemberTransaction)}
	 * for more information.
	 * @return An <b>unsigned</b> long (use {@link Long#toUnsignedString(long)})
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see NetworkManager Basic concept for networking
	 * @see <a href="https://discord.com/developers/docs/game-sdk/networking#getpeerid">
	 *     https://discord.com/developers/docs/game-sdk/networking#getpeerid</a>
	 */
	public long getPeerId()
	{
		return core.execute(()->getPeerId(pointer));
	}

	/**
	 * Flushes the network.
	 * This sends out all pending network messages and hence should
	 * be called at the end of a (game) "tick".
	 * <p>
	 * Avoid calling this function inside any Discord callback;
	 * that has shown to be pretty troublesome.
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see NetworkManager Basic concept for networking
	 * @see <a href="https://discord.com/developers/docs/game-sdk/networking#flush">
	 *     https://discord.com/developers/docs/game-sdk/networking#flush</a>
	 * @see <a href="https://discord.com/developers/docs/game-sdk/networking#flush-vs-runcallbacks">
	 *     https://discord.com/developers/docs/game-sdk/networking#flush-vs-runcallbacks</a>
	 */
	public void flush()
	{
		Result result = core.execute(()->flush(pointer));
		if(result != Result.OK)
			throw new GameSDKException(result);
	}

	/**
	 * Opens a network connection to another Discord user/peer.
	 * @param peerId Peer ID of the user you want to connect to
	 * @param routeData The route of the user you want to connect to
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see NetworkManager Basic concept for networking
	 * @see <a href="https://discord.com/developers/docs/game-sdk/networking#openpeer">
	 *     https://discord.com/developers/docs/game-sdk/networking#openpeer</a>
	 */
	public void openPeer(long peerId, String routeData)
	{
		Result result = core.execute(()->openPeer(pointer, peerId, routeData));
		if(result != Result.OK)
			throw new GameSDKException(result);
	}

	/**
	 * Updates the route to an already open peer.
	 * <p>
	 * You want to do this when you receive a route update
	 * for a user that you are already connected to
	 * (e.g. in a {@link DiscordEventAdapter#onMemberUpdate(long, long)})
	 * in order to notify Discord about the new route.
	 * @param peerId Peer ID of the user you want to update
	 * @param routeData The new route for the user you want to update
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see NetworkManager Basic concept for networking
	 * @see <a href="https://discord.com/developers/docs/game-sdk/networking#updatepeer">
	 *     https://discord.com/developers/docs/game-sdk/networking#updatepeer</a>
	 */
	public void updatePeer(long peerId, String routeData)
	{
		Result result = core.execute(()->updatePeer(pointer, peerId, routeData));
		if(result != Result.OK)
			throw new GameSDKException(result);
	}

	/**
	 * Closes the network connection to another user/peer.
	 * @param peerId Peer ID of the user you want to disconnect from
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see NetworkManager Basic concept for networking
	 * @see <a href="https://discord.com/developers/docs/game-sdk/networking#closepeer">
	 *     https://discord.com/developers/docs/game-sdk/networking#closepeer</a>
	 */
	public void closePeer(long peerId)
	{
		Result result = core.execute(()->closePeer(pointer, peerId));
		if(result != Result.OK)
			throw new GameSDKException(result);
	}

	/**
	 * Opens a channel to another user/peer on a given channel number.
	 * <p>
	 * The channel ID can be an arbitrary {@code byte}, just keep in mind,
	 * that you need to open the same channel (same number and type) on both sides.
	 * <p>
	 * It seems to be good practise to use many channels to reduce the load
	 * on each of them.
	 * Messages with priority (e.g. heartbeat) should be sent on their own channel,
	 * so they don't get slowed down by other messages.
	 * For that it might be useful to always send certain types of messages
	 * on certain channels (e.g. channel 0 for handshaking, channel 1 for movement).
	 * <p>
	 * Use reliable channels for important data (world updates, loot, handshaking,
	 * heart beats, etc.) and unreliable channels for loss-tolerant data
	 * (player positions, movements, rotations, etc.).
	 * It of course depends on your game which data you want to send on reliable
	 * and which on unreliable channels.
	 * @param peerId Peer ID of the other user
	 * @param channelId ID of the channel
	 * @param reliable Whether the channel should be reliable (is for important data)
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see NetworkManager Basic concept for networking
	 * @see <a href="https://discord.com/developers/docs/game-sdk/networking#openchannel">
	 *     https://discord.com/developers/docs/game-sdk/networking#openchannel</a>
	 */
	public void openChannel(long peerId, byte channelId, boolean reliable)
	{
		Result result = core.execute(()->openChannel(pointer, peerId, channelId, reliable));
		if(result != Result.OK)
			throw new GameSDKException(result);
	}

	/**
	 * Closes the channel with a given channel ID to a given peer.
	 * @param peerId Peer ID of the other user
	 * @param channelId ID of the channel you want to close
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see NetworkManager Basic concept for networking
	 * @see <a href="https://discord.com/developers/docs/game-sdk/networking#closechannel">
	 *     https://discord.com/developers/docs/game-sdk/networking#closechannel</a>
	 */
	public void closeChannel(long peerId, byte channelId)
	{
		Result result = core.execute(()->closeChannel(pointer, peerId, channelId));
		if(result != Result.OK)
			throw new GameSDKException(result);
	}

	/**
	 * Sends a network message to the given peer over the given channel.
	 * <p>
	 * Make sure to {@link #flush()} or your messages won't be sent.
	 * @param peerId Peer ID of the other user/receiver
	 * @param channelId ID of the channel to send the message on
	 * @param data The messsage/data to send
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see NetworkManager Basic concept for networking
	 * @see <a href="https://discord.com/developers/docs/game-sdk/networking#sendmessage">
	 *     https://discord.com/developers/docs/game-sdk/networking#sendmessage</a>
	 */
	public void sendMessage(long peerId, byte channelId, byte[] data)
	{
		Result result = core.execute(()->sendMessage(pointer, peerId, channelId, data, 0, data.length));
		if(result != Result.OK)
			throw new GameSDKException(result);
	}

	private native long getPeerId(long pointer);

	private native Result flush(long pointer);

	private native Result openPeer(long pointer, long peerId, String routeData);
	private native Result updatePeer(long pointer, long peerId, String routeData);
	private native Result closePeer(long pointer, long peerId);

	private native Result openChannel(long pointer, long peerId, byte channelId, boolean reliable);
	private native Result closeChannel(long pointer, long peerId, byte channelId);
	private native Result sendMessage(long pointer, long peerId, byte channelId, byte[] data, int offset, int length);
}
