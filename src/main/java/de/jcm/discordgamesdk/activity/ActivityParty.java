package de.jcm.discordgamesdk.activity;

/**
 * A structure used show information about the player's party.
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/activities#data-models-activityparty-struct">
 *     https://discordapp.com/developers/docs/game-sdk/activities#data-models-activityparty-struct</a>
 */
public class ActivityParty
{
	private final long pointer;

	private final ActivityPartySize size;

	ActivityParty(long pointer)
	{
		this.pointer = pointer;

		this.size = new ActivityPartySize(getSize(pointer));
	}

	/**
	 * Sets an unique identifier for the party.
	 * @param id a unique identifier, max 127 characters
	 * @throws IllegalArgumentException if {@code id} is too long
	 */
	public void setID(String id)
	{
		if(id.getBytes().length>=128)
			throw new IllegalArgumentException("max length is 127");
		setID(pointer, id);
	}

	/**
	 * Gets the unique identifier set for the party.
	 * @return The unique identifier or an empty string if it is not set
	 */
	public String getID()
	{
		return getID(pointer);
	}

	/**
	 * Returns an embedded structure to set the size of the party.
	 * @return An ActivityPartySize structure
	 */
	public ActivityPartySize size()
	{
		return size;
	}

	private native void setID(long pointer, String id);
	private native String getID(long pointer);

	private native long getSize(long pointer);
}
