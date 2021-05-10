package de.jcm.discordgamesdk;

import de.jcm.discordgamesdk.activity.ActivitySecrets;
import de.jcm.discordgamesdk.lobby.*;
import de.jcm.discordgamesdk.user.DiscordUser;

import java.util.*;
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
	private final Core core;

	LobbyManager(long pointer, Core core)
	{
		this.pointer = pointer;
		this.core = core;
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
		Object ret = core.execute(()->getLobbyCreateTransaction(pointer));
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
		Object ret = core.execute(()->getLobbyUpdateTransaction(pointer, lobbyId));
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
		Object ret = core.execute(()->getMemberUpdateTransaction(pointer, lobbyId, userId));
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
	public void createLobby(LobbyTransaction transaction, BiConsumer<Result, Lobby> callback)
	{
		core.execute(()->createLobby(pointer, transaction.getPointer(), Objects.requireNonNull(callback)));
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
	public void createLobby(LobbyTransaction transaction, Consumer<Lobby> callback)
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
	public void updateLobby(long lobbyId, LobbyTransaction transaction, Consumer<Result> callback)
	{
		core.execute(()->updateLobby(pointer, lobbyId, transaction.getPointer(), Objects.requireNonNull(callback)));
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
	public void updateLobby(Lobby lobby, LobbyTransaction transaction, Consumer<Result> callback)
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
	public void deleteLobby(long lobbyId, Consumer<Result> callback)
	{
		core.execute(()->deleteLobby(pointer, lobbyId, Objects.requireNonNull(callback)));
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
		deleteLobby(lobbyId, Core.DEFAULT_CALLBACK);
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
	public void deleteLobby(Lobby lobby, Consumer<Result> callback)
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
	 * Both can be obtained with {@link LobbyManager#search(LobbySearchQuery, Consumer)}
	 * for {@link LobbyType#PUBLIC} Lobbies.
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
	public void connectLobby(long lobbyId, String secret, BiConsumer<Result, Lobby> callback)
	{
		if(secret.getBytes().length >= 128)
			throw new IllegalArgumentException("max secret length is 127");
		core.execute(()->connectLobby(pointer, lobbyId, secret, Objects.requireNonNull(callback)));
	}

	/**
	 * Connects the current user to a Lobby by its ID and secret.
	 * If the connection succeeds the Lobby is fetched and returned.
	 * <p>
	 * The user can be connected to up to 5 different Lobbies at the same time.
	 * A Lobby cannot be connected to (= joined) if it is full,
	 * locked or the user is already connected to 5 Lobbies.
	 * <p>
	 * Both can be obtained with {@link LobbyManager#search(LobbySearchQuery, Consumer)}
	 * for {@link LobbyType#PUBLIC} Lobbies.
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
	public void connectLobby(long lobbyId, String secret, Consumer<Lobby> callback)
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
	 * Both can be obtained with {@link LobbyManager#search(LobbySearchQuery, Consumer)}
	 * for {@link LobbyType#PUBLIC} Lobbies.
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
	public void connectLobby(Lobby lobby, BiConsumer<Result, Lobby> callback)
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
	public void connectLobbyWithActivitySecret(String activitySecret, BiConsumer<Result, Lobby> callback)
	{
		if(activitySecret.getBytes().length >= 128)
			throw new IllegalArgumentException("max activity secret length is 127");
		core.execute(()->connectLobbyWithActivitySecret(pointer, activitySecret, Objects.requireNonNull(callback)));
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
	public void connectLobbyWithActivitySecret(String activitySecret, Consumer<Lobby> callback)
	{
		connectLobbyWithActivitySecret(activitySecret, (result, lobby) ->
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
		core.execute(()->disconnectLobby(pointer, lobbyId, Objects.requireNonNull(callback)));
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
	 * The Lobby must be "fetched" before; either as part of a {@link #search(LobbySearchQuery, Consumer)} or
	 * because the current user is connected to it.
	 * @param lobbyId ID of the requested Lobby
	 * @return A Lobby object for the given ID
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#getlobby">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#getlobby</a>
	 */
	public Lobby getLobby(long lobbyId)
	{
		Object ret = core.execute(()->getLobby(pointer, lobbyId));
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
		Object ret = core.execute(()->getLobbyActivitySecret(pointer, lobbyId));
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

	/**
	 * Gets the metadata value associated with a given metadata key for a Lobby.
	 * @param lobbyId ID of the Lobby to get the metadata of
	 * @param key Key of the metadata entry, max. 255 bytes
	 * @return The associated value, max. 4095 bytes
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #getLobbyMetadataValue(Lobby, String)
	 * @see #getLobbyMetadata(long)
	 * @see LobbyTransaction#setMetadata(String, String)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#getlobbymetadatavalue">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#getlobbymetadatavalue</a>
	 */
	public String getLobbyMetadataValue(long lobbyId, String key)
	{
		if(key.getBytes().length >= 256)
			throw new IllegalArgumentException("max key length is 255");
		Object ret = core.execute(()->getLobbyMetadataValue(pointer, lobbyId, key));
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
	 * Gets the metadata value associated with a given metadata key for a Lobby.
	 * <p>
	 * This method simply obtains the ID of the given Lobby with {@link Lobby#getId()}.
	 * @param lobby The Lobby to get the metadata of
	 * @param key Key of the metadata entry, max. 255 bytes
	 * @return The associated value, max. 4095 bytes
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #getLobbyMetadataValue(long, String)
	 * @see #getLobbyMetadata(Lobby)
	 * @see LobbyTransaction#setMetadata(String, String)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#getlobbymetadatavalue">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#getlobbymetadatavalue</a>
	 */
	public String getLobbyMetadataValue(Lobby lobby, String key)
	{
		return getLobbyMetadataValue(lobby.getId(), key);
	}

	/**
	 * Gets the key for a metadata entry at a given index.
	 * <p>
	 * Use {@link #lobbyMetadataCount(long)} to get the numbers of entries available.
	 * @param lobbyId ID of the Lobby to get the metadata of
	 * @param index Index of the key, must be {@literal >=} 0
	 *              and {@literal <} {@link #lobbyMetadataCount(long)}
	 * @return The key, max. 255 bytes
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #getLobbyMetadataKey(Lobby, int)
	 * @see #lobbyMetadataCount(long)
	 * @see #getLobbyMetadata(long)
	 * @see LobbyTransaction#setMetadata(String, String)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#getlobbymetadatakey">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#getlobbymetadatakey</a>
	 */
	public String getLobbyMetadataKey(long lobbyId, int index)
	{
		Object ret = core.execute(()->getLobbyMetadataKey(pointer, lobbyId, index));
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
	 * Gets the key for a metadata entry at a given index.
	 * <p>
	 * Use {@link #lobbyMetadataCount(Lobby)} to get the numbers of entries available.
	 * <p>
	 * This method simply obtains the ID of the given Lobby with {@link Lobby#getId()}.
	 * @param lobby The Lobby to get the metadata of
	 * @param index Index of the key, must be {@literal >=} 0
	 *              and {@literal <} {@link #lobbyMetadataCount(Lobby)}
	 * @return The key, max. 255 bytes
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #getLobbyMetadataKey(Lobby, int)
	 * @see #lobbyMetadataCount(Lobby)
	 * @see #getLobbyMetadata(Lobby)
	 * @see LobbyTransaction#setMetadata(String, String)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#getlobbymetadatakey">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#getlobbymetadatakey</a>
	 */
	public String getLobbyMetadataKey(Lobby lobby, int index)
	{
		return getLobbyMetadataKey(lobby.getId(), index);
	}

	/**
	 * Gets the number of available metadata entries for a given Lobby.
	 * This method can e.g. be used for iterating over the metadata.
	 * <p>
	 * If you want to get all metadata (as key/value pairs), simply
	 * use {@link #getLobbyMetadata(long)}.
	 * @param lobbyId ID of the Lobby to get the metadata for
	 * @return The number of metadata entries
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #lobbyMetadataCount(Lobby)
	 * @see #getLobbyMetadata(long)
	 * @see LobbyTransaction#setMetadata(String, String)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#lobbymetadatacount">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#lobbymetadatacount</a>
	 */
	public int lobbyMetadataCount(long lobbyId)
	{
		Object ret = core.execute(()->lobbyMetadataCount(pointer, lobbyId));
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			return (Integer) ret;
		}
	}

	/**
	 * Gets the number of available metadata entries for a given Lobby.
	 * This method can e.g. be used for iterating over the metadata.
	 * <p>
	 * If you want to get all metadata (as key/value pairs), simply
	 * use {@link #getLobbyMetadata(long)}.
	 * <p>
	 * This method simply obtains the ID of the given Lobby with {@link Lobby#getId()}.
	 * @param lobby The Lobby to get the metadata for
	 * @return The number of metadata entries
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #lobbyMetadataCount(long)
	 * @see #getLobbyMetadata(Lobby)
	 * @see LobbyTransaction#setMetadata(String, String)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#lobbymetadatacount">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#lobbymetadatacount</a>
	 */
	public int lobbyMetadataCount(Lobby lobby)
	{
		return lobbyMetadataCount(lobby.getId());
	}

	/**
	 * Gets all available metadata (as key/value pairs) for a given Lobby.
	 * <p>
	 * This is done in the following way:
	 * <ol>
	 *    <li>Get the number of available entries with {@link #lobbyMetadataCount(long)}
	 *    <li>Create a {@link HashMap} for storing the metadata
	 *    <li>For each entry {@code (i = 0; i < count; i++)} do:
	 *    <ol>
	 *        <li>Get the key with {@link #getLobbyMetadataKey(long, int)}
	 *        <li>Get the corresponding value with {@link #getLobbyMetadataValue(long, String)}
	 *        <li>Store them in the Map
	 *    </ol>
	 *    <li>Return an <i>unmodifiable</i> view of the Map with {@link Collections#unmodifiableMap(Map)}
	 * </ol>
	 * <p>
	 * The metadata is returned as an <i>unmodifiable</i> {@link HashMap}
	 * (as per {@link Collections#unmodifiableMap(Map)})
	 * to indicate that modifying the metadata will not affect the Lobby
	 * at all.
	 * To modify the metadata of a Lobby use {@link #updateLobby(long, LobbyTransaction)}
	 * and {@link LobbyTransaction#setMetadata(String, String)}.
	 * @param lobbyId ID of the Lobby to get the metadata for.
	 * @return The metadata of the Lobby as an <i>unmodifiable</i> {@link HashMap}
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #getLobbyMetadata(Lobby) 
	 * @see LobbyTransaction#setMetadata(String, String) 
	 */
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

	/**
	 * Gets all available metadata (as key/value pairs) for a given Lobby.
	 * <p>
	 * This is done in the following way:
	 * <ol>
	 *    <li>Get the number of available entries with {@link #lobbyMetadataCount(long)}
	 *    <li>Create a {@link HashMap} for storing the metadata
	 *    <li>For each entry {@code (i = 0; i < count; i++)} do:
	 *    <ol>
	 *        <li>Get the key with {@link #getLobbyMetadataKey(long, int)}
	 *        <li>Get the corresponding value with {@link #getLobbyMetadataValue(long, String)}
	 *        <li>Store them in the Map
	 *    </ol>
	 *    <li>Return an <i>unmodifiable</i> view of the Map with {@link Collections#unmodifiableMap(Map)}
	 * </ol>
	 * <p>
	 * The metadata is returned as an <i>unmodifiable</i> {@link HashMap}
	 * (as per {@link Collections#unmodifiableMap(Map)})
	 * to indicate that modifying the metadata will not affect the Lobby
	 * at all.
	 * To modify the metadata of a Lobby use {@link #updateLobby(Lobby, LobbyTransaction)}
	 * and {@link LobbyTransaction#setMetadata(String, String)}.
	 * <p>
	 * This method simply obtains the ID of the given Lobby with {@link Lobby#getId()}.
	 * @param lobby The Lobby to get the metadata for.
	 * @return The metadata of the Lobby as an <i>unmodifiable</i> {@link HashMap}
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #getLobbyMetadata(Lobby)
	 * @see LobbyTransaction#setMetadata(String, String)
	 */
	public Map<String, String> getLobbyMetadata(Lobby lobby)
	{
		return getLobbyMetadata(lobby.getId());
	}

	/**
	 * Gets the number of users that are currently connected to a Lobby.
	 * This method can e.g. be used for iterating over the members.
	 * @param lobbyId ID of the Lobby to get the member count of
	 * @return A positive integer
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #memberCount(Lobby)
	 * @see #getMemberUsers(long)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#membercount">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#membercount</a>
	 */
	public int memberCount(long lobbyId)
	{
		Object ret = core.execute(()->memberCount(pointer, lobbyId));
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			return (Integer) ret;
		}
	}

	/**
	 * Gets the number of users that are currently connected to a Lobby.
	 * This method can e.g. be used for iterating over the members.
	 * <p>
	 * This method simply obtains the ID of the given Lobby with {@link Lobby#getId()}.
	 * @param lobby The Lobby to get the member count of
	 * @return A positive integer
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #memberCount(long)
	 * @see #getMemberUsers(Lobby)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#membercount">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#membercount</a>
	 */
	public int memberCount(Lobby lobby)
	{
		return memberCount(lobby.getId());
	}

	/**
	 * Gets the Discord user ID of a member by their index in the member list.
	 * <p>
	 * Use {@link #getMemberUserIds(long)} for a list of all user IDs and
	 * {@link #getMemberUsers(long)} for a list of all user objects.
	 * @param lobbyId ID of the Lobby to get the members of
	 * @param index Index of the member in the member list, must be {@literal >=} 0
	 *              and {@literal <} {@link #memberCount(long)}
	 * @return A Discord user ID
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #getMemberUserId(Lobby, int)
	 * @see #memberCount(long)
	 * @see #getMemberUserIds(long)
	 * @see #getMemberUsers(long)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#getmemberuserid">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#getmemberuserid</a>
	 */
	public long getMemberUserId(long lobbyId, int index)
	{
		Object ret = core.execute(()->getMemberUserId(pointer, lobbyId, index));
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			return (Long) ret;
		}
	}

	/**
	 * Gets the Discord user ID of a member by their index in the member list.
	 * <p>
	 * Use {@link #getMemberUserIds(long)} for a list of all user IDs and
	 * {@link #getMemberUsers(long)} for a list of all user objects.
	 * <p>
	 * This method simply obtains the ID of the given Lobby with {@link Lobby#getId()}.
	 * @param lobby The Lobby to get the members of
	 * @param index Index of the member in the member list, must be {@literal >=} 0
	 *              and {@literal <} {@link #memberCount(Lobby)}
	 * @return A Discord user ID
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #getMemberUserId(long, int)
	 * @see #memberCount(Lobby)
	 * @see #getMemberUserIds(Lobby)
	 * @see #getMemberUsers(Lobby)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#getmemberuserid">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#getmemberuserid</a>
	 */
	public long getMemberUserId(Lobby lobby, int index)
	{
		return getMemberUserId(lobby.getId(), index);
	}

	/**
	 * Gets a list of the IDs of all users connected to a given Lobby.
	 * <p>
	 * This is done in the following way:
	 * <ol>
	 *    <li>Get the number of members with {@link #memberCount(long)}
	 *    <li>Stream the indices with {@link IntStream#range(int, int)}
	 *    <li>Map each index to the user ID with {@link #getMemberUserId(long, int)}
	 *    <li>Box the IDs and collect them into a List
	 *    <li>Return an <i>unmodifiable</i> view of the List with {@link Collections#unmodifiableList(List)}
	 * </ol>
	 * <p>
	 * The list is <i>unmodifiable</i> to indicate that changing it will not affect the Lobby.
	 * @param lobbyId ID of the Lobby to get the members of
	 * @return An <i>unmodifiable</i> list containing Discord user IDs
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #getMemberUserIds(Lobby)
	 */
	public List<Long> getMemberUserIds(long lobbyId)
	{
		List<Long> list = IntStream.range(0, memberCount(lobbyId))
				.mapToLong(i->getMemberUserId(lobbyId, i))
				.boxed()
				.collect(Collectors.toList());
		return Collections.unmodifiableList(list);
	}

	/**
	 * Gets a list of the IDs of all users connected to a given Lobby.
	 * <p>
	 * This is done in the following way:
	 * <ol>
	 *    <li>Get the number of members with {@link #memberCount(long)}
	 *    <li>Stream the indices with {@link IntStream#range(int, int)}
	 *    <li>Map each index to the user ID with {@link #getMemberUserId(long, int)}
	 *    <li>Box the IDs and collect them into a List
	 *    <li>Return an <i>unmodifiable</i> view of the List with {@link Collections#unmodifiableList(List)}
	 * </ol>
	 * <p>
	 * The list is <i>unmodifiable</i> to indicate that changing it will not affect the Lobby.
	 * <p>
	 * This method simply obtains the ID of the given Lobby with {@link Lobby#getId()}.
	 * @param lobby The Lobby to get the members of
	 * @return An <i>unmodifiable</i> list containing Discord user IDs
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #getMemberUserIds(long)
	 */
	public List<Long> getMemberUserIds(Lobby lobby)
	{
		return getMemberUserIds(lobby.getId());
	}

	/**
	 * Gets a user object for a member of a Lobby by their user ID.
	 * To get a list of all user objects use {@link #getMemberUsers(long)}.
	 * @param lobbyId ID of the Lobby to get the member of
	 * @param userId User ID of the member
	 * @return A {@link DiscordUser} object
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #getMemberUser(Lobby, long)
	 * @see #getMemberUserId(long, int)
	 * @see #getMemberUsers(long)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#getmemberuser">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#getmemberuser</a>
	 */
	public DiscordUser getMemberUser(long lobbyId, long userId)
	{
		Object ret = core.execute(()->getMemberUser(pointer, lobbyId, userId));
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			return (DiscordUser) ret;
		}
	}

	/**
	 * Gets a user object for a member of a Lobby by their user ID.
	 * To get a list of all user objects use {@link #getMemberUsers(long)}.
	 * <p>
	 * This method simply obtains the ID of the given Lobby with {@link Lobby#getId()}.
	 * @param lobby The Lobby to get the member of
	 * @param userId User ID of the member
	 * @return A {@link DiscordUser} object
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #getMemberUser(long, long)
	 * @see #getMemberUserId(Lobby, int)
	 * @see #getMemberUsers(Lobby)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#getmemberuser">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#getmemberuser</a>
	 */
	public DiscordUser getMemberUser(Lobby lobby, long userId)
	{
		return getMemberUser(lobby.getId(), userId);
	}

	/**
	 * Gets a list of user objects for the members of a Lobby.
	 * <p>
	 * This is done in the following way:
	 * <ol>
	 *     <li>Get a list of the users IDs with {@link #getMemberUserIds(long)} and stream it
	 *     <li>Map each user ID to the user object with {@link #getMemberUser(long, long)}
	 *     <li>Collect the stream into a list and return an <i>unmodifiable</i> view of it
	 * </ol>
	 * <p>
	 * The returned list is <i>unmodifiable</i> to indicate that
	 * changing it will not affect the Lobby or its members.
	 * @param lobbyId ID of the Lobby to get the members of
	 * @return An <i>unmodifiable</i> list containing Discord user objects
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #getMemberUsers(Lobby)
	 * @see #getMemberUserIds(long)
	 */
	public List<DiscordUser> getMemberUsers(long lobbyId)
	{
		List<DiscordUser> list = getMemberUserIds(lobbyId).stream()
				.map(l->getMemberUser(lobbyId, l))
				.collect(Collectors.toList());
		return Collections.unmodifiableList(list);
	}

	/**
	 * Gets a list of user objects for the members of a Lobby.
	 * <p>
	 * This is done in the following way:
	 * <ol>
	 *     <li>Get a list of the users IDs with {@link #getMemberUserIds(long)} and stream it
	 *     <li>Map each user ID to the user object with {@link #getMemberUser(long, long)}
	 *     <li>Collect the stream into a list and return an <i>unmodifiable</i> view of it
	 * </ol>
	 * <p>
	 * The returned list is <i>unmodifiable</i> to indicate that
	 * changing it will not affect the Lobby or its members.
	 * <p>
	 * This method simply obtains the ID of the given Lobby with {@link Lobby#getId()}.
	 * @param lobby The Lobby to get the members of
	 * @return An <i>unmodifiable</i> list containing Discord user objects
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #getMemberUsers(long)
	 * @see #getMemberUserIds(Lobby)
	 */
	public List<DiscordUser> getMemberUsers(Lobby lobby)
	{
		return getMemberUsers(lobby.getId());
	}

	/**
	 * Gets a member metadata value for a given Lobby, member and metadata key.
	 * @param lobbyId ID of the Lobby of the member
	 * @param userId User ID of the member
	 * @param key Metadata key, max. 255 bytes
	 * @return A metadata value, max. 4095 bytes
	 * @throws IllegalArgumentException if the key is too long
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #getMemberMetadataValue(Lobby, long, String)
	 * @see #getMemberMetadata(long, long)
	 * @see LobbyMemberTransaction#setMetadata(String, String)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#getmembermetadatavalue">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#getmembermetadatavalue</a>
	 */
	public String getMemberMetadataValue(long lobbyId, long userId, String key)
	{
		if(key.getBytes().length >= 256)
			throw new IllegalArgumentException("max key length is 255");
		Object ret = core.execute(()->getMemberMetadataValue(pointer, lobbyId, userId, key));
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
	 * Gets a member metadata value for a given Lobby, member and metadata key.
	 * <p>
	 * This method simply obtains the ID of the given Lobby with {@link Lobby#getId()}.
	 * @param lobby The Lobby of the member
	 * @param userId User ID of the member
	 * @param key Metadata key, max. 255 bytes
	 * @return A metadata value, max. 4095 bytes
	 * @throws IllegalArgumentException if the key is too long
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #getMemberMetadataValue(long, long, String)
	 * @see #getMemberMetadata(Lobby, long)
	 * @see LobbyMemberTransaction#setMetadata(String, String)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#getmembermetadatavalue">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#getmembermetadatavalue</a>
	 */
	public String getMemberMetadataValue(Lobby lobby, long userId, String key)
	{
		return getMemberMetadataValue(lobby.getId(), userId, key);
	}

	/**
	 * Gets the key for a member metadata entry at a given index.
	 * <p>
	 * Use {@link #memberMetadataCount(long, long)} to get the numbers of entries available.
	 * @param lobbyId ID of the Lobby of the member
	 * @param userId User ID of the member
	 * @param index Index of the key, must be {@literal >=} 0
	 *              and {@literal <} {@link #memberMetadataCount(long, long)}
	 * @return The key, max. 255 bytes
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #getMemberMetadataKey(Lobby, long, int)
	 * @see #memberMetadataCount(long, long)
	 * @see #getMemberMetadata(long, long)
	 * @see LobbyMemberTransaction#setMetadata(String, String)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#getmembermetadatakey">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#getmembermetadatakey</a>
	 */
	public String getMemberMetadataKey(long lobbyId, long userId, int index)
	{
		Object ret = core.execute(()->getMemberMetadataKey(pointer, lobbyId, userId, index));
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
	 * Gets the key for a member metadata entry at a given index.
	 * <p>
	 * Use {@link #memberMetadataCount(long, long)} to get the numbers of entries available.
	 * <p>
	 * This method simply obtains the ID of the given Lobby with {@link Lobby#getId()}.
	 * @param lobby The Lobby of the member
	 * @param userId User ID of the member
	 * @param index Index of the key, must be {@literal >=} 0
	 *              and {@literal <} {@link #memberMetadataCount(long, long)}
	 * @return The key, max. 255 bytes
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #getMemberMetadataKey(long, long, int)
	 * @see #memberMetadataCount(Lobby, long)
	 * @see #getMemberMetadata(Lobby, long)
	 * @see LobbyMemberTransaction#setMetadata(String, String)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#getmembermetadatakey">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#getmembermetadatakey</a>
	 */
	public String getMemberMetadataKey(Lobby lobby, long userId, int index)
	{
		return getMemberMetadataKey(lobby.getId(), userId, index);
	}

	/**
	 * Gets the number of available metadata entries for a given member in a Lobby.
	 * This method can e.g. be used for iterating over the metadata.
	 * <p>
	 * If you want to get all metadata (as key/value pairs), simply
	 * use {@link #getMemberMetadata(long, long)}.
	 * @param lobbyId ID of the Lobby of the member
	 * @param userId User ID of the member
	 * @return The number of metadata entries
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #memberMetadataCount(Lobby, long)
	 * @see #getMemberMetadata(long, long)
	 * @see LobbyMemberTransaction#setMetadata(String, String)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#membermetadatacount">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#membermetadatacount</a>
	 */
	public int memberMetadataCount(long lobbyId, long userId)
	{
		Object ret = core.execute(()->memberMetadataCount(pointer, lobbyId, userId));
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			return (Integer) ret;
		}
	}

	/**
	 * Gets the number of available metadata entries for a given member in a Lobby.
	 * This method can e.g. be used for iterating over the metadata.
	 * <p>
	 * If you want to get all metadata (as key/value pairs), simply
	 * use {@link #getMemberMetadata(long, long)}.
	 * <p>
	 * This method simply obtains the ID of the given Lobby with {@link Lobby#getId()}.
	 * @param lobby The Lobby of the member
	 * @param userId User ID of the member
	 * @return The number of metadata entries
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #memberMetadataCount(long, long)
	 * @see #getMemberMetadata(Lobby, long)
	 * @see LobbyMemberTransaction#setMetadata(String, String)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#membermetadatacount">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#membermetadatacount</a>
	 */
	public int memberMetadataCount(Lobby lobby, long userId)
	{
		return memberMetadataCount(lobby.getId(), userId);
	}

	/**
	 * Gets all metadata entries for a given member of a given Lobby.
	 * This methods uses roughly the same procedure as {@link #getLobbyMetadata(long)}.
	 * <p>
	 * The metadata is returned as an <i>unmodifiable</i> {@link HashMap}
	 * (as per {@link Collections#unmodifiableMap(Map)})
	 * to indicate that modifying the metadata will not affect
	 * the Lobby or the member at all.
	 * To modify the metadata of a member use {@link #updateMember(long, long, LobbyMemberTransaction)}}
	 * and {@link LobbyMemberTransaction#setMetadata(String, String)}.
	 * @param lobbyId ID of the Lobby of the member
	 * @param userId User ID of the member
	 * @return The metadata of the member as an <i>unmodifiable</i> {@link HashMap}
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #getMemberMetadata(Lobby, long)
	 * @see LobbyMemberTransaction#setMetadata(String, String)
	 */
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

	/**
	 * Gets all metadata entries for a given member of a given Lobby.
	 * This methods uses roughly the same procedure as {@link #getLobbyMetadata(Lobby)}.
	 * <p>
	 * The metadata is returned as an <i>unmodifiable</i> {@link HashMap}
	 * (as per {@link Collections#unmodifiableMap(Map)})
	 * to indicate that modifying the metadata will not affect
	 * the Lobby or the member at all.
	 * To modify the metadata of a member use {@link #updateMember(Lobby, long, LobbyMemberTransaction)}}
	 * and {@link LobbyMemberTransaction#setMetadata(String, String)}.
	 * <p>
	 * This method simply obtains the ID of the given Lobby with {@link Lobby#getId()}.
	 * @param lobby The Lobby of the member
	 * @param userId User ID of the member
	 * @return The metadata of the member as an <i>unmodifiable</i> {@link HashMap}
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #getMemberMetadata(long, long)
	 * @see LobbyMemberTransaction#setMetadata(String, String)
	 */
	public Map<String, String> getMemberMetadata(Lobby lobby, long userId)
	{
		return getMemberMetadata(lobby.getId(), userId);
	}

	/**
	 * Updates the information for a given member according to a {@link LobbyMemberTransaction}.
	 * <p>
	 * A {@link DiscordEventAdapter#onMemberUpdate(long, long)}
	 * will be fired for all members of the Lobby.
	 * @param lobbyId ID of the Lobby of the member
	 * @param userId User ID of the member
	 * @param transaction Transaction specifying what to update
	 * @param callback Callback to process the returned {@link Result}
	 * @see #updateMember(long, long, LobbyMemberTransaction)
	 * @see #getMemberUpdateTransaction(Lobby, long)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#updatemember">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#updatemember</a>
	 */
	public void updateMember(long lobbyId, long userId, LobbyMemberTransaction transaction, Consumer<Result> callback)
	{
		core.execute(()->updateMember(pointer, lobbyId, userId, transaction.getPointer(), Objects.requireNonNull(callback)));
	}

	/**
	 * Updates the information for a given member according to a {@link LobbyMemberTransaction}.
	 * <p>
	 * A {@link DiscordEventAdapter#onMemberUpdate(long, long)}
	 * will be fired for all members of the Lobby..
	 * <p>
	 * The {@link Core#DEFAULT_CALLBACK} is used to handle the returned {@link Result}.
	 * @param lobbyId ID of the Lobby of the member
	 * @param userId User ID of the member
	 * @param transaction Transaction specifying what to update
	 * @see #updateMember(long, long, LobbyMemberTransaction, Consumer)
	 * @see #getMemberUpdateTransaction(Lobby, long)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#updatemember">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#updatemember</a>
	 */
	public void updateMember(long lobbyId, long userId, LobbyMemberTransaction transaction)
	{
		updateMember(lobbyId, userId, transaction, Core.DEFAULT_CALLBACK);
	}

	/**
	 * Updates the information for a given member according to a {@link LobbyMemberTransaction}.
	 * <p>
	 * A {@link DiscordEventAdapter#onMemberUpdate(long, long)}
	 * will be fired for all members of the Lobby.
	 * <p>
	 * This method simply obtains the ID of the given Lobby with {@link Lobby#getId()}.
	 * @param lobby The Lobby of the member
	 * @param userId User ID of the member
	 * @param transaction Transaction specifying what to update
	 * @param callback Callback to process the returned {@link Result}
	 * @see #updateMember(Lobby, long, LobbyMemberTransaction)
	 * @see #getMemberUpdateTransaction(Lobby, long)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#updatemember">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#updatemember</a>
	 */
	public void updateMember(Lobby lobby, long userId, LobbyMemberTransaction transaction, Consumer<Result> callback)
	{
		updateMember(lobby.getId(), userId, transaction, callback);
	}

	/**
	 * Updates the information for a given member according to a {@link LobbyMemberTransaction}.
	 * <p>
	 * A {@link DiscordEventAdapter#onMemberUpdate(long, long)}
	 * will be fired for all members of the Lobby.
	 * <p>
	 * The {@link Core#DEFAULT_CALLBACK} is used to handle the returned {@link Result}.
	 * <p>
	 * This method simply obtains the ID of the given Lobby with {@link Lobby#getId()}.
	 * @param lobby The Lobby of the member
	 * @param userId User ID of the member
	 * @param transaction Transaction specifying what to update
	 * @see #updateMember(Lobby, long, LobbyMemberTransaction, Consumer)
	 * @see #getMemberUpdateTransaction(Lobby, long)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#updatemember">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#updatemember</a>
	 */
	public void updateMember(Lobby lobby, long userId, LobbyMemberTransaction transaction)
	{
		updateMember(lobby, userId, transaction, Core.DEFAULT_CALLBACK);
	}

	/**
	 * Sends a message to all members of a Lobby.
	 * The current user must be connected to the Lobby.
	 * The message is received in {@link DiscordEventAdapter#onLobbyMessage(long, long, byte[])}.
	 * <p>
	 * This function is rate-limited at 10 messages per 5 seconds.
	 * <p>
	 * It is not recommended to use this function together built-in Lobby networking
	 * layer.
	 * If you are using the built-in layer prefer
	 * {@link #sendNetworkMessage(long, long, byte, byte[])} over this function.
	 * @param lobbyId ID of the Lobby to send the message to
	 * @param data Message data to send
	 * @param callback Callback to process the returned {@link Result}
	 * @see #sendLobbyMessage(long, byte[])
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#sendlobbymessage">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#sendlobbymessage</a>
	 */
	public void sendLobbyMessage(long lobbyId, byte[] data, Consumer<Result> callback)
	{
		core.execute(()->sendLobbyMessage(pointer, lobbyId, data, 0, data.length, Objects.requireNonNull(callback)));
	}

	/**
	 * Sends a message to all members of a Lobby.
	 * The current user must be connected to the Lobby.
	 * The message is received in {@link DiscordEventAdapter#onLobbyMessage(long, long, byte[])}.
	 * <p>
	 * This function is rate-limited at 10 messages per 5 seconds.
	 * <p>
	 * It is not recommended to use this function together built-in Lobby networking
	 * layer.
	 * If you are using the built-in layer prefer
	 * {@link #sendNetworkMessage(long, long, byte, byte[])} over this function.
	 * <p>
	 * The {@link Core#DEFAULT_CALLBACK} is used to handle the returned {@link Result}.
	 * @param lobbyId ID of the Lobby to send the message to
	 * @param data Message data to send
	 * @see #sendLobbyMessage(long, byte[])
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#sendlobbymessage">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#sendlobbymessage</a>
	 */
	public void sendLobbyMessage(long lobbyId, byte[] data)
	{
		sendLobbyMessage(lobbyId, data, Core.DEFAULT_CALLBACK);
	}

	/**
	 * Sends a message to all members of a Lobby.
	 * The current user must be connected to the Lobby.
	 * The message is received in {@link DiscordEventAdapter#onLobbyMessage(long, long, byte[])}.
	 * <p>
	 * This function is rate-limited at 10 messages per 5 seconds.
	 * <p>
	 * It is not recommended to use this function together built-in Lobby networking
	 * layer.
	 * If you are using the built-in layer prefer
	 * {@link #sendNetworkMessage(long, long, byte, byte[])} over this function.
	 * <p>
	 * This method simply obtains the ID of the given Lobby with {@link Lobby#getId()}.
	 * @param lobby The Lobby to send the message to
	 * @param data Message data to send
	 * @param callback Callback to process the returned {@link Result}
	 * @see #sendLobbyMessage(long, byte[])
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#sendlobbymessage">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#sendlobbymessage</a>
	 */
	public void sendLobbyMessage(Lobby lobby, byte[] data, Consumer<Result> callback)
	{
		sendLobbyMessage(lobby.getId(), data, callback);
	}


	/**
	 * Sends a message to all members of a Lobby.
	 * The current user must be connected to the Lobby.
	 * The message is received in {@link DiscordEventAdapter#onLobbyMessage(long, long, byte[])}.
	 * <p>
	 * This function is rate-limited at 10 messages per 5 seconds.
	 * <p>
	 * It is not recommended to use this function together built-in Lobby networking
	 * layer.
	 * If you are using the built-in layer prefer
	 * {@link #sendNetworkMessage(long, long, byte, byte[])} over this function.
	 * <p>
	 * The {@link Core#DEFAULT_CALLBACK} is used to handle the returned {@link Result}.
	 * <p>
	 * This method simply obtains the ID of the given Lobby with {@link Lobby#getId()}.
	 * @param lobby The Lobby to send the message to
	 * @param data Message data to send
	 * @see #sendLobbyMessage(long, byte[])
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#sendlobbymessage">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#sendlobbymessage</a>
	 */
	public void sendLobbyMessage(Lobby lobby, byte[] data)
	{
		sendLobbyMessage(lobby, data, Core.DEFAULT_CALLBACK);
	}

	/**
	 * Returns a new {@link LobbySearchQuery}.
	 * A search query is used with {@link #search(LobbySearchQuery, Consumer)} to search for
	 * other available and Discord lobbies.
	 * <p>
	 * This method is the only way of obtaining an instance of {@link LobbySearchQuery}.
	 * Do <b>not</b> attempt to create an instance in any other way.
	 * @return A {@link LobbySearchQuery}
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #search(LobbySearchQuery, Consumer)
	 * @see #getSearchQuery()
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#getsearchquery">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#getsearchquery</a>
	 */
	public LobbySearchQuery getSearchQuery()
	{
		Object ret = core.execute(()->getSearchQuery(pointer));
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			return (LobbySearchQuery) ret;
		}
	}

	/**
	 * Searches available Lobbies according to the given {@link LobbySearchQuery}.
	 * Only Lobbies that are {@link LobbyType#PUBLIC}, not {@link Lobby#isLocked()} and
	 * meet the criteria of the search query are found by this method.
	 * <p>
	 * The callback fires when the search is completed and
	 * a stable list of Lobbies is available.
	 * There is no need to access the search results inside the
	 * callback, but it helps with timing.
	 * <p>
	 * You can access the found Lobbies using the following methods:
	 * <ul>
	 *      <li>{@link #lobbyCount()} - number of found Lobbies
	 *      <li>{@link #getLobbyId(int)} - Lobby ID by index in result list
	 *      <li>{@link #getLobby(long)} - Lobby object in result list by ID
	 *      <li>{@link #getLobbies()} - all Lobby objects in result list
	 * </ul>
	 * @param query A {@link LobbySearchQuery} specifying additional criteria and sorting
	 * @param callback Callback to process the returned {@link Result}
	 * @see #search(LobbySearchQuery)
	 * @see #getSearchQuery()
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#search">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#search</a>
	 */
	public void search(LobbySearchQuery query, Consumer<Result> callback)
	{
		core.execute(()->search(pointer, query.getPointer(), Objects.requireNonNull(callback)));
	}

	/**
	 * Searches available Lobbies according to the given {@link LobbySearchQuery}.
	 * Only Lobbies that are {@link LobbyType#PUBLIC}, not {@link Lobby#isLocked()} and
	 * meet the criteria of the search query are found by this method.
	 * <p>
	 * The callback fires when the search is completed and
	 * a stable list of Lobbies is available.
	 * There is no need to access the search results inside the
	 * callback, but it helps with timing.
	 * <p>
	 * You can access the found Lobbies using the following methods:
	 * <ul>
	 *      <li>{@link #lobbyCount()} - number of found Lobbies
	 *      <li>{@link #getLobbyId(int)} - Lobby ID by index in result list
	 *      <li>{@link #getLobby(long)} - Lobby object in result list by ID
	 *      <li>{@link #getLobbies()} - all Lobby objects in result list
	 * </ul>
	 * <p>
	 * The {@link Core#DEFAULT_CALLBACK} is used to handle the returned {@link Result}.
	 * Be careful about timing,
	 * so you don't try to access the search result, before it is ready.
	 * @param query A {@link LobbySearchQuery} specifying additional criteria and sorting
	 * @see #search(LobbySearchQuery, Consumer) 
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#search">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#search</a>
	 */
	public void search(LobbySearchQuery query)
	{
		search(query, Core.DEFAULT_CALLBACK);
	}

	/**
	 * Gets the number of Lobbies found in the last {@link #search(LobbySearchQuery, Consumer)}.
	 * <p>
	 * If you want a list of found Lobbies use either
	 * {@link #getLobbyIds()} or {@link #getLobbies()}.
	 * @return A positive integer or {@code 0} if no Lobbies were found
	 * @see #search(LobbySearchQuery, Consumer)
	 * @see #getLobbyId(int)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#lobbycount">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#lobbycount</a>
	 */
	public int lobbyCount()
	{
		return core.execute(()->lobbyCount(pointer));
	}

	/**
	 * Gets the ID of the Lobby at the given index in the {@link #search(LobbySearchQuery, Consumer)} result.
	 * Use {@link #lobbyCount()} the get the number of Lobbies in the result.
	 * <p>
	 * If you want a list of found Lobbies use either
	 * {@link #getLobbyIds()} or {@link #getLobbies()}.
	 * @param index Index of the Lobby, must be {@literal >=} 0
	 *              and {@literal <} {@link #lobbyCount()}
	 * @return A Discord Lobby ID
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #search(LobbySearchQuery, Consumer)
	 * @see #lobbyCount()
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#getlobbyid">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#getlobbyid</a>
	 */
	public long getLobbyId(int index)
	{
		Object ret = core.execute(()->getLobbyId(pointer, index));
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			return (long) ret;
		}
	}

	/**
	 * Gets a list of the IDs of all Lobbies found in the last {@link #search(LobbySearchQuery, Consumer)}.
	 * <p>
	 * This is done in the following way:
	 * <ol>
	 *    <li>Get the number of Lobbies with {@link #lobbyCount()}
	 *    <li>Stream the indices with {@link IntStream#range(int, int)}
	 *    <li>Map each index to the Lobby ID with {@link #getLobbyId(int)}
	 *    <li>Box the IDs and collect them into a List
	 *    <li>Return an <i>unmodifiable</i> view of the List with {@link Collections#unmodifiableList(List)}
	 * </ol>
	 * <p>
	 * The list is <i>unmodifiable</i> to indicate that changing it will not affect the Lobbies
	 * or the search result, which is stored by Discord.
	 * @return An <i>unmodifiable</i> list containing Discord Lobby IDs
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #search(LobbySearchQuery, Consumer)
	 */
	public List<Long> getLobbyIds()
	{
		List<Long> list = IntStream.range(0, lobbyCount())
		                           .mapToLong(this::getLobbyId)
		                           .boxed()
		                           .collect(Collectors.toList());
		return Collections.unmodifiableList(list);
	}

	/**
	 * Gets a list of all Lobbies found in the last {@link #search(LobbySearchQuery, Consumer)}.
	 * <p>
	 * This is done in the following way:
	 * <ol>
	 *    <li>Get a list of the Lobby IDs with {@link #getLobbyIds()} and stream it
	 *    <li>Map each ID to the Lobby object with {@link #getLobby(long)}
	 *    <li>Collect the stream into a list and return an <i>unmodifiable</i> view of the it
	 * </ol>
	 * <p>
	 * The list is <i>unmodifiable</i> to indicate that changing it will not affect the Lobbies
	 * or the search result, which is stored by Discord.
	 * @return An <i>unmodifiable</i> list containing {@link Lobby} objects
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #search(LobbySearchQuery, Consumer)
	 */
	public List<Lobby> getLobbies()
	{
		List<Lobby> list = getLobbyIds().stream()
		                                .map(this::getLobby)
		                                .collect(Collectors.toList());
		return Collections.unmodifiableList(list);
	}

	/**
	 * Connects the current user to the voice channel of a given Lobby.
	 * <p>
	 * The user can see a list of other users connected to the Lobby
	 * in their Discord Overlay.
	 * The window to adjust voice settings can be opened with
	 * {@link OverlayManager#openVoiceSettings(Consumer)}.
	 * @param lobbyId ID of the Lobby whose voice channel to connect to
	 * @param callback Callback to process the returned {@link Result}
	 * @see #connectVoice(long)
	 * @see #disconnectVoice(long, Consumer)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#connectvoice">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#connectvoice</a>
	 */
	public void connectVoice(long lobbyId, Consumer<Result> callback)
	{
		core.execute(()->connectVoice(pointer, lobbyId, Objects.requireNonNull(callback)));
	}

	/**
	 * Connects the current user to the voice channel of a given Lobby.
	 * <p>
	 * The user can see a list of other users connected to the Lobby
	 * in their Discord Overlay.
	 * The window to adjust voice settings can be opened with
	 * {@link OverlayManager#openVoiceSettings()}.
	 * <p>
	 * The {@link Core#DEFAULT_CALLBACK} is used to handle the returned {@link Result}.
	 * @param lobbyId ID of the Lobby whose voice channel to connect to
	 * @see #connectVoice(long, Consumer)
	 * @see #disconnectVoice(long)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#connectvoice">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#connectvoice</a>
	 */
	public void connectVoice(long lobbyId)
	{
		connectVoice(lobbyId, Core.DEFAULT_CALLBACK);
	}

	/**
	 * Connects the current user to the voice channel of a given Lobby.
	 * <p>
	 * The user can see a list of other users connected to the Lobby
	 * in their Discord Overlay.
	 * The window to adjust voice settings can be opened with
	 * {@link OverlayManager#openVoiceSettings(Consumer)}.
	 * <p>
	 * This method simply obtains the ID of the given Lobby with {@link Lobby#getId()}.
	 * @param lobby The Lobby whose voice channel to connect to
	 * @param callback Callback to process the returned {@link Result}
	 * @see #connectVoice(Lobby)
	 * @see #disconnectVoice(Lobby, Consumer)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#connectvoice">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#connectvoice</a>
	 */
	public void connectVoice(Lobby lobby, Consumer<Result> callback)
	{
		connectVoice(lobby.getId(), callback);
	}

	/**
	 * Connects the current user to the voice channel of a given Lobby.
	 * <p>
	 * The user can see a list of other users connected to the Lobby
	 * in their Discord Overlay.
	 * The window to adjust voice settings can be opened with
	 * {@link OverlayManager#openVoiceSettings()}.
	 * <p>
	 * The {@link Core#DEFAULT_CALLBACK} is used to handle the returned {@link Result}.
	 * <p>
	 * This method simply obtains the ID of the given Lobby with {@link Lobby#getId()}.
	 * @param lobby The Lobby whole voice channel to connect to
	 * @see #connectVoice(Lobby, Consumer)
	 * @see #disconnectVoice(Lobby)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#connectvoice">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#connectvoice</a>
	 */
	public void connectVoice(Lobby lobby)
	{
		connectVoice(lobby, Core.DEFAULT_CALLBACK);
	}

	/**
	 * Disconnects the current user from the voice channel of a given Lobby.
	 * @param lobbyId ID of the Lobby whose voice channel to disconnect from
	 * @param callback Callback to process the returned {@link Result}
	 * @see #disconnectVoice(long)
	 * @see #connectVoice(long, Consumer)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#disconnectvoice">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#disconnectvoice</a>
	 */
	public void disconnectVoice(long lobbyId, Consumer<Result> callback)
	{
		core.execute(()->disconnectVoice(pointer, lobbyId, Objects.requireNonNull(callback)));
	}

	/**
	 * Disconnects the current user from the voice channel of a given Lobby.
	 * <p>
	 * The {@link Core#DEFAULT_CALLBACK} is used to handle the returned {@link Result}.
	 * @param lobbyId ID of the Lobby whose voice channel to disconnect from
	 * @see #disconnectVoice(long, Consumer)
	 * @see #connectVoice(long)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#disconnectvoice">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#disconnectvoice</a>
	 */
	public void disconnectVoice(long lobbyId)
	{
		disconnectVoice(lobbyId, Core.DEFAULT_CALLBACK);
	}

	/**
	 * Disconnects the current user from the voice channel of a given Lobby.
	 * <p>
	 * This method simply obtains the ID of the given Lobby with {@link Lobby#getId()}.
	 * @param lobby The Lobby whose voice channel to disconnect from
	 * @param callback Callback to process the returned {@link Result}
	 * @see #disconnectVoice(Lobby)
	 * @see #connectVoice(Lobby, Consumer)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#disconnectvoice">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#disconnectvoice</a>
	 */
	public void disconnectVoice(Lobby lobby, Consumer<Result> callback)
	{
		disconnectVoice(lobby.getId(), callback);
	}

	/**
	 * Disconnects the current user from the voice channel of a given Lobby.
	 * <p>
	 * The {@link Core#DEFAULT_CALLBACK} is used to handle the returned {@link Result}.
	 * <p>
	 * This method simply obtains the ID of the given Lobby with {@link Lobby#getId()}.
	 * @param lobby The Lobby whose voice channel to disconnect from
	 * @see #disconnectVoice(Lobby, Consumer)
	 * @see #connectVoice(Lobby)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#disconnectvoice">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#disconnectvoice</a>
	 */
	public void disconnectVoice(Lobby lobby)
	{
		disconnectVoice(lobby, Core.DEFAULT_CALLBACK);
	}

	/**
	 * Connects to the networking layer of a given Lobby.
	 * The current users must be already connected to the Lobby itself.
	 * <p>
	 * This also automatically sets certain member metadata later
	 * ("{@code $peer_id}" and "{@code $route}") to communicate
	 * networking parameters.
	 * <p>
	 * Basic networking structure:
	 * <ol>
	 *     <li>Connect to the Lobby with {@link #connectLobby(long, String, BiConsumer)}
	 *     <li>Connect to the Lobby's networking layer with {@link #connectNetwork(long)}
	 *     <li>Open some channels with {@link #openNetworkChannel(long, byte, boolean)}
	 *     <li>Send messages with {@link #sendNetworkMessage(long, long, byte, byte[])}
	 *     <li>Repeatedly flush the networking layer with {@link #flushNetwork()}
	 *     <li>When you are done, disconnect from the networking layer with {@link #disconnectNetwork(long)}
	 *     <li>Finally, disconnect from the Lobby with {@link #disconnectLobby(long)}
	 * </ol>
	 * See <a href="https://discord.com/developers/docs/game-sdk/lobbies#integrated-networking">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#integrated-networking</a> and
	 * <a href="https://discord.com/developers/docs/game-sdk/lobbies#example-networking-the-easy-way">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#example-networking-the-easy-way</a>
	 * for more details and examples.
	 * @param lobbyId ID of the Lobby whose networking layer to connect to
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #connectNetwork(Lobby)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#connectnetwork">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#connectnetwork</a>
	 */
	public void connectNetwork(long lobbyId)
	{
		Result result = core.execute(()->connectNetwork(pointer, lobbyId));
		if(result != Result.OK)
			throw new GameSDKException(result);
	}

	/**
	 * Connects to the networking layer of a given Lobby.
	 * The current users must be already connected to the Lobby itself.
	 * <p>
	 * This also automatically sets certain member metadata later
	 * ("{@code $peer_id}" and "{@code $route}") to communicate
	 * networking parameters.
	 * Hence a {@link DiscordEventAdapter#onMemberUpdate(long, long)}
	 * will be fired, but does not have to be handled manually.
	 * <p>
	 * Basic networking structure:
	 * <ol>
	 *     <li>Connect to the Lobby with {@link #connectLobby(Lobby, BiConsumer)}
	 *     <li>Connect to the Lobby's networking layer with {@link #connectNetwork(Lobby)}
	 *     <li>Open some channels with {@link #openNetworkChannel(Lobby, byte, boolean)}
	 *     <li>Send messages with {@link #sendNetworkMessage(Lobby, long, byte, byte[])}
	 *     <li>Repeatedly flush the networking layer with {@link #flushNetwork()}
	 *     <li>When you are done, disconnect from the networking layer with {@link #disconnectNetwork(Lobby)}
	 *     <li>Finally, disconnect from the Lobby with {@link #disconnectLobby(Lobby)}
	 * </ol>
	 * See <a href="https://discord.com/developers/docs/game-sdk/lobbies#integrated-networking">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#integrated-networking</a> and
	 * <a href="https://discord.com/developers/docs/game-sdk/lobbies#example-networking-the-easy-way">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#example-networking-the-easy-way</a>
	 * for more details and examples.
	 * <p>
	 * This method simply obtains the ID of the given Lobby with {@link Lobby#getId()}.
	 * @param lobby The Lobby whose networking layer to connect to
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #connectNetwork(long)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#connectnetwork">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#connectnetwork</a>
	 */
	public void connectNetwork(Lobby lobby)
	{
		connectNetwork(lobby.getId());
	}

	/**
	 * Disconnects from the networking layer of a given Lobby.
	 * <p>
	 * Basic networking structure:
	 * <ol>
	 *     <li>Connect to the Lobby with {@link #connectLobby(long, String, BiConsumer)}
	 *     <li>Connect to the Lobby's networking layer with {@link #connectNetwork(long)}
	 *     <li>Open some channels with {@link #openNetworkChannel(long, byte, boolean)}
	 *     <li>Send messages with {@link #sendNetworkMessage(long, long, byte, byte[])}
	 *     <li>Repeatedly flush the networking layer with {@link #flushNetwork()}
	 *     <li>When you are done, disconnect from the networking layer with {@link #disconnectNetwork(long)}
	 *     <li>Finally, disconnect from the Lobby with {@link #disconnectLobby(long)}
	 * </ol>
	 * See <a href="https://discord.com/developers/docs/game-sdk/lobbies#integrated-networking">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#integrated-networking</a> and
	 * <a href="https://discord.com/developers/docs/game-sdk/lobbies#example-networking-the-easy-way">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#example-networking-the-easy-way</a>
	 * for more details and examples.
	 * @param lobbyId ID of the Lobby whose networking layer to disconnect from
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #disconnectNetwork(Lobby)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#disconnectnetwork">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#disconnectnetwork</a>
	 */
	public void disconnectNetwork(long lobbyId)
	{
		Result result = core.execute(()->disconnectNetwork(pointer, lobbyId));
		if(result != Result.OK)
			throw new GameSDKException(result);
	}

	/**
	 * Disconnects from the networking layer of a given Lobby.
	 * <p>
	 * Basic networking structure:
	 * <ol>
	 *     <li>Connect to the Lobby with {@link #connectLobby(Lobby, BiConsumer)}
	 *     <li>Connect to the Lobby's networking layer with {@link #connectNetwork(Lobby)}
	 *     <li>Open some channels with {@link #openNetworkChannel(Lobby, byte, boolean)}
	 *     <li>Send messages with {@link #sendNetworkMessage(Lobby, long, byte, byte[])}
	 *     <li>Repeatedly flush the networking layer with {@link #flushNetwork()}
	 *     <li>When you are done, disconnect from the networking layer with {@link #disconnectNetwork(Lobby)}
	 *     <li>Finally, disconnect from the Lobby with {@link #disconnectLobby(Lobby)}
	 * </ol>
	 * See <a href="https://discord.com/developers/docs/game-sdk/lobbies#integrated-networking">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#integrated-networking</a> and
	 * <a href="https://discord.com/developers/docs/game-sdk/lobbies#example-networking-the-easy-way">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#example-networking-the-easy-way</a>
	 * for more details and examples.
	 * <p>
	 * This method simply obtains the ID of the given Lobby with {@link Lobby#getId()}.
	 * @param lobby The Lobby whose networking layer to disconnect from
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #disconnectNetwork(long)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#disconnectnetwork">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#disconnectnetwork</a>
	 */
	public void disconnectNetwork(Lobby lobby)
	{
		disconnectNetwork(lobby.getId());
	}

	/**
	 * Flushes the Lobby networking layer.
	 * <p>
	 * This should be called after sending all messages and therefore
	 * at the end of a (game) "tick".
	 * Try to avoid calling this function inside a Discord callback,
	 * which has been observed to be problematic for the networking layer.
	 * <p>
	 * Basic networking structure:
	 * <ol>
	 *     <li>Connect to the Lobby with {@link #connectLobby(long, String, BiConsumer)}
	 *     <li>Connect to the Lobby's networking layer with {@link #connectNetwork(long)}
	 *     <li>Open some channels with {@link #openNetworkChannel(long, byte, boolean)}
	 *     <li>Send messages with {@link #sendNetworkMessage(long, long, byte, byte[])}
	 *     <li>Repeatedly flush the networking layer with {@link #flushNetwork()}
	 *     <li>When you are done, disconnect from the networking layer with {@link #disconnectNetwork(long)}
	 *     <li>Finally, disconnect from the Lobby with {@link #disconnectLobby(long)}
	 * </ol>
	 * See <a href="https://discord.com/developers/docs/game-sdk/lobbies#integrated-networking">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#integrated-networking</a> and
	 * <a href="https://discord.com/developers/docs/game-sdk/lobbies#example-networking-the-easy-way">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#example-networking-the-easy-way</a>
	 * for more details and examples.
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#flushnetwork">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#flushnetwork</a>
	 */
	public void flushNetwork()
	{
		Result result = core.execute(()->flushNetwork(pointer));
		if(result != Result.OK)
			throw new GameSDKException(result);
	}

	/**
	 * Opens a network channel to all members of a given Lobby.
	 * <p>
	 * You need to be connected to be Lobby's network before
	 * opening a channel (see {@link #connectNetwork(long)}).
	 * <p>
	 * Basic networking structure:
	 * <ol>
	 *     <li>Connect to the Lobby with {@link #connectLobby(long, String, BiConsumer)}
	 *     <li>Connect to the Lobby's networking layer with {@link #connectNetwork(long)}
	 *     <li>Open some channels with {@link #openNetworkChannel(long, byte, boolean)}
	 *     <li>Send messages with {@link #sendNetworkMessage(long, long, byte, byte[])}
	 *     <li>Repeatedly flush the networking layer with {@link #flushNetwork()}
	 *     <li>When you are done, disconnect from the networking layer with {@link #disconnectNetwork(long)}
	 *     <li>Finally, disconnect from the Lobby with {@link #disconnectLobby(long)}
	 * </ol>
	 * See <a href="https://discord.com/developers/docs/game-sdk/lobbies#integrated-networking">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#integrated-networking</a> and
	 * <a href="https://discord.com/developers/docs/game-sdk/lobbies#example-networking-the-easy-way">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#example-networking-the-easy-way</a>
	 * for more details and examples.
	 * @param lobbyId ID of the Lobby to open a channel in
	 * @param channelId ID of the new network channel (can be any {@code byte})
	 * @param reliable Whether the channel should be reliable
	 *                 (e.g. is intended for important data)
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #openNetworkChannel(Lobby, byte, boolean)
	 * @see #connectNetwork(long)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#opennetworkchannel">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#opennetworkchannel</a>
	 */
	public void openNetworkChannel(long lobbyId, byte channelId, boolean reliable)
	{
		Result result = core.execute(()->openNetworkChannel(pointer, lobbyId, channelId, reliable));
		if(result != Result.OK)
			throw new GameSDKException(result);
	}

	/**
	 * Opens a network channel to all members of a given Lobby.
	 * <p>
	 * You need to be connected to be Lobby's network before
	 * opening a channel (see {@link #connectNetwork(Lobby)}).
	 * <p>
	 * Basic networking structure:
	 * <ol>
	 *     <li>Connect to the Lobby with {@link #connectLobby(Lobby, BiConsumer)}
	 *     <li>Connect to the Lobby's networking layer with {@link #connectNetwork(Lobby)}
	 *     <li>Open some channels with {@link #openNetworkChannel(Lobby, byte, boolean)}
	 *     <li>Send messages with {@link #sendNetworkMessage(Lobby, long, byte, byte[])}
	 *     <li>Repeatedly flush the networking layer with {@link #flushNetwork()}
	 *     <li>When you are done, disconnect from the networking layer with {@link #disconnectNetwork(Lobby)}
	 *     <li>Finally, disconnect from the Lobby with {@link #disconnectLobby(Lobby)}
	 * </ol>
	 * See <a href="https://discord.com/developers/docs/game-sdk/lobbies#integrated-networking">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#integrated-networking</a> and
	 * <a href="https://discord.com/developers/docs/game-sdk/lobbies#example-networking-the-easy-way">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#example-networking-the-easy-way</a>
	 * for more details and examples.
	 * <p>
	 * This method simply obtains the ID of the given Lobby with {@link Lobby#getId()}.
	 * @param lobby The Lobby to open a channel in
	 * @param channelId ID of the new network channel (can be any {@code byte})
	 * @param reliable Whether the channel should be reliable
	 *                 (e.g. is intended for important data)
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #openNetworkChannel(long, byte, boolean)
	 * @see #connectNetwork(Lobby)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#opennetworkchannel">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#opennetworkchannel</a>
	 */
	public void openNetworkChannel(Lobby lobby, byte channelId, boolean reliable)
	{
		openNetworkChannel(lobby.getId(), channelId, reliable);
	}

	/**
	 * Sends a network message to a given member of a given Lobby on a given channel.
	 * A message is received in {@link DiscordEventAdapter#onNetworkMessage(long, long, byte, byte[])}.
	 * <p>
	 * You need to open a channel before sending messages over it
	 * (see {@link #openNetworkChannel(long, byte, boolean)}).
	 * Also make sure to call {@link #flushNetwork()} somewhere
	 * or your messages won't be sent.
	 * <p>
	 * Basic networking structure:
	 * <ol>
	 *     <li>Connect to the Lobby with {@link #connectLobby(long, String, BiConsumer)}
	 *     <li>Connect to the Lobby's networking layer with {@link #connectNetwork(long)}
	 *     <li>Open some channels with {@link #openNetworkChannel(long, byte, boolean)}
	 *     <li>Send messages with {@link #sendNetworkMessage(long, long, byte, byte[])}
	 *     <li>Repeatedly flush the networking layer with {@link #flushNetwork()}
	 *     <li>When you are done, disconnect from the networking layer with {@link #disconnectNetwork(long)}
	 *     <li>Finally, disconnect from the Lobby with {@link #disconnectLobby(long)}
	 * </ol>
	 * See <a href="https://discord.com/developers/docs/game-sdk/lobbies#integrated-networking">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#integrated-networking</a> and
	 * <a href="https://discord.com/developers/docs/game-sdk/lobbies#example-networking-the-easy-way">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#example-networking-the-easy-way</a>
	 * for more details and examples.
	 * @param lobbyId ID of the Lobby of the member and where the networking happens
	 * @param userId User ID of the member/receiver of the message
	 * @param channelId ID of the opened channel to send the message on
	 * @param data The message
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #sendNetworkMessage(Lobby, long, byte, byte[])
	 * @see #openNetworkChannel(long, byte, boolean)
	 * @see #flushNetwork()
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#sendnetworkmessage">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#sendnetworkmessage</a>
	 */
	public void sendNetworkMessage(long lobbyId, long userId, byte channelId, byte[] data)
	{
		Result result = core.execute(()->sendNetworkMessage(pointer, lobbyId, userId, channelId, data, 0, data.length));
		if(result != Result.OK)
			throw new GameSDKException(result);
	}

	/**
	 * Sends a network message to a given member of a given Lobby on a given channel.
	 * A message is received in {@link DiscordEventAdapter#onNetworkMessage(long, long, byte, byte[])}.
	 * <p>
	 * You need to open a channel before sending messages over it
	 * (see {@link #openNetworkChannel(Lobby, byte, boolean)}).
	 * Also make sure to call {@link #flushNetwork()} somewhere
	 * or your messages won't be sent.
	 * <p>
	 * Basic networking structure:
	 * <ol>
	 *     <li>Connect to the Lobby with {@link #connectLobby(Lobby, BiConsumer)}
	 *     <li>Connect to the Lobby's networking layer with {@link #connectNetwork(Lobby)}
	 *     <li>Open some channels with {@link #openNetworkChannel(Lobby, byte, boolean)}
	 *     <li>Send messages with {@link #sendNetworkMessage(Lobby, long, byte, byte[])}
	 *     <li>Repeatedly flush the networking layer with {@link #flushNetwork()}
	 *     <li>When you are done, disconnect from the networking layer with {@link #disconnectNetwork(Lobby)}
	 *     <li>Finally, disconnect from the Lobby with {@link #disconnectLobby(Lobby)}
	 * </ol>
	 * See <a href="https://discord.com/developers/docs/game-sdk/lobbies#integrated-networking">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#integrated-networking</a> and
	 * <a href="https://discord.com/developers/docs/game-sdk/lobbies#example-networking-the-easy-way">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#example-networking-the-easy-way</a>
	 * for more details and examples.
	 * <p>
	 * This method simply obtains the ID of the given Lobby with {@link Lobby#getId()}.
	 * @param lobby The Lobby of the member and where the networking happens
	 * @param userId User ID of the member/receiver of the message
	 * @param channelId ID of the opened channel to send the message on
	 * @param data The message
	 * @throws GameSDKException for a {@link Result} that is not {@link Result#OK}
	 * @see #sendNetworkMessage(long, long, byte, byte[])
	 * @see #openNetworkChannel(Lobby, byte, boolean)
	 * @see #flushNetwork()
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#sendnetworkmessage">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#sendnetworkmessage</a>
	 */
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
