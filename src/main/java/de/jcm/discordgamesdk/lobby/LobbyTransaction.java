package de.jcm.discordgamesdk.lobby;

import de.jcm.discordgamesdk.GameSDKException;
import de.jcm.discordgamesdk.LobbyManager;
import de.jcm.discordgamesdk.Result;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A transaction used to create or update a Lobby.
 * <p>
 * An instance of this can only be obtained by {@link LobbyManager#getLobbyCreateTransaction()}
 * and {@link LobbyManager#getLobbyUpdateTransaction(Lobby)}.
 * <p>
 * The instance should be used to do <i>only one</i> creation or update an should be discarded
 * after that.
 * <p>
 * Closing or freeing the instance does not seem to be required or even possible.
 * @see LobbyManager#getLobbyCreateTransaction()
 * @see LobbyManager#getLobbyUpdateTransaction(Lobby)
 * @see LobbyManager#createLobby(LobbyTransaction, BiConsumer)
 * @see LobbyManager#updateLobby(Lobby, LobbyTransaction)
 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#data-models-lobbytransaction-struct">
 *     https://discord.com/developers/docs/game-sdk/lobbies#data-models-lobbytransaction-struct</a>
 */
public class LobbyTransaction
{
	private final long pointer;

	LobbyTransaction(long pointer)
	{
		this.pointer = pointer;
	}

	/**
	 * Returns a pointer to the native structure backing this transaction.
	 * <p>
	 * This method should <b>never</b> be called outside intern code.
	 * @return A native pointer.
	 */
	public long getPointer()
	{
		return pointer;
	}

	/**
	 * Sets the type (public, private) of the Lobby created/updated by this transaction.
	 * <p>
	 * Public Lobbies can be found via {@link LobbyManager#search(LobbySearchQuery, Consumer)}
	 * while private Lobbies cannot. You can only join them via their ID and secret
	 * or their activity secret (a concatenation of ID and secret) which can be obtained
	 * with {@link LobbyManager#getLobbyActivitySecret(Lobby)}
	 * @param type The type of the Lobby.
	 * @throws GameSDKException if anything goes wrong on the native side
	 * @see Lobby#getType()
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#lobbytransactionsettype">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#lobbytransactionsettype</a>
	 */
	public void setType(LobbyType type)
	{
		Result result = setType(pointer, type.ordinal()+1);
		if(result != Result.OK)
		{
			throw new GameSDKException(result);
		}
	}

	/**
	 * Sets the owner of the updated Lobby to a new user specified by their Discord User ID.
	 * <p>
	 * This is only allowed in an update transaction obtained with {@link LobbyManager#getLobbyUpdateTransaction(Lobby)}.
	 * In a create transaction this value is at best ignored, but might cause some errors, so do <b>not</b> set it.
	 * @param ownerId The user ID of the new owner of the Lobby.
	 * @throws GameSDKException if anything goes wrong on the native side
	 * @see Lobby#getOwnerId()
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#lobbytransactionsetowner">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#lobbytransactionsetowner</a>
	 */
	public void setOwner(long ownerId)
	{
		Result result = setOwner(pointer, ownerId);
		if(result != Result.OK)
		{
			throw new GameSDKException(result);
		}
	}

	/**
	 * Sets a the capacity (how many users can connect to the Lobby at the same time) of the created/updated Lobby.
	 * <p>
	 * If the capacity is set too hight or too low, a create or update operation will fail
	 * with a {@link Result#INTERNAL_ERROR}.
	 * @param capacity The capacity, a positive integer ranging from 1 to 1024.
	 * @throws GameSDKException if anything goes wrong on the native side
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#lobbytransactionsetcapacity">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#lobbytransactionsetcapacity</a>
	 */
	public void setCapacity(int capacity)
	{
		Result result = setCapacity(pointer, capacity);
		if(result != Result.OK)
		{
			throw new GameSDKException(result);
		}
	}

	/**
	 * Sets a metadata for the created/updated Lobby.
	 * This can either set a new value of the key or replace the existing one.
	 * <p>
	 * Metadata can be used for filtering and sorting in a {@link LobbySearchQuery}
	 * and to provide additional information.
	 * @param key A metadata key, max. 255 bytes
	 * @param value A metadata value, max. 4095 bytes
	 * @throws IllegalArgumentException if key or value are too long
	 * @throws GameSDKException if anything goes wrong on the native side
	 * @see #deleteMetadata(String)
	 * @see LobbyManager#getLobbyMetadata(Lobby)
	 * @see LobbySearchQuery#filter(String, LobbySearchQuery.Comparison, LobbySearchQuery.Cast, String)
	 * @see LobbySearchQuery#sort(String, LobbySearchQuery.Cast, String)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#lobbytransactionsetmetadata">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#lobbytransactionsetmetadata</a>
	 */
	public void setMetadata(String key, String value)
	{
		if(key.getBytes().length >= 256)
			throw new IllegalArgumentException("max key length is 255");
		if(value.getBytes().length >= 4096)
			throw new IllegalArgumentException("max value length is 4095");

		Result result = setMetadata(pointer, key, value);
		if(result != Result.OK)
		{
			throw new GameSDKException(result);
		}
	}

	/**
	 * Sets an existing metadata entry to be deleted.
	 * @param key Key of the metadata entry to delete, max. 255 bytes
	 * @throws IllegalArgumentException if the key is too long
	 * @throws GameSDKException if anything goes wrong on the native side
	 * @see #setMetadata(String, String)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#lobbytransactiondeletemetadata">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#lobbytransactiondeletemetadata</a>
	 */
	public void deleteMetadata(String key)
	{
		if(key.getBytes().length >= 256)
			throw new IllegalArgumentException("max key length is 255");

		Result result = deleteMetadata(pointer, key);
		if(result != Result.OK)
		{
			throw new GameSDKException(result);
		}
	}

	/**
	 * Sets whether the Lobby created/updated by this transaction is locked,
	 * so no new players can join it.
	 * <p>
	 * Attempting to join a locked Lobby will result in a {@link Result#LOBBY_FULL}.
	 * <p>
	 * For some reason a locked Lobby cannot be found with {@link LobbyManager#search(LobbySearchQuery, Consumer)}.
	 * Therefore it acts like a "stronger" version of {@link LobbyType#PRIVATE}
	 * as it cannot be connected to using ID and secret.
	 * @param locked {@code true} if the Lobby is locked
	 * @throws GameSDKException if anything goes wrong on the native side
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#lobbytransactionsetlocked">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#lobbytransactionsetlocked</a>
	 */
	public void setLocked(boolean locked)
	{
		Result result = setLocked(pointer, locked);
		if(result != Result.OK)
		{
			throw new GameSDKException(result);
		}
	}

	private native Result setType(long pointer, int type);
	private native Result setOwner(long pointer, long ownerId);
	private native Result setCapacity(long pointer, int capacity);
	private native Result setMetadata(long pointer, String key, String value);
	private native Result deleteMetadata(long pointer, String key);
	private native Result setLocked(long pointer, boolean locked);
}
