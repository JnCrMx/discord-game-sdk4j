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
	/*
	This seems to work for freeing allocated native space of Activity,
	but I'm somehow sure this is REALLY wrong.
	 */
	private static final ReferenceQueue<Activity> QUEUE = new ReferenceQueue<>();
	private static final ArrayList<ActivityReference> REFERENCES = new ArrayList<>();
	private static final Thread QUEUE_THREAD = new Thread(()->{
		while(true)
		{
			try
			{
				ActivityReference reference = (ActivityReference) QUEUE.remove();
				free(reference.pointer);
				REFERENCES.remove(reference);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}, "Activity-Cleaner");
	static
	{
		QUEUE_THREAD.setDaemon(true);
		QUEUE_THREAD.start();
	}

	private static class ActivityReference extends PhantomReference<Activity>
	{
		private final long pointer;

		public ActivityReference(Activity referent, ReferenceQueue<? super Activity> q)
		{
			super(referent, q);
			this.pointer = referent.pointer;
		}

		public long getPointer()
		{
			return pointer;
		}
	}

	private final long pointer;

	private final ActivityTimestamps timestamps;
	private final ActivityAssets assets;
	private final ActivityParty party;
	private final ActivitySecrets secrets;

	/**
	 * Allocates a new Activity structure.
	 */
	public Activity()
	{
		this.pointer = allocate();

		this.timestamps = new ActivityTimestamps(getTimestamps(pointer));
		this.assets = new ActivityAssets(getAssets(pointer));
		this.party = new ActivityParty(getParty(pointer));
		this.secrets = new ActivitySecrets(getSecrets(pointer));

		/*
		 * This constructor is only invoked from people using this library, not from the library itself.
		 * So, I don't think it's our job to clean up their closeables.
		 */
	}

	/**
	 * Parses the given pointer as an Activity.
	 * <p>This is <b>not</b> an API method. Do <b>not</b> call it.</p>
	 * @param pointer A native pointer
	 */
	public Activity(long pointer)
	{
		this.pointer = pointer;

		this.timestamps = new ActivityTimestamps(getTimestamps(pointer));
		this.assets = new ActivityAssets(getAssets(pointer));
		this.party = new ActivityParty(getParty(pointer));
		this.secrets = new ActivitySecrets(getSecrets(pointer));

		/*
		 * This constructor is ideally never invoked by users.
		 * Only the library uses it to wrap existing activity objects.
		 * So, it's our job to clean the allocated space.
		 */
		ActivityReference reference = new ActivityReference(this, QUEUE);
		REFERENCES.add(reference);
	}

	/**
	 * <p>Gets the application ID of the Activity.</p>
	 * <p>This is a <i>read-only</i> property. You are only gonna use it
	 * if you acquire the Activity from a {@link Presence}.</p>
	 * @return The application ID
	 */
	public long getApplicationId()
	{
		return getApplicationId(pointer);
	}

	/**
	 * <p>Gets the name of the Activity.</p>
	 * <p>This is a <i>read-only</i> property. You are only gonna use it
	 * if you acquire the Activity from a {@link Presence}.</p>
	 * @return The name
	 */
	public String getName()
	{
		return getName(pointer);
	}

	/**
	 * Sets the player's current party status.
	 * @param state Current party status, max 127 characters
	 * @throws IllegalArgumentException if {@code state} is too long
	 */
	public void setState(String state)
	{
		if(state.getBytes().length >= 128)
			throw new IllegalArgumentException("max length is 127");
		setState(pointer, state);
	}

	/**
	 * Gets the player's current party status.
	 * @return Current party status or an empty string if none is set
	 */
	public String getState()
	{
		return getState(pointer);
	}

	/**
	 * Sets what the player is currently doing.
	 * @param details What the player is currently doing, max 127 characters
	 * @throws IllegalArgumentException if {@code details} is too long
	 */
	public void setDetails(String details)
	{
		if(details.getBytes().length >= 128)
			throw new IllegalArgumentException("max length is 127");
		setDetails(pointer, details);
	}

	/**
	 * Gets what the player is currently doing.
	 * @return What the player is currently doing or an empty string if it is not set
	 */
	public String getDetails()
	{
		return getDetails(pointer);
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
		setType(pointer, type.ordinal());
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
		return ActivityType.values()[getType(pointer)];
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
		setInstance(pointer, instance);
	}
	/**
	 * Gets whether the player is in an instance
	 * @return {@code true} if the player is in an instance
	 */
	public boolean getInstance()
	{
		return getInstance(pointer);
	}

	private native long allocate();
	private static native void free(long pointer);

	private native long getApplicationId(long pointer);
	private native String getName(long pointer);

	private native void setState(long pointer, String state);
	private native String getState(long pointer);

	private native void setDetails(long pointer, String details);
	private native String getDetails(long pointer);

	private native void setType(long pointer, int type);
	private native int getType(long pointer);

	private native long getTimestamps(long pointer);
	private native long getAssets(long pointer);
	private native long getParty(long pointer);
	private native long getSecrets(long pointer);

	private native void setInstance(long pointer, boolean instance);
	private native boolean getInstance(long pointer);

	/**
	 * <p>Frees the allocated native structure and therefore also all embedded native structures.</p>
	 * <p>You should call this when you do not need the structure anymore.</p>
	 */
	@Override
	public void close()
	{
		free(pointer);
	}

	/**
	 * <p>Return the pointer to the native structure.</p>
	 * <p>This is <b>not</b> an API method. Do <b>not</b> call it.</p>
	 * @return A native pointer
	 */
	public long getPointer()
	{
		return pointer;
	}

	@Override
	public String toString()
	{
		return "Activity@"+pointer+"{" +
				"applicationId=" + getApplicationId() +
				", name = " + getName() +
				", state = " + getState() +
				", details = " + getDetails() +
				", type = " + getType() +
				", timestamps=" + timestamps() +
				", assets=" + assets() +
				", party=" + party() +
				", secrets=" + secrets() +
				'}';
	}
}
