package de.jcm.discordgamesdk.lobby;

import de.jcm.discordgamesdk.GameSDKException;
import de.jcm.discordgamesdk.Result;

public class LobbyMemberTransaction
{
	private final long pointer;

	LobbyMemberTransaction(long pointer)
	{
		this.pointer = pointer;
	}

	public long getPointer()
	{
		return pointer;
	}

	public void setMetadata(String key, String value)
	{
		if(key.getBytes().length >= 256)
			throw new IllegalArgumentException("max key length is 255");
		if(value.getBytes().length >= 4095)
			throw new IllegalArgumentException("max value length is 4096");

		Result result = setMetadata(pointer, key, value);
		if(result != Result.OK)
		{
			throw new GameSDKException(result);
		}
	}

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
