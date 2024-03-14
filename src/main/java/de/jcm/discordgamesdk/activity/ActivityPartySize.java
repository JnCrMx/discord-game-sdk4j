package de.jcm.discordgamesdk.activity;

/**
 * A structure used to display information about the size (current and max) of the player's party.
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/activities#data-models-partysize-struct">
 *     https://discordapp.com/developers/docs/game-sdk/activities#data-models-partysize-struct</a>
 */
public class ActivityPartySize
{
	private final int[] size;

	ActivityPartySize(int[] size)
	{
		this.size = size;
	}

	/**
	 * Sets the current size of the player's party (how full the party is).
	 * @param size The current size
	 */
	public void setCurrentSize(int size)
	{
		this.size[0] = size;
	}

	/**
	 * Gets the current size of the player's party (how full the party is).
	 * @return The current size
	 */
	public int getCurrentSize()
	{
		return size[0];
	}

	/**
	 * Sets the maximal size of the player's party (how many people fit in the party).
	 * @param size The maximal size
	 */
	public void setMaxSize(int size)
	{
		this.size[1] = size;
	}

	/**
	 * Gets the maximal size of the player's party (how many people fit in the party).
	 * @return The maximal size
	 */
	public int getMaxSize()
	{
		return size[1];
	}

	@Override
	public String toString()
	{
		return "ActivityPartySize{" +
				"currentSize=" + size[0] +
				", maxSize=" + size[1] +
				'}';
	}
}
