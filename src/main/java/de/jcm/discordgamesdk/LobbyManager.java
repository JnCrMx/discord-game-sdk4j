package de.jcm.discordgamesdk;

import de.jcm.discordgamesdk.lobby.Lobby;
import de.jcm.discordgamesdk.lobby.LobbySearchQuery;
import de.jcm.discordgamesdk.lobby.LobbyTransaction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LobbyManager
{
	private final long pointer;

	LobbyManager(long pointer)
	{
		this.pointer = pointer;
	}

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

	public LobbyTransaction getLobbyUpdateTransaction(Lobby lobby)
	{
		return getLobbyUpdateTransaction(lobby.getId());
	}

	public void createLobby(LobbyTransaction transaction, @NotNull BiConsumer<Result, Lobby> callback)
	{
		createLobby(pointer, transaction.getPointer(), callback);
	}
	public void createLobby(LobbyTransaction transaction, @NotNull Consumer<Lobby> callback)
	{
		createLobby(transaction, (result, lobby) ->
		{
			Core.DEFAULT_CALLBACK.accept(result);
			callback.accept(lobby);
		});
	}

	public void updateLobby(long lobbyId, LobbyTransaction transaction, @NotNull Consumer<Result> callback)
	{
		updateLobby(pointer, lobbyId, transaction.getPointer(), callback);
	}
	public void updateLobby(long lobbyId, LobbyTransaction transaction)
	{
		updateLobby(lobbyId, transaction, Core.DEFAULT_CALLBACK);
	}
	public void updateLobby(Lobby lobby, LobbyTransaction transaction, @NotNull Consumer<Result> callback)
	{
		updateLobby(lobby.getId(), transaction, callback);
	}
	public void updateLobby(Lobby lobby, LobbyTransaction transaction)
	{
		updateLobby(lobby, transaction, Core.DEFAULT_CALLBACK);
	}

	public void deleteLobby(long lobbyId, @NotNull Consumer<Result> callback)
	{
		deleteLobby(pointer, lobbyId, callback);
	}
	public void deleteLobby(long lobbyId)
	{
		deleteLobby(pointer, lobbyId, Core.DEFAULT_CALLBACK);
	}
	public void deleteLobby(Lobby lobby, @NotNull Consumer<Result> callback)
	{
		deleteLobby(lobby.getId(), callback);
	}
	public void deleteLobby(Lobby lobby)
	{
		deleteLobby(lobby, Core.DEFAULT_CALLBACK);
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

	public void connectLobby(long lobbyId, String secret, @NotNull BiConsumer<Result, Lobby> callback)
	{
		connectLobby(pointer, lobbyId, secret, callback);
	}

	public void disconnectLobby(long lobbyId, Consumer<Result> callback)
	{
		disconnectLobby(pointer, lobbyId, callback);
	}
	public void disconnectLobby(long lobbyId)
	{
		disconnectLobby(lobbyId, Core.DEFAULT_CALLBACK);
	}
	public void disconnectLobby(Lobby lobby, Consumer<Result> callback)
	{
		disconnectLobby(lobby.getId(), callback);
	}
	public void disconnectLobby(Lobby lobby)
	{
		disconnectLobby(lobby, Core.DEFAULT_CALLBACK);
	}

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

	public String getLobbyMetadataValue(Lobby lobby, String key)
	{
		return getLobbyMetadataValue(lobby.getId(), key);
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

	private native Object getLobbyCreateTransaction(long pointer);
	private native Object getLobbyUpdateTransaction(long pointer, long lobbyId);

	private native void createLobby(long pointer, long transactionPointer, BiConsumer<Result, Lobby> callback);
	private native void updateLobby(long pointer, long lobbyId, long transactionPointer, Consumer<Result> callback);
	private native void deleteLobby(long pointer, long lobbyId, Consumer<Result> callback);

	private native void connectLobby(long pointer, long lobbyId, String secret, BiConsumer<Result, Lobby> callback);
	private native void connectLobbyWithActivitySecret(long pointer, String activitySecret, BiConsumer<Result, Lobby> callback);
	private native void disconnectLobby(long pointer, long lobbyId, Consumer<Result> callback);

	private native Object getLobby(long pointer, long lobbyId);

	private native Object getLobbyMetadataValue(long pointer, long lobbyId, String key);

	private native Object getSearchQuery(long pointer);
	private native void search(long pointer, long searchQueryPointer, Consumer<Result> callback);
	private native int lobbyCount(long pointer);
	private native Object getLobbyId(long pointer, int index);

	private native void connectVoice(long pointer, long lobbyId, Consumer<Result> callback);
	private native void disconnectVoice(long pointer, long lobbyId, Consumer<Result> callback);
}
