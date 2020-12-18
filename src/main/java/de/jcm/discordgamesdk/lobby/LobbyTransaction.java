package de.jcm.discordgamesdk.lobby;

import de.jcm.discordgamesdk.GameSDKException;
import de.jcm.discordgamesdk.Result;

public class LobbyTransaction
{
	private final long pointer;

	LobbyTransaction(long pointer)
	{
		this.pointer = pointer;
	}

	public long getPointer()
	{
		return pointer;
	}

	public void setType(LobbyType type)
	{
		Result result = setType(pointer, type.ordinal()+1);
		if(result != Result.OK)
		{
			throw new GameSDKException(result);
		}
	}

	public void setOwner(long ownerId)
	{
		Result result = setOwner(pointer, ownerId);
		if(result != Result.OK)
		{
			throw new GameSDKException(result);
		}
	}

	public void setCapacity(int capacity)
	{
		Result result = setCapacity(pointer, capacity);
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
