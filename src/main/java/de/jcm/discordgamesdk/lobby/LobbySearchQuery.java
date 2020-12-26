package de.jcm.discordgamesdk.lobby;

import de.jcm.discordgamesdk.GameSDKException;
import de.jcm.discordgamesdk.Result;

public class LobbySearchQuery
{
	public enum Comparison
	{
		LESS_THAN_OR_EQUAL,
		LESS_THAN,
		EQUAL,
		GREATER_THAN,
		GREATER_THAN_OR_EQUA,
		NOT_EQUAL
		;

		private static final int OFFSET = -2;

		private int nativeValue()
		{
			return ordinal() + OFFSET;
		}
	}

	public enum Cast
	{
		STRING,
		NUMBER
		;

		private static final int OFFSET = 1;

		public int nativeValue()
		{
			return ordinal() + OFFSET;
		}
	}

	public enum Distance
	{
		LOCAL,
		DEFAULT,
		EXTENDED,
		GLOBAL
		;

		private static final int OFFSET = 0;

		public int nativeValue()
		{
			return ordinal() + OFFSET;
		}
	}

	private final long pointer;

	LobbySearchQuery(long pointer)
	{
		this.pointer = pointer;
	}

	public long getPointer()
	{
		return pointer;
	}

	public LobbySearchQuery filter(String key, Comparison comparison, Cast cast, String value)
	{
		Result result = filter(pointer, key, comparison.nativeValue(), cast.nativeValue(), value);
		if(result != Result.OK)
		{
			throw new GameSDKException(result);
		}
		return this;
	}

	public LobbySearchQuery sort(String key, Cast cast, String baseValue)
	{
		Result result = sort(pointer, key, cast.nativeValue(), baseValue);
		if(result != Result.OK)
		{
			throw new GameSDKException(result);
		}
		return this;
	}

	public LobbySearchQuery limit(int limit)
	{
		Result result = limit(pointer, limit);
		if(result != Result.OK)
		{
			throw new GameSDKException(result);
		}
		return this;
	}

	public LobbySearchQuery distance(Distance distance)
	{
		Result result = distance(pointer, distance.nativeValue());
		if(result != Result.OK)
		{
			throw new GameSDKException(result);
		}
		return this;
	}

	private native Result filter(long pointer, String key, int comparison, int cast, String value);
	private native Result sort(long pointer, String key, int cast, String baseValue);
	private native Result limit(long pointer, int limit);
	private native Result distance(long pointer, int distance);
}
