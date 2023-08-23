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

    public ActivityButton() {
    }

    public ActivityButton(String label, String url) {
        this.label = label;
        this.url = url;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
