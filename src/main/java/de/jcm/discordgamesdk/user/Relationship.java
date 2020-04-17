package de.jcm.discordgamesdk.user;

import de.jcm.discordgamesdk.activity.Activity;

import java.lang.ref.*;
import java.util.ArrayList;

/**
 * The relationship between a user and the current user.
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/relationships#data-models-relationship-struct">
 *     https://discordapp.com/developers/docs/game-sdk/relationships#data-models-relationship-struct</a>
 */
public class Relationship
{
	private RelationshipType type;
	private DiscordUser user;
	private Presence presence;

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

	/*
	This seems to work for freeing allocated native space of Activity,
	but I'm somehow sure this is REALLY wrong.
	 */
	private static final ReferenceQueue<Relationship> QUEUE = new ReferenceQueue<>();
	private static final ArrayList<RelationshipReference> REFERENCES = new ArrayList<>();
	private static final Thread QUEUE_THREAD = new Thread(()->{
		while(true)
		{
			try
			{
				RelationshipReference reference = (RelationshipReference) QUEUE.remove();
				reference.getActivity().close();
				REFERENCES.remove(reference);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}, "Relationship-Cleaner");
	static
	{
		QUEUE_THREAD.start();
	}

	private static class RelationshipReference extends PhantomReference<Relationship>
	{
		private Activity activity;

		public RelationshipReference(Relationship referent, ReferenceQueue<? super Relationship> q)
		{
			super(referent, q);
			this.activity = referent.getPresence().getActivity();
		}

		public Activity getActivity()
		{
			return activity;
		}
	}

	static Relationship createRelationship(int type, DiscordUser user, int status, long activity)
	{
		RelationshipType type1 = RelationshipType.values()[type];
		OnlineStatus status1 = OnlineStatus.values()[status];

		Activity activity1 = new Activity(activity);

		Presence presence = new Presence(status1, activity1);
		Relationship relationship = new Relationship(type1, user, presence);

		RelationshipReference reference = new RelationshipReference(relationship, QUEUE);
		REFERENCES.add(reference);

		return relationship;
	}
}
