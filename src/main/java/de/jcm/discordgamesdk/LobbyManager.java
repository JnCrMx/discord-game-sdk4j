package de.jcm.discordgamesdk;

import de.jcm.discordgamesdk.activity.ActivitySecrets;
import de.jcm.discordgamesdk.lobby.Lobby;
import de.jcm.discordgamesdk.lobby.LobbyMemberTransaction;
import de.jcm.discordgamesdk.lobby.LobbySearchQuery;
import de.jcm.discordgamesdk.lobby.LobbyTransaction;
import de.jcm.discordgamesdk.lobby.LobbyType;
import de.jcm.discordgamesdk.user.DiscordUser;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Manager to create and manage Discord Lobbies.
 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies">
 *     https://discord.com/developers/docs/game-sdk/lobbies</a>
 */
public class LobbyManager
{
	private final long pointer;

	LobbyManager(long pointer)
	{
		this.pointer = pointer;
	}

	/**
	 * Gets a {@link LobbyTransaction} that can be used to
	 * create a new Discord Lobby.
	 * <p>
	 * Together with {@link #getLobbyUpdateTransaction(Lobby)} this
	 * is the <b>only</b> way to obtain an instance of a {@link LobbyTransaction}.
	 * Do <b>not</b> attempt to create it in any other way.
	 * @return A {@link LobbyTransaction} that can be used to create a new Lobby.
	 * @see #createLobby(LobbyTransaction, BiConsumer)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#getlobbycreatetransaction">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#getlobbycreatetransaction</a>
	 */
	public LobbyTransaction getLobbyCreateTransaction()
	{
		Object ret = getLobbyCreateTransaction(pointer);
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			return (LobbyTransaction) ret;
		}
	}

	/**
	 * Gets a {@link LobbyTransaction} that can be used to
	 * update an existing Discord Lobby.
	 * <p>
	 * Together with {@link #getLobbyCreateTransaction()} this
	 * is the <b>only</b> way to obtain an instance of a {@link LobbyTransaction}.
	 * Do <b>not</b> attempt to create it in any other way.
	 * @param lobbyId ID of the Lobby that the created transaction is supposed to update
	 * @return A {@link LobbyTransaction} that can be used to update the specified Lobby.
	 * @see #getLobbyUpdateTransaction(Lobby)
	 * @see #updateLobby(long, LobbyTransaction) 
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#getlobbyupdatetransaction">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#getlobbyupdatetransaction</a>
	 */
	public LobbyTransaction getLobbyUpdateTransaction(long lobbyId)
	{
		Object ret = getLobbyUpdateTransaction(pointer, lobbyId);
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			return (LobbyTransaction) ret;
		}
	}

	/**
	 * Gets a {@link LobbyTransaction} that can be used to update an existing Discord Lobby.
	 * <p>
	 * Together with {@link #getLobbyCreateTransaction()} this
	 * is the <b>only</b> way to obtain an instance of a {@link LobbyTransaction}.
	 * Do <b>not</b> attempt to create it in any other way.
	 * <p>
	 * This method simply obtains the ID of the given Lobby with {@link Lobby#getId()}.
	 * @param lobby Lobby that the created transaction is supposed to update
	 * @return A {@link LobbyTransaction} that can be used to update the specified Lobby.
	 * @see #getLobbyUpdateTransaction(long)
	 * @see #updateLobby(Lobby, LobbyTransaction) 
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#getlobbyupdatetransaction">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#getlobbyupdatetransaction</a>
	 */
	public LobbyTransaction getLobbyUpdateTransaction(Lobby lobby)
	{
		return getLobbyUpdateTransaction(lobby.getId());
	}

	/**
	 * Gets a {@link LobbyMemberTransaction} that can be used to update metadata for a member of a Lobby.
	 * <p>
	 * This is the <b>only</b> way to obtain an instance of a {@link LobbyMemberTransaction}.
	 * Do <b>not</b> attempt to create it in any other way.
	 * @param lobbyId ID of the Lobby to which the member is currently connected
	 * @param userId User ID of the member that should be updated
	 * @return A {@link LobbyMemberTransaction} that can be used to update the specified member in the specified Lobby.
	 * @see #getMemberUpdateTransaction(Lobby, long)
	 * @see #updateMember(long, long, LobbyMemberTransaction) 
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#getmemberupdatetransaction">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#getmemberupdatetransaction</a>
	 */
	public LobbyMemberTransaction getMemberUpdateTransaction(long lobbyId, long userId)
	{
		Object ret = getMemberUpdateTransaction(pointer, lobbyId, userId);
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			return (LobbyMemberTransaction) ret;
		}
	}

	/**
	 * Gets a {@link LobbyMemberTransaction} that can be used to update metadata for a member of a Lobby.
	 * <p>
	 * This is the <b>only</b> way to obtain an instance of a {@link LobbyMemberTransaction}.
	 * Do <b>not</b> attempt to create it in any other way.
	 * <p>
	 * This method simply obtains the ID of the given Lobby with {@link Lobby#getId()}.
	 * @param lobby Lobby to which the member is currently connected
	 * @param userId User ID of the member that should be updated
	 * @return A {@link LobbyMemberTransaction} that can be used to update the specified member in the specified Lobby.
	 * @see #getMemberUpdateTransaction(long, long) 
	 * @see #updateMember(Lobby, long, LobbyMemberTransaction) 
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#getmemberupdatetransaction">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#getmemberupdatetransaction</a>
	 */
	public LobbyMemberTransaction getMemberUpdateTransaction(Lobby lobby, long userId)
	{
		return getMemberUpdateTransaction(lobby.getId(), userId);
	}

	/**
	 * Creates a new Discord Lobby based on a {@link LobbyTransaction}.
	 * The current user gets automatically connected to it.
	 * <p>
	 * {@link LobbyTransaction#setOwner(long)} must <b>not</b> be set;
	 * it is only allowed when updating a Lobby.
	 * @param transaction A {@link LobbyTransaction} that specifies the properties of the Lobby
	 * @param callback A callback to return the {@link Result} and created {@link Lobby} to
	 * @see #createLobby(LobbyTransaction, Consumer)    
	 * @see #getLobbyCreateTransaction()
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#createlobby">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#createlobby</a>
	 */
	public void createLobby(LobbyTransaction transaction, @NotNull BiConsumer<Result, Lobby> callback)
	{
		createLobby(pointer, transaction.getPointer(), callback);
	}

	/**
	 * Creates a new Discord Lobby based on a {@link LobbyTransaction}.
	 * The current user gets automatically connected to it.
	 * <p>
	 * {@link LobbyTransaction#setOwner(long)} must <b>not</b> be set;
	 * it is only allowed when updating a Lobby.
	 * <p>
	 * When completed the {@link Result} is checked using the {@link Core#DEFAULT_CALLBACK} and
	 * if it completes normally (which is the case if result is {@link Result#OK})
	 * the created Lobby is passed to the provided callback.
	 * @param transaction A {@link LobbyTransaction} that specifies the properties of the Lobby
	 * @param callback A callback to return the created {@link Lobby} to
	 * @see #createLobby(LobbyTransaction, BiConsumer)
	 * @see #getLobbyCreateTransaction()
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#createlobby">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#createlobby</a>
	 */
	public void createLobby(LobbyTransaction transaction, @NotNull Consumer<Lobby> callback)
	{
		createLobby(transaction, (result, lobby) ->
		{
			Core.DEFAULT_CALLBACK.accept(result);
			callback.accept(lobby);
		});
	}

	/**
	 * Updates an existing Discord Lobby according to a {@link LobbyTransaction}.
	 * A {@link DiscordEventAdapter#onLobbyUpdate(long)} will be fired for all members of the Lobby.
	 * <p>
	 * This function is rate-limited to 10 updates every 5 seconds.
	 * Use the transaction for batching updates in order to avoid hitting the limit.
	 * <p>
	 * Other than for a creation, {@link LobbyTransaction#setOwner(long)} may be set for an update.
	 * @param lobbyId ID of the Lobby to update
	 * @param transaction Transaction specifying what should be updated
	 * @param callback Callback to process the returned {@link Result}
	 * @see #updateLobby(long, LobbyTransaction)
	 * @see #getLobbyUpdateTransaction(long)
	 * @see DiscordEventAdapter#onLobbyUpdate(long)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#updatelobby">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#updatelobby</a>
	 */
	public void updateLobby(long lobbyId, LobbyTransaction transaction, @NotNull Consumer<Result> callback)
	{
		updateLobby(pointer, lobbyId, transaction.getPointer(), callback);
	}

	/**
	 * Updates an existing Discord Lobby according to a {@link LobbyTransaction}.
	 * A {@link DiscordEventAdapter#onLobbyUpdate(long)} will be fired for all members of the Lobby.
	 * <p>
	 * This function is rate-limited to 10 updates every 5 seconds.
	 * Use the transaction for batching updates in order to avoid hitting the limit.
	 * <p>
	 * Other than for a creation, {@link LobbyTransaction#setOwner(long)} may be set for an update.
	 * <p>
	 * The {@link Core#DEFAULT_CALLBACK} is used to handle the returned {@link Result}.
	 * @param lobbyId ID of the Lobby to update
	 * @param transaction Transaction specifying what should be updated
	 * @see #updateLobby(long, LobbyTransaction, Consumer)
	 * @see #getLobbyUpdateTransaction(long)
	 * @see DiscordEventAdapter#onLobbyUpdate(long)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#updatelobby">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#updatelobby</a>
	 */
	public void updateLobby(long lobbyId, LobbyTransaction transaction)
	{
		updateLobby(lobbyId, transaction, Core.DEFAULT_CALLBACK);
	}

	/**
	 * Updates an existing Discord Lobby according to a {@link LobbyTransaction}.
	 * A {@link DiscordEventAdapter#onLobbyUpdate(long)} will be fired for all members of the Lobby.
	 * <p>
	 * This function is rate-limited to 10 updates every 5 seconds.
	 * Use the transaction for batching updates in order to avoid hitting the limit.
	 * <p>
	 * Other than for a creation, {@link LobbyTransaction#setOwner(long)} may be set for an update.
	 * <p>
	 * This method simply obtains the ID of the given Lobby with {@link Lobby#getId()}.
	 * @param lobby The Lobby to update
	 * @param transaction Transaction specifying what should be updated
	 * @param callback Callback to process the returned {@link Result}
	 * @see #updateLobby(Lobby, LobbyTransaction)
	 * @see #getLobbyUpdateTransaction(Lobby)
	 * @see DiscordEventAdapter#onLobbyUpdate(long)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#updatelobby">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#updatelobby</a>
	 */
	public void updateLobby(Lobby lobby, LobbyTransaction transaction, @NotNull Consumer<Result> callback)
	{
		updateLobby(lobby.getId(), transaction, callback);
	}

	/**
	 * Updates an existing Discord Lobby according to a {@link LobbyTransaction}.
	 * A {@link DiscordEventAdapter#onLobbyUpdate(long)} will be fired for all members of the Lobby.
	 * <p>
	 * This function is rate-limited to 10 updates every 5 seconds.
	 * Use the transaction for batching updates in order to avoid hitting the limit.
	 * <p>
	 * Other than for a creation, {@link LobbyTransaction#setOwner(long)} may be set for an update.
	 * <p>
	 * The {@link Core#DEFAULT_CALLBACK} is used to handle the returned {@link Result}.
	 * <p>
	 * This method simply obtains the ID of the given Lobby with {@link Lobby#getId()}.
	 * @param lobby The Lobby to update
	 * @param transaction Transaction specifying what should be updated
	 * @see #updateLobby(Lobby, LobbyTransaction, Consumer)
	 * @see #getLobbyUpdateTransaction(Lobby)
	 * @see DiscordEventAdapter#onLobbyUpdate(long)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#updatelobby">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#updatelobby</a>
	 */
	public void updateLobby(Lobby lobby, LobbyTransaction transaction)
	{
		updateLobby(lobby, transaction, Core.DEFAULT_CALLBACK);
	}

	/**
	 * Deletes an existing Lobby.
	 * All members will automatically get disconnected from the Lobby.
	 * A {@link DiscordEventAdapter#onLobbyDelete(long, int)} will be fired for all members of the Lobby.
	 * @param lobbyId ID of the lobby to delete
	 * @param callback Callback to process the returned {@link Result}
	 * @see #deleteLobby(long)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#deletelobby">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#deletelobby</a>
	 */
	public void deleteLobby(long lobbyId, @NotNull Consumer<Result> callback)
	{
		deleteLobby(pointer, lobbyId, callback);
	}

	/**
	 * Deletes an existing Lobby.
	 * All members will automatically get disconnected from the Lobby.
	 * A {@link DiscordEventAdapter#onLobbyDelete(long, int)} will be fired for all members of the Lobby.
	 * <p>
	 * The {@link Core#DEFAULT_CALLBACK} is used to handle the returned {@link Result}.
	 * @param lobbyId ID of the lobby to delete
	 * @see #deleteLobby(long, Consumer)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#deletelobby">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#deletelobby</a>
	 */
	public void deleteLobby(long lobbyId)
	{
		deleteLobby(pointer, lobbyId, Core.DEFAULT_CALLBACK);
	}

	/**
	 * Deletes an existing Lobby.
	 * All members will automatically get disconnected from the Lobby.
	 * A {@link DiscordEventAdapter#onLobbyDelete(long, int)} will be fired for all members of the Lobby.
	 * <p>
	 * This method simply obtains the ID of the given Lobby with {@link Lobby#getId()}.
	 * @param lobby The lobby to delete
	 * @param callback Callback to process the returned {@link Result}
	 * @see #deleteLobby(Lobby)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#deletelobby">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#deletelobby</a>
	 */
	public void deleteLobby(Lobby lobby, @NotNull Consumer<Result> callback)
	{
		deleteLobby(lobby.getId(), callback);
	}

	/**
	 * Deletes an existing Lobby.
	 * All members will automatically get disconnected from the Lobby.
	 * A {@link DiscordEventAdapter#onLobbyDelete(long, int)} will be fired for all members of the Lobby.
	 * <p>
	 * The {@link Core#DEFAULT_CALLBACK} is used to handle the returned {@link Result}.
	 * <p>
	 * This method simply obtains the ID of the given Lobby with {@link Lobby#getId()}.
	 * @param lobby The lobby to delete
	 * @see #deleteLobby(Lobby, Consumer)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#deletelobby">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#deletelobby</a>
	 */
	public void deleteLobby(Lobby lobby)
	{
		deleteLobby(lobby, Core.DEFAULT_CALLBACK);
	}

	/**
	 * Connects the current user to a Lobby by its ID and secret.
	 * If the connection succeeds the Lobby is fetched and returned.
	 * <p>
	 * The user can be connected to up to 5 different Lobbies at the same time.
	 * A Lobby cannot be connected to (= joined) if it is full,
	 * locked or the user is already connected to 5 Lobbies.
	 * <p>
	 * Both can be obtained with {@link LobbyManager#search(LobbySearchQuery)} for {@link LobbyType#PUBLIC} Lobbies.
	 * For {@link LobbyType#PRIVATE} Lobbies you need to obtain them on an different way (e.g. user input).
	 * <p>
	 * For connecting with an Activity secret (as obtained by {@link #getLobbyActivitySecret(Lobby)})
	 * consider using {@link #connectLobbyWithActivitySecret(String, BiConsumer)} instead of parsing it manually.
	 * @param lobbyId The ID of the Lobby you want to connect to
	 * @param secret The secret of the Lobby, max. 127 bytes
	 * @param callback Callback to return the {@link Result} and fetched {@link Lobby} object to
	 * @throws IllegalArgumentException if the secret is too long
	 * @see #connectLobby(long, String, Consumer)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#connectlobby">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#connectlobby</a>
	 */
	public void connectLobby(long lobbyId, String secret, @NotNull BiConsumer<Result, Lobby> callback)
	{
		if(secret.getBytes().length >= 128)
			throw new IllegalArgumentException("max secret length is 127");
		connectLobby(pointer, lobbyId, secret, callback);
	}

	/**
	 * Connects the current user to a Lobby by its ID and secret.
	 * If the connection succeeds the Lobby is fetched and returned.
	 * <p>
	 * The user can be connected to up to 5 different Lobbies at the same time.
	 * A Lobby cannot be connected to (= joined) if it is full,
	 * locked or the user is already connected to 5 Lobbies.
	 * <p>
	 * Both can be obtained with {@link LobbyManager#search(LobbySearchQuery)} for {@link LobbyType#PUBLIC} Lobbies.
	 * For {@link LobbyType#PRIVATE} Lobbies you need to obtain them on an different way (e.g. user input).
	 * <p>
	 * For connecting with an Activity secret (as obtained by {@link #getLobbyActivitySecret(Lobby)})
	 * consider using {@link #connectLobbyWithActivitySecret(String, BiConsumer)} instead of parsing it manually.
	 * <p>
	 * When completed the {@link Result} is checked using the {@link Core#DEFAULT_CALLBACK} and
	 * if it completes normally (which is the case if result is {@link Result#OK})
	 * the joined Lobby is passed to the provided callback.
	 * @param lobbyId The ID of the Lobby you want to connect to
	 * @param secret The secret of the Lobby, max. 127 bytes
	 * @param callback Callback to return the fetched {@link Lobby} object to
	 * @throws IllegalArgumentException if the secret is too long
	 * @see #connectLobby(long, String, BiConsumer)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#connectlobby">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#connectlobby</a>
	 */
	public void connectLobby(long lobbyId, String secret, @NotNull Consumer<Lobby> callback)
	{
		connectLobby(lobbyId, secret, (result, lobby) ->
		{
			Core.DEFAULT_CALLBACK.accept(result);
			callback.accept(lobby);
		});
	}

	/**
	 * Connects the current user to a Lobby by its ID and secret.
	 * If the connection succeeds the Lobby is fetched and returned.
	 * <p>
	 * The user can be connected to up to 5 different Lobbies at the same time.
	 * A Lobby cannot be connected to (= joined) if it is full,
	 * locked or the user is already connected to 5 Lobbies.
	 * <p>
	 * Both can be obtained with {@link LobbyManager#search(LobbySearchQuery)} for {@link LobbyType#PUBLIC} Lobbies.
	 * <p>
	 * For connecting with an Activity secret (as obtained by {@link #getLobbyActivitySecret(Lobby)})
	 * consider using {@link #connectLobbyWithActivitySecret(String, BiConsumer)} instead of parsing it manually.
	 * <p>
	 * The Lobby ID and the secret are simply obtained by {@link Lobby#getId()} and {@link Lobby#getSecret()}.
	 * @param lobby Lobby to connect to
	 * @param callback Callback to return the fetched {@link Lobby} object to
	 * @throws IllegalArgumentException if the secret is too long
	 * @see #connectLobby(long, String, BiConsumer)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#connectlobby">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#connectlobby</a>
	 */
	public void connectLobby(Lobby lobby, @NotNull BiConsumer<Result, Lobby> callback)
	{
		connectLobby(lobby.getId(), lobby.getSecret(), callback);
	}

	/**
	 * Connects the current user to a Lobby by its Activity secret.
	 * If the connection succeeds the Lobby is fetched and returned.
	 * <p>
	 * The user can be connected to up to 5 different Lobbies at the same time.
	 * A Lobby cannot be connected to (= joined) if it is full,
	 * locked or the user is already connected to 5 Lobbies.
	 * <p>
	 * An Activity secret is a concatenation of the Lobby ID and secret (separated with ':').
	 * It can be obtained of an existing Lobby by {@link #getLobbyActivitySecret(Lobby)}.
	 * The parsing of the Activity secret is done internally by Discord,
	 * so a change of its structure will not impact this function.
	 * Therefore it is recommended to prefer this function over manually parsing
	 * the Activity secret.
	 * For connecting with Lobby ID and secret use {@link #connectLobby(long, String, BiConsumer)}.
	 * Do <b>not</b> concat them manually into an Activity secret!
	 * @param activitySecret The Activity secret for the Lobby you want to connect to, max. 127 bytes
	 * @param callback Callback to return the result and fetched {@link Lobby} object to
	 * @throws IllegalArgumentException if the Activity secret is too long
	 * @see #connectLobbyWithActivitySecret(String, Consumer)
	 * @see #getLobbyActivitySecret(Lobby)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#connectlobbywithactivitysecret">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#connectlobbywithactivitysecret</a>
	 */
	public void connectLobbyWithActivitySecret(String activitySecret, @NotNull BiConsumer<Result, Lobby> callback)
	{
		if(activitySecret.getBytes().length >= 128)
			throw new IllegalArgumentException("max activity secret length is 127");
		connectLobbyWithActivitySecret(pointer, activitySecret, callback);
	}

	/**
	 * Connects the current user to a Lobby by its Activity secret.
	 * If the connection succeeds the Lobby is fetched and returned.
	 * <p>
	 * The user can be connected to up to 5 different Lobbies at the same time.
	 * A Lobby cannot be connected to (= joined) if it is full,
	 * locked or the user is already connected to 5 Lobbies.
	 * <p>
	 * An Activity secret is a concatenation of the Lobby ID and secret (separated with ':').
	 * It can be obtained of an existing Lobby by {@link #getLobbyActivitySecret(Lobby)}.
	 * The parsing of the Activity secret is done internally by Discord,
	 * so a change of its structure will not impact this function.
	 * Therefore it is recommended to prefer this function over manually parsing
	 * the Activity secret.
	 * For connecting with Lobby ID and secret use {@link #connectLobby(long, String, BiConsumer)}.
	 * Do <b>not</b> concat them manually into an Activity secret!
	 * <p>
	 * When completed the {@link Result} is checked using the {@link Core#DEFAULT_CALLBACK} and
	 * if it completes normally (which is the case if result is {@link Result#OK})
	 * the joined Lobby is passed to the provided callback.
	 * @param activitySecret The Activity secret for the Lobby you want to connect to, max. 127 bytes
	 * @param callback Callback to return the fetched {@link Lobby} object to
	 * @throws IllegalArgumentException if the Activity secret is too long
	 * @see #connectLobbyWithActivitySecret(String, Consumer)
	 * @see #getLobbyActivitySecret(Lobby)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#connectlobbywithactivitysecret">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#connectlobbywithactivitysecret</a>
	 */
	public void connectLobbyWithActivitySecret(String activitySecret, @NotNull Consumer<Lobby> callback)
	{
		connectLobbyWithActivitySecret(pointer, activitySecret, (result, lobby) ->
		{
			Core.DEFAULT_CALLBACK.accept(result);
			callback.accept(lobby);
		});
	}

	/**
	 * Disconnects the current user from a Lobby.
	 * @param lobbyId ID of the Lobby to disconnect from.
	 * @param callback Callback to process the returned {@link Result}
	 * @see #disconnectLobby(long)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#disconnectlobby">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#disconnectlobby</a>
	 */
	public void disconnectLobby(long lobbyId, Consumer<Result> callback)
	{
		disconnectLobby(pointer, lobbyId, callback);
	}

	/**
	 * Disconnects the current user from a Lobby.
	 * <p>
	 * The {@link Core#DEFAULT_CALLBACK} is used to handle the returned {@link Result}.
	 * @param lobbyId ID of the Lobby to disconnect from.
	 * @see #disconnectLobby(long, Consumer)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#disconnectlobby">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#disconnectlobby</a>
	 */
	public void disconnectLobby(long lobbyId)
	{
		disconnectLobby(lobbyId, Core.DEFAULT_CALLBACK);
	}

	/**
	 * Disconnects the current user from a Lobby.
	 * <p>
	 * This method simply obtains the ID of the given Lobby with {@link Lobby#getId()}.
	 * @param lobby The Lobby to disconnect from.
	 * @param callback Callback to process the returned {@link Result}
	 * @see #disconnectLobby(Lobby)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#disconnectlobby">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#disconnectlobby</a>
	 */
	public void disconnectLobby(Lobby lobby, Consumer<Result> callback)
	{
		disconnectLobby(lobby.getId(), callback);
	}

	/**
	 * Disconnects the current user from a Lobby.
	 * <p>
	 * The {@link Core#DEFAULT_CALLBACK} is used to handle the returned {@link Result}.
	 * <p>
	 * This method simply obtains the ID of the given Lobby with {@link Lobby#getId()}.
	 * @param lobby The Lobby to disconnect from.
	 * @see #disconnectLobby(Lobby, Consumer)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#disconnectlobby">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#disconnectlobby</a>
	 */
	public void disconnectLobby(Lobby lobby)
	{
		disconnectLobby(lobby, Core.DEFAULT_CALLBACK);
	}

	/**
	 * Gets the Lobby object for a Lobby with the specified ID.
	 * <p>
	 * The Lobby must be "fetched" before; either as part of a {@link #search(LobbySearchQuery)} or
	 * because the current user is connected to it.
	 * @param lobbyId ID of the requested Lobby
	 * @return A Lobby object for the given ID
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#getlobby">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#getlobby</a>
	 */
	public Lobby getLobby(long lobbyId)
	{
		Object ret = getLobby(pointer, lobbyId);
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			return (Lobby) ret;
		}
	}

	/**
	 * Gets the Activity secret for a Lobby.
	 * <p>
	 * An Activity secret is a concatenation of the ID and secret of the Lobby,
	 * separated by '{@code :}'.
	 * <p>
	 * <i>Warning: This format is handled internally by Discord and
	 * is therefore subject to sudden and unexpected changes.
	 * Do <b>not</b> rely on the format of the Activity secret.</i>
	 * <p>
	 * The Activity secret is supposed to be used for Rich Presence Invites and
	 * can be set for an Activity using {@link ActivitySecrets#setJoinSecret(String)}.
	 * The joining client will receive the Activity secret in
	 * {@link DiscordEventAdapter#onActivityJoin(String)} and should
	 * use it with {@link #connectLobbyWithActivitySecret(String, BiConsumer)}
	 * to connect to the Lobby.
	 * @param lobbyId ID of the Lobby to get the Activity secret for
	 * @return The Activity secret for this Lobby, max. 127 bytes
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #getLobbyActivitySecret(Lobby)
	 * @see ActivitySecrets#setJoinSecret(String)
	 * @see #connectLobbyWithActivitySecret(String, BiConsumer)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#getlobbyactivitysecret">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#getlobbyactivitysecret</a>
	 */
	public String getLobbyActivitySecret(long lobbyId)
	{
		Object ret = getLobbyActivitySecret(pointer, lobbyId);
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			return (String) ret;
		}
	}

	/**
	 * Gets the Activity secret for a Lobby.
	 * <p>
	 * An Activity secret is a concatenation of the ID and secret of the Lobby,
	 * separated by '{@code :}'.
	 * <p>
	 * <i>Warning: This format is handled internally by Discord and
	 * is therefore subject to sudden and unexpected changes.
	 * Do <b>not</b> rely on the format of the Activity secret.</i>
	 * <p>
	 * The Activity secret is supposed to be used for Rich Presence Invites and
	 * can be set for an Activity using {@link ActivitySecrets#setJoinSecret(String)}.
	 * The joining client will receive the Activity secret in
	 * {@link DiscordEventAdapter#onActivityJoin(String)} and should
	 * use it with {@link #connectLobbyWithActivitySecret(String, BiConsumer)}
	 * to connect to the Lobby.
	 * <p>
	 * This method simply obtains the ID of the given Lobby with {@link Lobby#getId()}.
	 * @param lobby The Lobby to get the Activity secret for
	 * @return The Activity secret for this Lobby, max. 127 bytes
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #getLobbyActivitySecret(long) 
	 * @see ActivitySecrets#setJoinSecret(String)
	 * @see #connectLobbyWithActivitySecret(String, BiConsumer)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#getlobbyactivitysecret">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#getlobbyactivitysecret</a>
	 */
	public String getLobbyActivitySecret(Lobby lobby)
	{
		return getLobbyActivitySecret(lobby.getId());
	}

	public String getLobbyMetadataValue(long lobbyId, String key)
	{
		if(key.getBytes().length >= 256)
			throw new IllegalArgumentException("max key length is 255");
		Object ret = getLobbyMetadataValue(pointer, lobbyId, key);
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			return (String) ret;
		}
	}
	public String getLobbyMetadataValue(Lobby lobby, String key)
	{
		return getLobbyMetadataValue(lobby.getId(), key);
	}

	public String getLobbyMetadataKey(long lobbyId, int index)
	{
		Object ret = getLobbyMetadataKey(pointer, lobbyId, index);
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			return (String) ret;
		}
	}
	public String getLobbyMetadataKey(Lobby lobby, int index)
	{
		return getLobbyMetadataKey(lobby.getId(), index);
	}

	public int lobbyMetadataCount(long lobbyId)
	{
		Object ret = lobbyMetadataCount(pointer, lobbyId);
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			return (Integer) ret;
		}
	}
	public int lobbyMetadataCount(Lobby lobby)
	{
		return lobbyMetadataCount(lobby.getId());
	}

	public Map<String, String> getLobbyMetadata(long lobbyId)
	{
		int count = lobbyMetadataCount(lobbyId);
		HashMap<String, String> map = new HashMap<>(count);
		for(int i=0; i<count; i++)
		{
			String key = getLobbyMetadataKey(lobbyId, i);
			String value = getLobbyMetadataValue(lobbyId, key);
			map.put(key, value);
		}
		return Collections.unmodifiableMap(map);
	}
	public Map<String, String> getLobbyMetadata(Lobby lobby)
	{
		return getLobbyMetadata(lobby.getId());
	}

	public int memberCount(long lobbyId)
	{
		Object ret = memberCount(pointer, lobbyId);
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			return (Integer) ret;
		}
	}
	public int memberCount(Lobby lobby)
	{
		return memberCount(lobby.getId());
	}

	public long getMemberUserId(long lobbyId, int index)
	{
		Object ret = getMemberUserId(pointer, lobbyId, index);
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			return (Long) ret;
		}
	}
	public long getMemberUserId(Lobby lobby, int index)
	{
		return getMemberUserId(lobby.getId(), index);
	}

	public List<Long> getMemberUserIds(long lobbyId)
	{
		List<Long> list = IntStream.range(0, memberCount(lobbyId))
				.mapToLong(i->getMemberUserId(lobbyId, i))
				.boxed()
				.collect(Collectors.toList());
		return Collections.unmodifiableList(list);
	}
	public List<Long> getMemberUserIds(Lobby lobby)
	{
		return getMemberUserIds(lobby.getId());
	}

	public DiscordUser getMemberUser(long lobbyId, long userId)
	{
		Object ret = getMemberUser(pointer, lobbyId, userId);
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			return (DiscordUser) ret;
		}
	}
	public DiscordUser getMemberUser(Lobby lobby, long userId)
	{
		return getMemberUser(lobby.getId(), userId);
	}

	public List<DiscordUser> getMemberUsers(long lobbyId)
	{
		List<DiscordUser> list = getMemberUserIds(lobbyId).stream()
				.map(l->getMemberUser(lobbyId, l))
				.collect(Collectors.toList());
		return Collections.unmodifiableList(list);
	}
	public List<DiscordUser> getMemberUsers(Lobby lobby)
	{
		return getMemberUsers(lobby.getId());
	}

	public String getMemberMetadataValue(long lobbyId, long userId, String key)
	{
		if(key.getBytes().length >= 256)
			throw new IllegalArgumentException("max key length is 255");
		Object ret = getMemberMetadataValue(pointer, lobbyId, userId, key);
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			return (String) ret;
		}
	}
	public String getMemberMetadataValue(Lobby lobby, long userId, String key)
	{
		return getMemberMetadataValue(lobby.getId(), userId, key);
	}

	public String getMemberMetadataKey(long lobbyId, long userId, int index)
	{
		Object ret = getMemberMetadataKey(pointer, lobbyId, userId, index);
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			return (String) ret;
		}
	}
	public String getMemberMetadataKey(Lobby lobby, long userId, int index)
	{
		return getMemberMetadataKey(lobby.getId(), userId, index);
	}

	public int memberMetadataCount(long lobbyId, long userId)
	{
		Object ret = memberMetadataCount(pointer, lobbyId, userId);
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			return (Integer) ret;
		}
	}
	public int memberMetadataCount(Lobby lobby, long userId)
	{
		return memberMetadataCount(lobby.getId(), userId);
	}

	public Map<String, String> getMemberMetadata(long lobbyId, long userId)
	{
		int count = memberMetadataCount(lobbyId, userId);
		HashMap<String, String> map = new HashMap<>(count);
		for(int i=0; i<count; i++)
		{
			String key = getMemberMetadataKey(lobbyId, userId, i);
			String value = getMemberMetadataValue(lobbyId, userId, key);
			map.put(key, value);
		}
		return Collections.unmodifiableMap(map);
	}
	public Map<String, String> getMemberMetadata(Lobby lobby, long userId)
	{
		return getMemberMetadata(lobby.getId(), userId);
	}

	public void updateMember(long lobbyId, long userId, LobbyMemberTransaction transaction, Consumer<Result> callback)
	{
		updateMember(pointer, lobbyId, userId, transaction.getPointer(), callback);
	}
	public void updateMember(long lobbyId, long userId, LobbyMemberTransaction transaction)
	{
		updateMember(lobbyId, userId, transaction, Core.DEFAULT_CALLBACK);
	}
	public void updateMember(Lobby lobby, long userId, LobbyMemberTransaction transaction, Consumer<Result> callback)
	{
		updateMember(lobby.getId(), userId, transaction, callback);
	}
	public void updateMember(Lobby lobby, long userId, LobbyMemberTransaction transaction)
	{
		updateMember(lobby, userId, transaction, Core.DEFAULT_CALLBACK);
	}

	public void sendLobbyMessage(long lobbyId, byte[] data, Consumer<Result> callback)
	{
		sendLobbyMessage(pointer, lobbyId, data, 0, data.length, callback);
	}
	public void sendLobbyMessage(long lobbyId, byte[] data)
	{
		sendLobbyMessage(lobbyId, data, Core.DEFAULT_CALLBACK);
	}
	public void sendLobbyMessage(Lobby lobby, byte[] data, Consumer<Result> callback)
	{
		sendLobbyMessage(lobby.getId(), data, callback);
	}
	public void sendLobbyMessage(Lobby lobby, byte[] data)
	{
		sendLobbyMessage(lobby, data, Core.DEFAULT_CALLBACK);
	}

	public LobbySearchQuery getSearchQuery()
	{
		Object ret = getSearchQuery(pointer);
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			return (LobbySearchQuery) ret;
		}
	}

	public void search(LobbySearchQuery query, Consumer<Result> callback)
	{
		search(pointer, query.getPointer(), callback);
	}
	public void search(LobbySearchQuery query)
	{
		search(query, Core.DEFAULT_CALLBACK);
	}

	public int lobbyCount()
	{
		return lobbyCount(pointer);
	}

	public long getLobbyId(int index)
	{
		Object ret = getLobbyId(pointer, index);
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			return (long) ret;
		}
	}

	public List<Long> getLobbyIds()
	{
		List<Long> list = IntStream.range(0, lobbyCount())
		                           .mapToLong(this::getLobbyId)
		                           .boxed()
		                           .collect(Collectors.toList());
		return Collections.unmodifiableList(list);
	}

	public List<Lobby> getLobbies()
	{
		List<Lobby> list = getLobbyIds().stream()
		                                .map(this::getLobby)
		                                .collect(Collectors.toList());
		return Collections.unmodifiableList(list);
	}

	public void connectVoice(long lobbyId, @NotNull Consumer<Result> callback)
	{
		connectVoice(pointer, lobbyId, callback);
	}
	public void connectVoice(long lobbyId)
	{
		connectVoice(pointer, lobbyId, Core.DEFAULT_CALLBACK);
	}
	public void connectVoice(Lobby lobby, @NotNull Consumer<Result> callback)
	{
		connectVoice(lobby.getId(), callback);
	}
	public void connectVoice(Lobby lobby)
	{
		connectVoice(lobby, Core.DEFAULT_CALLBACK);
	}

	public void disconnectVoice(long lobbyId, @NotNull Consumer<Result> callback)
	{
		disconnectVoice(pointer, lobbyId, callback);
	}
	public void disconnectVoice(long lobbyId)
	{
		disconnectVoice(pointer, lobbyId, Core.DEFAULT_CALLBACK);
	}
	public void disconnectVoice(Lobby lobby, @NotNull Consumer<Result> callback)
	{
		disconnectVoice(lobby.getId(), callback);
	}
	public void disconnectVoice(Lobby lobby)
	{
		disconnectVoice(lobby, Core.DEFAULT_CALLBACK);
	}

	public void connectNetwork(long lobbyId)
	{
		Result result = connectNetwork(pointer, lobbyId);
		if(result != Result.OK)
			throw new GameSDKException(result);
	}
	public void connectNetwork(Lobby lobby)
	{
		connectNetwork(lobby.getId());
	}

	public void disconnectNetwork(long lobbyId)
	{
		Result result = disconnectNetwork(pointer, lobbyId);
		if(result != Result.OK)
			throw new GameSDKException(result);
	}
	public void disconnectNetwork(Lobby lobby)
	{
		disconnectNetwork(lobby.getId());
	}

	public void flushNetwork()
	{
		Result result = flushNetwork(pointer);
		if(result != Result.OK)
			throw new GameSDKException(result);
	}

	public void openNetworkChannel(long lobbyId, byte channelId, boolean reliable)
	{
		Result result = openNetworkChannel(pointer, lobbyId, channelId, reliable);
		if(result != Result.OK)
			throw new GameSDKException(result);
	}
	public void openNetworkChannel(Lobby lobby, byte channelId, boolean reliable)
	{
		openNetworkChannel(lobby.getId(), channelId, reliable);
	}

	public void sendNetworkMessage(long lobbyId, long userId, byte channelId, byte[] data)
	{
		Result result = sendNetworkMessage(pointer, lobbyId, userId, channelId, data, 0, data.length);
		if(result != Result.OK)
			throw new GameSDKException(result);
	}
	public void sendNetworkMessage(Lobby lobby, long userId, byte channelId, byte[] data)
	{
		sendNetworkMessage(lobby.getId(), userId, channelId, data);
	}

	private native Object getLobbyCreateTransaction(long pointer);
	private native Object getLobbyUpdateTransaction(long pointer, long lobbyId);
	private native Object getMemberUpdateTransaction(long pointer, long lobbyId, long userId);

	private native void createLobby(long pointer, long transactionPointer, BiConsumer<Result, Lobby> callback);
	private native void updateLobby(long pointer, long lobbyId, long transactionPointer, Consumer<Result> callback);
	private native void deleteLobby(long pointer, long lobbyId, Consumer<Result> callback);

	private native void connectLobby(long pointer, long lobbyId, String secret, BiConsumer<Result, Lobby> callback);
	private native void connectLobbyWithActivitySecret(long pointer, String activitySecret, BiConsumer<Result, Lobby> callback);
	private native void disconnectLobby(long pointer, long lobbyId, Consumer<Result> callback);

	private native Object getLobby(long pointer, long lobbyId);
	private native Object getLobbyActivitySecret(long pointer, long lobbyId);
	private native Object getLobbyMetadataValue(long pointer, long lobbyId, String key);
	private native Object getLobbyMetadataKey(long pointer, long lobbyId, int index);
	private native Object lobbyMetadataCount(long pointer, long lobbyId);

	private native Object memberCount(long pointer, long lobbyId);
	private native Object getMemberUserId(long pointer, long lobbyId, int index);
	private native Object getMemberUser(long pointer, long lobbyId, long userId);
	private native Object getMemberMetadataValue(long pointer, long lobbyId, long userId, String key);
	private native Object getMemberMetadataKey(long pointer, long lobbyId, long userId, int index);
	private native Object memberMetadataCount(long pointer, long lobbyId, long userId);
	private native void updateMember(long pointer, long lobbyId, long userId, long transactionPointer, Consumer<Result> callback);

	private native void sendLobbyMessage(long pointer, long lobbyId, byte[] data, int offset, int length, Consumer<Result> callback);

	private native Object getSearchQuery(long pointer);
	private native void search(long pointer, long searchQueryPointer, Consumer<Result> callback);
	private native int lobbyCount(long pointer);
	private native Object getLobbyId(long pointer, int index);

	private native void connectVoice(long pointer, long lobbyId, Consumer<Result> callback);
	private native void disconnectVoice(long pointer, long lobbyId, Consumer<Result> callback);

	private native Result connectNetwork(long pointer, long lobbyId);
	private native Result disconnectNetwork(long pointer, long lobbyId);
	private native Result flushNetwork(long pointer);
	private native Result openNetworkChannel(long pointer, long lobbyId, byte channelId, boolean reliable);
	private native Result sendNetworkMessage(long pointer, long lobbyId, long userId, byte channelId, byte[] data, int offset, int length);
}
