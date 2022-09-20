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
	private String large_image;
	private String large_text;
	private String small_image;
	private String small_text;

	/**
	 * <p>Sets the asset to be displayed as the large image.</p>
	 * <p>Upload assets at the Art Assets configuration page of your Discord application:<br>
	 *     https://discordapp.com/developers/applications/&lt;your application id&gt;/rich-presence/assets</p>
	 * @param assetKey An asset key
	 */
	public void setLargeImage(String assetKey)
	{
		this.large_image = assetKey;
	}

	/**
	 * Gets the asset key of the large image.
	 * @return The asset key or an empty string if it is not set
	 */
	public String getLargeImage()
	{
		return large_image;
	}

	/**
	 * <p>Sets the tooltip text (displayed on hover) for the large image.</p>
	 * @param text A text
	 */
	public void setLargeText(String text)
	{
		this.large_text = text;
	}

	/**
	 * Gets the tooltip text for the large image.
	 * @return The tooltip text or an empty string if it is not set
	 */
	public String getLargeText()
	{
		return large_text;
	}

	/**
	 * <p>Sets the asset to be displayed as the small image.</p>
	 * <p>Upload assets at the Art Assets configuration page of your Discord application:<br>
	 *     https://discordapp.com/developers/applications/&lt;your application id&gt;/rich-presence/assets</p>
	 * @param assetKey An asset key
	 */
	public void setSmallImage(String assetKey)
	{
		this.small_image = assetKey;
	}

	/**
	 * Gets the asset key of the small image.
	 * @return The asset key or an empty string if it is not set
	 */
	public String getSmallImage()
	{
		return small_image;
	}

	/**
	 * <p>Sets the tooltip text (displayed on hover) for the small image.</p>
	 * @param text A text
	 */
	public void setSmallText(String text)
	{
		this.small_text = text;
	}

	/**
	 * Gets the tooltip text for the small image.
	 * @return The tooltip text or an empty string if it is not set
	 */
	public String getSmallText()
	{
		return small_text;
	}

	@Override
	public String toString()
	{
		return "ActivityAssets{" +
				"large_image='" + large_image + '\'' +
				", large_text='" + large_text + '\'' +
				", small_image='" + small_image + '\'' +
				", small_text='" + small_text + '\'' +
				'}';
	}
}
