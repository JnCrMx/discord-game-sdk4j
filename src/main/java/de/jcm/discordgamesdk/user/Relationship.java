package de.jcm.discordgamesdk.user;

import de.jcm.discordgamesdk.activity.Activity;

/**
 * The relationship between a user and the current user.
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/relationships#data-models-relationship-struct">
 *     https://discordapp.com/developers/docs/game-sdk/relationships#data-models-relationship-struct</a>
 */
public class Relationship
{
	private final RelationshipType type;
	private final DiscordUser user;
	private final Presence presence;

	private Relationship(RelationshipType type, DiscordUser user, Presence presence)
	{
		this.type = type;
		this.user = user;
		this.presence = presence;
	}

	/**
	 * Gets the type of the relationship between the user and the current user.
	 * @return The type of the relationship
	 */
	public RelationshipType getType()
	{
		return type;
	}

	/**
	 * Gets the user the current user has the relationship with.
	 * @return Another user
	 */
	public DiscordUser getUser()
	{
		return user;
	}

	/**
	 * Gets the Presence of the user which the current user has an relationship with.
	 * @return The user's presence.
	 */
	public Presence getPresence()
	{
		return presence;
	}

	/**
	 * <p>Generates a string representation of the relationship containing all its attributes.</p>
	 * <p>This is just one of <i>IntelliJ IDEA</i>'s default {@code toString()}-Methods,
	 * so don't expect anything special.</p>
	 * @return A string representation of the relationship
	 */
	@Override
	public String toString()
	{
		return "Relationship{" +
				"type=" + type +
				", user=" + user +
				", presence=" + presence +
				'}';
	}

	static Relationship createRelationship(int type, DiscordUser user, int status, long activity)
	{
		RelationshipType type1 = RelationshipType.values()[type];
		OnlineStatus status1 = OnlineStatus.values()[status];

		Activity activity1 = new Activity(activity);

		Presence presence = new Presence(status1, activity1);

		return new Relationship(type1, user, presence);
	}
}
