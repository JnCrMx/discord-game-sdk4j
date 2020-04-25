package de.jcm.discordgamesdk.activity;

/**
 * <p>A structure used for images (assets) attached to activities.</p>
 * <p>Have a look at Discord's Rich Presence Visualizer for more clues:<br>
 *     https://discordapp.com/developers/applications/&lt;your application id&gt;/rich-presence/visualizer</p>
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/activities#data-models-activityassets-struct">
 *     https://discordapp.com/developers/docs/game-sdk/activities#data-models-activityassets-struct</a>
 */
public class ActivityAssets
{
	private final long pointer;

	ActivityAssets(long pointer)
	{
		this.pointer = pointer;
	}

	/**
	 * <p>Sets the asset to be displayed as the large image.</p>
	 * <p>Upload assets at the Art Assets configuration page of your Discord application:<br>
	 *     https://discordapp.com/developers/applications/&lt;your application id&gt;/rich-presence/assets</p>
	 * @param assetKey An asset key, max 127 characters
	 * @throws IllegalArgumentException if {@code assetKey} is too long
	 */
	public void setLargeImage(String assetKey)
	{
		if(assetKey.getBytes().length>=128)
			throw new IllegalArgumentException("max length is 127");
		setLargeImage(pointer, assetKey);
	}

	/**
	 * Gets the asset key of the large image.
	 * @return The asset key or an empty string if it is not set
	 */
	public String getLargeImage()
	{
		return getLargeImage(pointer);
	}

	/**
	 * <p>Sets the tooltip text (displayed on hover) for the large image.</p>
	 * @param text A text, max 127 characters
	 * @throws IllegalArgumentException if {@code text} is too long
	 */
	public void setLargeText(String text)
	{
		if(text.getBytes().length>=128)
			throw new IllegalArgumentException("max length is 127");
		setLargeText(pointer, text);
	}

	/**
	 * Gets the tooltip text for the large image.
	 * @return The tooltip text or an empty string if it is not set
	 */
	public String getLargeText()
	{
		return getLargeText(pointer);
	}

	/**
	 * <p>Sets the asset to be displayed as the small image.</p>
	 * <p>Upload assets at the Art Assets configuration page of your Discord application:<br>
	 *     https://discordapp.com/developers/applications/&lt;your application id&gt;/rich-presence/assets</p>
	 * @param assetKey An asset key, max 127 characters
	 * @throws IllegalArgumentException if {@code assetKey} is too long
	 */
	public void setSmallImage(String assetKey)
	{
		if(assetKey.getBytes().length>=128)
			throw new IllegalArgumentException("max length is 127");
		setSmallImage(pointer, assetKey);
	}

	/**
	 * Gets the asset key of the small image.
	 * @return The asset key or an empty string if it is not set
	 */
	public String getSmallImage()
	{
		return getSmallImage(pointer);
	}

	/**
	 * <p>Sets the tooltip text (displayed on hover) for the small image.</p>
	 * @param text A text, max 127 characters
	 * @throws IllegalArgumentException if {@code text} is too long
	 */
	public void setSmallText(String text)
	{
		if(text.getBytes().length>=128)
			throw new IllegalArgumentException("max length is 127");
		setSmallText(pointer, text);
	}

	/**
	 * Gets the tooltip text for the small image.
	 * @return The tooltip text or an empty string if it is not set
	 */
	public String getSmallText()
	{
		return getSmallText(pointer);
	}

	private native void setLargeImage(long pointer, String assetKey);
	private native String getLargeImage(long pointer);

	private native void setLargeText(long pointer, String text);
	private native String getLargeText(long pointer);


	private native void setSmallImage(long pointer, String assetKey);
	private native String getSmallImage(long pointer);

	private native void setSmallText(long pointer, String text);
	private native String getSmallText(long pointer);
}
