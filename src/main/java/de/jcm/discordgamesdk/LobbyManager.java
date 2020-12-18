package de.jcm.discordgamesdk;

import de.jcm.discordgamesdk.lobby.Lobby;
import de.jcm.discordgamesdk.lobby.LobbyTransaction;

import java.util.function.BiConsumer;

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

	public void createLobby(LobbyTransaction transaction, BiConsumer<Result, Lobby> callback)
	{
		createLobby(pointer, transaction.getPointer(), callback);
	}

	private native Object getLobbyCreateTransaction(long pointer);

	private native void createLobby(long pointer, long transactionPointer, BiConsumer<Result, Lobby> callback);
}
