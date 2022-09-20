package de.jcm.discordgamesdk.activity;

import de.jcm.discordgamesdk.user.Presence;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;

/**
 * Java representation of the Activity structure.
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/activities#data-models-activity-struct">
 *     https://discordapp.com/developers/docs/game-sdk/activities#data-models-activity-struct</a>
 */
public class Activity implements AutoCloseable
{
	private Long applicationId;
	private String name;
	private ActivityType type;

	private String state;
	private String details;
	private boolean instance;

	private final ActivityTimestamps timestamps;
	private final ActivityAssets assets;
	private final ActivityParty party;
	private final ActivitySecrets secrets;

	/**
	 * Allocates a new Activity structure.
	 */
	public Activity()
	{
		this.timestamps = new ActivityTimestamps();
		this.assets = new ActivityAssets();
		this.party = new ActivityParty();
		this.secrets = new ActivitySecrets();
	}

	/**
	 * <p>Gets the application ID of the Activity.</p>
	 * <p>This is a <i>read-only</i> property. You are only gonna use it
	 * if you acquire the Activity from a {@link Presence}.</p>
	 * @return The application ID
	 */
	public long getApplicationId()
	{
		return applicationId;
	}

	/**
	 * <p>Gets the name of the Activity.</p>
	 * <p>This is a <i>read-only</i> property. You are only gonna use it
	 * if you acquire the Activity from a {@link Presence}.</p>
	 * @return The name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets the player's current party status.
	 * @param state Current party status
	 */
	public void setState(String state)
	{
		this.state = state;
	}

	/**
	 * Gets the player's current party status.
	 * @return Current party status or an empty string if none is set
	 */
	public String getState()
	{
		return state;
	}

	/**
	 * Sets what the player is currently doing.
	 * @param details What the player is currently doing
	 */
	public void setDetails(String details)
	{
		this.details = details;
	}

	/**
	 * Gets what the player is currently doing.
	 * @return What the player is currently doing or an empty string if it is not set
	 */
	public String getDetails()
	{
		return details;
	}

	/**
	 * <p>Sets the type of the Activity.</p>
	 * <p>Only for event handling. Discord will ignore this field.</p>
	 * @param type Activity type
	 * @see <a href="https://discordapp.com/developers/docs/topics/gateway#activity-object-activity-types">
	 *     https://discordapp.com/developers/docs/topics/gateway#activity-object-activity-types</a>
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/activities#data-models-activitytype-enum">
	 *     https://discordapp.com/developers/docs/game-sdk/activities#data-models-activitytype-enum</a>
	 */
	public void setType(ActivityType type)
	{
		this.type = type;
	}
	/**
	 * <p>Gets the type of the Activity.</p>
	 * <p>Only for event handling. Discord will ignore this field.</p>
	 * @return Activity type
	 * @see <a href="https://discordapp.com/developers/docs/topics/gateway#activity-object-activity-types">
	 *     https://discordapp.com/developers/docs/topics/gateway#activity-object-activity-types</a>
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/activities#data-models-activitytype-enum">
	 *     https://discordapp.com/developers/docs/game-sdk/activities#data-models-activitytype-enum</a>
	 */
	public ActivityType getType()
	{
		return type;
	}

	/**
	 * <p>Returns the embedded ActivityTimestamps structures.</p>
	 * <p>The purpose of this structure is creating a "time left"-field.</p>
	 * @return An ActivityTimestamps structure
	 */
	public ActivityTimestamps timestamps()
	{
		return timestamps;
	}
	/**
	 * <p>Returns the embedded ActivityAssets structures.</p>
	 * <p>The purpose of this structure is attaching images to the activity.</p>
	 * @return An ActivityAssets structure
	 */
	public ActivityAssets assets()
	{
		return assets;
	}
	/**
	 * <p>Returns the embedded ActivityParty structures.</p>
	 * <p>The purpose of this structure is creating a "(a of b)"-field
	 * showing how many people are in the player's party.</p>
	 * @return An ActivityParty structure
	 */
	public ActivityParty party()
	{
		return party;
	}
	/**
	 * <p>Returns the embedded ActivitySecrets structures.</p>
	 * <p>The purpose of this structure is storing secrets used in and enabling the
	 * "Ask to join"- and "Spectate"-buttons as well as the
	 * "Invite ... to play ..."- and "Invite ... to spectate ..."-options.</p>
	 * @return An ActivityParty structure
	 */
	public ActivitySecrets secrets()
	{
		return secrets;
	}

	/**
	 * Sets whether the player is in an instance
	 * @param instance whether the player is in an instance
	 */
	public void setInstance(boolean instance)
	{
		this.instance = instance;
	}
	/**
	 * Gets whether the player is in an instance
	 * @return {@code true} if the player is in an instance
	 */
	public boolean getInstance()
	{
		return instance;
	}

	/**
	 * <p>Frees the allocated native structure and therefore also all embedded native structures.</p>
	 * <p>You should call this when you do not need the structure anymore.</p>
	 */
	@Override
	public void close()
	{
	}

	@Override
	public String toString()
	{
		return "Activity{" +
				"applicationId=" + applicationId +
				", name='" + name + '\'' +
				", type=" + type +
				", state='" + state + '\'' +
				", details='" + details + '\'' +
				", instance=" + instance +
				", timestamps=" + timestamps +
				", assets=" + assets +
				", party=" + party +
				", secrets=" + secrets +
				'}';
	}
}
