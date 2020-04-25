package de.jcm.discordgamesdk.activity;

/**
 * A structure used to display information about the size (current and max) of the player's party.
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/activities#data-models-partysize-struct">
 *     https://discordapp.com/developers/docs/game-sdk/activities#data-models-partysize-struct</a>
 */
public class ActivityPartySize
{
	private final long pointer;

	ActivityPartySize(long pointer)
	{
		this.pointer = pointer;
	}

	/**
	 * Sets the current size of the player's party (how full the party is).
	 * @param size The current size
	 */
	public void setCurrentSize(int size)
	{
		setCurrentSize(pointer, size);
	}

	/**
	 * Gets the current size of the player's party (how full the party is).
	 * @return The current size
	 */
	public int getCurrentSize()
	{
		return getCurrentSize(pointer);
	}

	/**
	 * Sets the maximal size of the player's party (how many people fit in the party).
	 * @param size The maximal size
	 */
	public void setMaxSize(int size)
	{
		setMaxSize(pointer, size);
	}

	/**
	 * Gets the maximal size of the player's party (how many people fit in the party).
	 * @return The maximal size
	 */
	public int getMaxSize()
	{
		return getMaxSize(pointer);
	}

	private native void setCurrentSize(long pointer, int size);
	private native int getCurrentSize(long pointer);
	private native void setMaxSize(long pointer, int size);
	private native int getMaxSize(long pointer);
}
