package de.jcm.discordgamesdk.lobby;

import de.jcm.discordgamesdk.LobbyManager;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * The type or "privacy setting" of a {@link Lobby}.
 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#data-models-lobbytype-enum">
 *     https://discord.com/developers/docs/game-sdk/lobbies#data-models-lobbytype-enum</a>
 */
public enum LobbyType
{
	/**
	 * A private lobby which cannot be found by a {@link LobbyManager#search(LobbySearchQuery, Consumer)}.
	 * It can only be joined directly; either by its ID and secret or by the Activity Secret,
	 * which have to be obtained from somewhere else.
	 * @see LobbyManager#connectLobby(long, String, BiConsumer)
	 * @see LobbyManager#connectLobbyWithActivitySecret(String, BiConsumer)
	 */
	PRIVATE,
	/**
	 * A public Lobby which can be found by a {@link LobbyManager#search(LobbySearchQuery, Consumer)}.
	 * The search result then provides all available information (via {@link LobbyManager#getLobby(long)}
	 * including the information required to join the Lobby (ID and secret).
	 * @see LobbyManager#search(LobbySearchQuery)
	 */
	PUBLIC
}
