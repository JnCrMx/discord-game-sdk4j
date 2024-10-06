package de.jcm.discordgamesdk.activity;

/**
 * A structure used show information about the player's party.
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/activities#data-models-activityparty-struct">
 *     https://discordapp.com/developers/docs/game-sdk/activities#data-models-activityparty-struct</a>
 */
public class ActivityParty
{
	private String id;
	private int[] size;

	ActivityParty()
	{
		this.size = null;
	}

	/**
	 * Sets an unique identifier for the party.
	 * @param id a unique identifier
	 */
	public void setID(String id)
	{
		this.id = id;
	}

	/**
	 * Gets the unique identifier set for the party.
	 * @return The unique identifier or an empty string if it is not set
	 */
	public String getID()
	{
		return id;
	}

	/**
	 * Returns an embedded structure to set the size of the party.
	 * @return An ActivityPartySize structure
	 */
	public ActivityPartySize size()
	{
		if(size == null)
		{
			this.size = new int[2];
		}
		return new ActivityPartySize(size);
	}

	@Override
	public String toString()
	{
		return "ActivityParty{" +
				"id='" + id + '\'' +
				", size=" + size() +
				'}';
	}
}
