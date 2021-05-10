package de.jcm.discordgamesdk.lobby;

import de.jcm.discordgamesdk.LobbyManager;
import de.jcm.discordgamesdk.Result;
import de.jcm.discordgamesdk.user.DiscordUser;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Data holding structure representing a Discord Lobby created via the {@link LobbyManager}.
 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#data-models-lobby-struct">
 *     https://discord.com/developers/docs/game-sdk/lobbies#data-models-lobby-struct</a>
 */
public class Lobby
{
	private final long id;
	private final LobbyType type;
	private final long ownerId;
	private final String secret;
	private final int capacity;
	private final boolean locked;

	/**
	 * Constructs a new lobby object with the given parameters.
	 * <p>
	 * Currently only used by native code. It has probably not much
	 * sense to construct a Lobby object manually as it is never required
	 * as a parameter to SDK functions.
	 * @param id Snowflake ID of the Lobby
	 * @param type Type (public, private) of the Lobby.
	 * @param ownerId ID of the user that own the Lobby.
	 * @param secret Secret that is required to join the Lobby, max. 127 bytes.
	 * @param capacity Maximum count of players that this Lobby can hold.
	 * @param locked Whether this Lobby is locked, so no new players can join.
	 * @throws IllegalArgumentException if the secret is too long
	 */
	public Lobby(long id, int type, long ownerId, String secret, int capacity, boolean locked)
	{
		this.id = id;
		this.type = LobbyType.values()[type-1];
		this.ownerId = ownerId;
		this.secret = secret;
		this.capacity = capacity;
		this.locked = locked;

		if(this.secret.getBytes().length >= 128)
			throw new IllegalArgumentException("max secret length is 127");
	}

	/**
	 * Return the ID of this Lobby which also is a Discord Snowflake.
	 * @return The ID of this Lobby.
	 * @see LobbyManager#getLobbyActivitySecret(Lobby)
	 */
	public long getId()
	{
		return id;
	}

	/**
	 * Returns the {@link LobbyType} of this Lobby (e.g. public, private).
	 * <p>
	 * Public Lobbies can be found via {@link LobbyManager#search(LobbySearchQuery, Consumer)}
	 * while private Lobbies cannot. You can only join them via their ID and secret
	 * or their activity secret (a concatenation of ID and secret) which can be obtained
	 * with {@link LobbyManager#getLobbyActivitySecret(Lobby)}.
	 * @return The type of this Lobby.
	 * @see LobbyTransaction#setType(LobbyType)
	 */
	public LobbyType getType()
	{
		return type;
	}

	/**
	 * Returns the user ID (a Discord Snowflake, see {@link DiscordUser#getUserId()}) of the user
	 * that owns this lobby.
	 * <p>
	 * Initially this is the user on whose behalf the Lobby was created, but that can of course change,
	 * e.g. when the owner leaves their own Lobby or the owner is changed with a
	 * {@link LobbyManager#updateLobby(Lobby, LobbyTransaction)}.
	 * @return The user ID of the owner of this Lobby.
	 * @see LobbyTransaction#setOwner(long) 
	 */
	public long getOwnerId()
	{
		return ownerId;
	}

	/**
	 * Returns a secret which is required to join the Lobby via
	 * {@link LobbyManager#connectLobby(long, String, BiConsumer)}.
	 * <p>
	 * It is unclear in which context this is really a "secret" as it can be obtained via
	 * {@link LobbyManager#getLobby(long)} for public Lobbies after searching for them with
	 * {@link LobbyManager#search(LobbySearchQuery, Consumer)}.
	 * @return The secret of this Lobby, max. 127 bytes in length
	 * @see LobbyManager#getLobbyActivitySecret(Lobby)
	 */
	public String getSecret()
	{
		return secret;
	}

	/**
	 * Returns the capacity of this Lobby and thus how many users can connect to it at the same time.
	 * <p>
	 * If the Lobby is full, attempting to join it will result in a {@link Result#LOBBY_FULL}.
	 * <p>
	 * The capacity seems to be in the range of 1 (inclusive) to 1024 (inclusive), as Lobby creations
	 * outside this range appear to fail.
	 * A value of 0 indicates that the structure if empty or filled incorrectly.
	 * @return The capacity of this Lobby.
	 * @see LobbyTransaction#setCapacity(int) 
	 */
	public int getCapacity()
	{
		return capacity;
	}

	/**
	 * Returns whether this Lobby is locked meaning that no new players can join it.
	 * <p>
	 * Attempting to join a locked Lobby will result in a {@link Result#LOBBY_FULL}.
	 * <p>
	 * For some reason a locked Lobby cannot be found with {@link LobbyManager#search(LobbySearchQuery, Consumer)}.
	 * Therefore it acts like a "stronger" version of {@link LobbyType#PRIVATE}
	 * as it cannot be connected to using ID and secret.
	 * @return {@code true} if this Lobby is locked.
	 * @see LobbyTransaction#setLocked(boolean) 
	 */
	public boolean isLocked()
	{
		return locked;
	}

	/**
	 * Creates a simple string representation of this Lobby containing:
	 * <ul>
	 *     <li>the ID ({@link #getId()})
	 *     <li>the type ({@link #getType()})
	 *     <li>the user ID of the owner ({@link #getOwnerId()})
	 *     <li>the secret ({@link #getSecret()})
	 *     <li>the capacity ({@link #getCapacity()})
	 *     <li>whether the Lobby is locked ({@link #isLocked()})
	 * </ul>
	 * <p>
	 * It is <b>not</b> recommended to use this method for serialization.
	 * It is only intended for debugging.
	 * <p>
	 * The methods uses the plain fields rather than the named getter methods.
	 * Overriding those will hence not change to return value of this method.
	 * @return A string representation of this Lobby.
	 */
	@Override
	public String toString()
	{
		return "Lobby{" +
				"id=" + id +
				", type=" + type +
				", ownerId=" + ownerId +
				", secret='" + secret + '\'' +
				", capacity=" + capacity +
				", locked=" + locked +
				'}';
	}
}
