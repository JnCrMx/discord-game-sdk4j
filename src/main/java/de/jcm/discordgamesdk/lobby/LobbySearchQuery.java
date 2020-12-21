package de.jcm.discordgamesdk.lobby;

public class LobbySearchQuery
{
	private final long pointer;

	LobbySearchQuery(long pointer)
	{
		this.pointer = pointer;
	}

	public long getPointer()
	{
		return pointer;
	}
}
