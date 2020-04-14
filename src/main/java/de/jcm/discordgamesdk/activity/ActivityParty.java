package de.jcm.discordgamesdk.activity;

/**
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/activities#data-models-activityparty-struct">
 *     https://discordapp.com/developers/docs/game-sdk/activities#data-models-activityparty-struct</a>
 */
public class ActivityParty
{
	private long pointer;

	private ActivityPartySize size;

	ActivityParty(long pointer)
	{
		this.pointer = pointer;

		this.size = new ActivityPartySize(getSize(pointer));
	}

	public void setID(String id)
	{
		if(id.getBytes().length>=128)
			throw new IllegalArgumentException("max length is 127");
		setID(pointer, id);
	}

	public String getID()
	{
		return getID(pointer);
	}

	public ActivityPartySize size()
	{
		return size;
	}

	private native void setID(long pointer, String id);
	private native String getID(long pointer);

	private native long getSize(long pointer);
}
