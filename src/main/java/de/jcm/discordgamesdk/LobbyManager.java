package de.jcm.discordgamesdk;

import de.jcm.discordgamesdk.lobby.Lobby;
import de.jcm.discordgamesdk.lobby.LobbyMemberTransaction;
import de.jcm.discordgamesdk.lobby.LobbySearchQuery;
import de.jcm.discordgamesdk.lobby.LobbyTransaction;
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
	public LobbyMemberTransaction getMemberUpdateTransaction(Lobby lobby, long userId)
	{
		return getMemberUpdateTransaction(lobby.getId(), userId);
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

	public void connectLobby(long lobbyId, String secret, @NotNull BiConsumer<Result, Lobby> callback)
	{
		if(secret.getBytes().length >= 128)
			throw new IllegalArgumentException("max secret length is 255");
		connectLobby(pointer, lobbyId, secret, callback);
	}

	public void connectLobbyWithActivitySecret(String activitySecret, @NotNull BiConsumer<Result, Lobby> callback)
	{
		if(activitySecret.getBytes().length >= 128)
			throw new IllegalArgumentException("max activity secret length is 255");
		connectLobbyWithActivitySecret(pointer, activitySecret, callback);
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
