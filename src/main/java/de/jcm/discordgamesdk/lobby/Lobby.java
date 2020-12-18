package de.jcm.discordgamesdk.lobby;

public class Lobby
{
	private long id;
	private LobbyType type;
	private long ownerId;
	private String secret;
	private int capacity;
	private boolean locked;

	public Lobby(long id, int type, long ownerId, String secret, int capacity, boolean locked)
	{
		this.id = id;
		this.type = LobbyType.values()[type-1];
		this.ownerId = ownerId;
		this.secret = secret;
		this.capacity = capacity;
		this.locked = locked;
	}

	public long getId()
	{
		return id;
	}

	public LobbyType getType()
	{
		return type;
	}

	public long getOwnerId()
	{
		return ownerId;
	}

	public String getSecret()
	{
		return secret;
	}

	public int getCapacity()
	{
		return capacity;
	}

	public boolean isLocked()
	{
		return locked;
	}

	@Override
	public String toString()
	{
		return "Lobby{" +
				"id=" + id +
				", type=" + type +
				", ownerId=" + ownerId +
				", secret='" + secret + '\'' +
				", capacity=" + capacity +
				", locked=" + locked +
				'}';
	}
}
