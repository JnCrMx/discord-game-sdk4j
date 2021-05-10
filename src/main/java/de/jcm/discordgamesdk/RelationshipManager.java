package de.jcm.discordgamesdk;

import de.jcm.discordgamesdk.activity.ActivityType;
import de.jcm.discordgamesdk.user.OnlineStatus;
import de.jcm.discordgamesdk.user.Relationship;
import de.jcm.discordgamesdk.user.RelationshipType;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Manager to fetch information about the current user's relationships with other users.
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/relationships">
 *     https://discordapp.com/developers/docs/game-sdk/relationships</a>
 */
public class RelationshipManager
{
	/**
	 * Matches any relationship.
	 */
	public static final Predicate<Relationship> NO_FILTER = r -> true;
	/**
	 * Matches friends ({@link RelationshipType#FRIEND}):
	 */
	public static final Predicate<Relationship> FRIEND_FILTER = r -> r.getType()==RelationshipType.FRIEND;
	/**
	 * Matches online (not idle or DND) users ({@link OnlineStatus#ONLINE}).
	 */
	public static final Predicate<Relationship> ONLINE_FILTER = r -> r.getPresence().getStatus()==OnlineStatus.ONLINE;
	/**
	 * Matches offline (not idle or DND) users ({@link OnlineStatus#OFFLINE}).
	 */
	public static final Predicate<Relationship> OFFLINE_FILTER = r -> r.getPresence().getStatus()==OnlineStatus.OFFLINE;
	/**
	 * Matches users that are doing something special
	 * (playing, watching, listening, having a custom status, etc.).
	 */
	public static final Predicate<Relationship> SPECIAL_FILTER = r->
			(
					r.getPresence().getActivity().getType() == ActivityType.PLAYING &&
							r.getPresence().getActivity().getApplicationId() != 0
			) ||
					r.getPresence().getActivity().getType() != ActivityType.PLAYING;

	private final long pointer;
	private final Core core;

	RelationshipManager(long pointer, Core core)
	{
		this.pointer = pointer;
		this.core = core;
	}

	/**
	 * Fetches the relationship with another user.
	 * @param userId ID to identifier the other user
	 * @return The Relationship between the users
	 * @throws GameSDKException if something went wrong fetching the relationship information
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/relationships#get">
	 *     https://discordapp.com/developers/docs/game-sdk/relationships#get</a>
	 */
	public Relationship getWith(long userId)
	{
		Object ret = core.execute(()->get(pointer, userId));
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			return (Relationship) ret;
		}
	}

	/**
	 * Filters the user's relationship by a certain {@link Predicate}.
	 * @param filter {@link Predicate} to filter the relationships with.
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/relationships#filter">
	 *     https://discordapp.com/developers/docs/game-sdk/relationships#filter</a>
	 */
	public void filter(Predicate<Relationship> filter)
	{
		core.execute(()->filter(pointer, Objects.requireNonNull(filter)));
	}

	/**
	 * <p>Fetches the count of relationships that match your filter.</p>
	 * <p><i>Only</i> works if you called {@link #filter(Predicate)} first, returns
	 * {@link Result#NOT_FOUND} otherwise.</p>
	 * @return The count.
	 * @throws GameSDKException if something went wrong fetching the relationship count
	 * @see #getAt(int)
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/relationships#count">
	 *     https://discordapp.com/developers/docs/game-sdk/relationships#count</a>
	 */
	public int count()
	{
		Object ret = core.execute(()->count(pointer));
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			return (Integer) ret;
		}
	}

	/**
	 * <p>Fetches a relationships at a certain position in the filtered list.</p>
	 * <p><i>Only</i> works if you called {@link #filter(Predicate)} first, returns
	 * {@link Result#NOT_FILTERED} otherwise.</p>
	 * @param index The index of the relationships
	 * @return The relationships
	 * @throws GameSDKException if something went wrong fetching the relationship
	 * @see #count()
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/relationships#getat">
	 *     https://discordapp.com/developers/docs/game-sdk/relationships#getat</a>
	 */
	public Relationship getAt(int index)
	{
		Object ret = core.execute(()->getAt(pointer, index));
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			return (Relationship) ret;
		}
	}

	/**
	 * Fetches a filtered list of relationships.
	 * <p><i>Only</i> works if you called {@link #filter(Predicate)} first, throws a
	 * {@link GameSDKException} with {@link Result#NOT_FOUND} otherwise.</p>
	 * @return A list of relationships
	 * @throws GameSDKException if something went wrong fetching the relationship list
	 */
	public List<Relationship> asList()
	{
		int count = count();
		Relationship[] relationships = new Relationship[count];
		for(int i=0; i<relationships.length; i++)
		{
			relationships[i] = getAt(i);
		}
		return Arrays.asList(relationships);
	}

	private native void filter(long pointer, Predicate<Relationship> filter);
	private native Object count(long pointer);
	private native Object get(long pointer, long userId);
	private native Object getAt(long pointer, int index);
}
