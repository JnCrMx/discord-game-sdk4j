package de.jcm.discordgamesdk.lobby;

import de.jcm.discordgamesdk.GameSDKException;
import de.jcm.discordgamesdk.LobbyManager;
import de.jcm.discordgamesdk.Result;

/**
 * A transaction used to update metadata for a Member of an existing Lobby.
 * <p>
 * An instance of this can only be obtained by {@link LobbyManager#getMemberUpdateTransaction(Lobby, long)}.
 * <p>
 * The instance should be used to do <i>only one</i> update an should be discarded after that.
 * <p>
 * Closing or freeing the instance does not seem to be required or even possible.
 * @see LobbyManager#getMemberUpdateTransaction(Lobby, long)
 * @see LobbyManager#updateMember(Lobby, long, LobbyMemberTransaction)
 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#data-models-lobbymembertransaction-struct">
 *     https://discord.com/developers/docs/game-sdk/lobbies#data-models-lobbymembertransaction-struct</a>
 */
public class LobbyMemberTransaction
{
	private final long pointer;

	LobbyMemberTransaction(long pointer)
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
	 * Sets a metadata entry to be created or if it already exists updated.
	 * <p>
	 * Member metadata can be used to provide additional information
	 * (e.g. in-game username, points, level) or important networking
	 * parameters (peer ID and route).
	 * When using Lobby networking ({@link LobbyManager#connectNetwork(Lobby)})
	 * the member metadata is used automatically to share such parameters.
	 * @param key A metadata key, max. 255 bytes
	 * @param value A metadata value, max. 4095 bytes
	 * @throws IllegalArgumentException if key or value are too long
	 * @throws GameSDKException if anything goes wrong on the native side
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#lobbymembertransactionsetmetadata">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#lobbymembertransactionsetmetadata</a>
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
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#lobbymembertransactiondeletemetadata">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#lobbymembertransactiondeletemetadata</a>
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

	private native Result setMetadata(long pointer, String key, String value);
	private native Result deleteMetadata(long pointer, String key);
}
