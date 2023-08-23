package de.jcm.discordgamesdk.activity;

/**
 * <p>A structure used for custom buttons</p>
 * <p>This structure is not documented in the Game SDK, but is documented in the Gateway API</p>
 * @see <a href="https://discord.com/developers/docs/topics/gateway-events#activity-object-activity-buttons">
 *  *     https://discord.com/developers/docs/topics/gateway-events#activity-object-activity-buttons</a>
 */
public class ActivityButton {
    private String label;
    private String url;

    public ActivityButton()
    {
    }

    /**
     * Construct a new ActivityButton
     * @param label The text displayed on the button
     * @param url Link that is clicked to
     */
    public ActivityButton(String label, String url)
    {
        this.label = label;
        this.url = url;
    }

    /**
     * Get a label
     * @return button label
     */
    public String getLabel()
    {
        return label;
    }

    /**
     * Set a label
     * @param label The text displayed on the button
     */
    public void setLabel(String label)
    {
        this.label = label;
    }

    /**
     * Get a url
     * @return button url
     */
    public String getUrl()
    {
        return url;
    }

    /**
     * Set a url
     * @param url Link that is clicked to
     */
    public void setUrl(String url)
    {
        this.url = url;
    }
}
