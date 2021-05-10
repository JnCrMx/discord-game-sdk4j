package de.jcm.discordgamesdk.lobby;

import de.jcm.discordgamesdk.GameSDKException;
import de.jcm.discordgamesdk.LobbyManager;
import de.jcm.discordgamesdk.Result;

import java.util.function.Consumer;

/**
 * A search query used to filter, sort and limit Lobbies in a {@link LobbyManager#search(LobbySearchQuery, Consumer)}.
 * <p>
 * An instance of this can only be obtained by {@link LobbyManager#getSearchQuery()}.
 * <p>
 * The instance should be used to do <i>only one</i> search an should be discarded after that.
 * <p>
 * Closing or freeing the instance does not seem to be required or even possible.
 * @see LobbyManager#getSearchQuery()
 * @see LobbyManager#search(LobbySearchQuery, Consumer) 
 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#data-models-lobbysearchquery-struct">
 *     https://discord.com/developers/docs/game-sdk/lobbies#data-models-lobbysearchquery-struct</a>
 */
public class LobbySearchQuery
{
	/**
	 * Comparison operators for filtering Lobbies.
	 * @see #filter(String, Comparison, Cast, String)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#data-models-lobbysearchcomparison-enum">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#data-models-lobbysearchcomparison-enum</a>
	 */
	public enum Comparison
	{
		/**
		 * {@code <=} comparison operator.
		 */
		LESS_THAN_OR_EQUAL,
		/**
		 * {@code <} comparison operator.
		 */
		LESS_THAN,
		/**
		 * {@code ==} comparison operator.
		 */
		EQUAL,
		/**
		 * {@code >} comparison operator.
		 */
		GREATER_THAN,
		/**
		 * {@code >=} comparison operator.
		 */
		GREATER_THAN_OR_EQUAL,
		/**
		 * {@code !=} comparison operator.
		 */
		NOT_EQUAL
		;

		private static final int OFFSET = -2;

		private int nativeValue()
		{
			return ordinal() + OFFSET;
		}
	}

	/**
	 * Hints on how a value should be interpreted for filtering and sorting.
	 * @see #filter(String, Comparison, Cast, String)
	 * @see #sort(String, Cast, String)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#data-models-lobbysearchcast-enum">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#data-models-lobbysearchcast-enum</a>
	 */
	public enum Cast
	{
		/**
		 * Interpret the comparing value as a string.
		 */
		STRING,
		/**
		 * Interpret the comparing value as a number.
		 */
		NUMBER
		;

		private static final int OFFSET = 1;

		private int nativeValue()
		{
			return ordinal() + OFFSET;
		}
	}

	/**
	 * Distance of a Lobby relative to the current user's own position.
	 * Used for filtering.
	 * @see #distance(Distance)
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#data-models-lobbysearchdistance-enum">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#data-models-lobbysearchdistance-enum</a>
	 */
	public enum Distance
	{
		/**
		 * Find Lobbies in the same region.
		 */
		LOCAL,
		/**
		 * Find Lobbies in the same region and in adjacent regions.
		 */
		DEFAULT,
		/**
		 * Find Lobbies that are 'far' away (e.g. EU to US).
		 */
		EXTENDED,
		/**
		 * Find Lobbies in all regions.
		 */
		GLOBAL
		;

		private static final int OFFSET = 0;

		private int nativeValue()
		{
			return ordinal() + OFFSET;
		}
	}

	private final long pointer;

	LobbySearchQuery(long pointer)
	{
		this.pointer = pointer;
	}

	/**
	 * Returns a pointer to the native structure backing this search query.
	 * <p>
	 * This method should <b>never</b> be called outside intern code.
	 * @return A native pointer.
	 */
	public long getPointer()
	{
		return pointer;
	}

	/**
	 * Filters Lobbies by their properties (including metadata).
	 * Only Lobbies that fulfill this comparison will appear in the search result.
	 * <p>
	 * Available keys:
	 * <ul>
	 *     <li>"{@code owner_id}" - User ID of the Lobby owner
	 *     <li>"{@code capacity}" - Capacity of the Lobby
	 *     <li>"{@code slots}" - ???
	 *     <li>"{@code metadata.}" - A metadata entry (e.g. "{@code metadata.matchmaking_rating}")
	 * </ul>
	 * <p>
	 * Metadata keys <b>need</b> to be prefixed with "{@code metadata.}" or it will not work.
	 * @param key The key to filter by, see the list above, max. 255 bytes
	 * @param comparison The comparison operator to use
	 * @param cast How to interpret the (metadata) value (as a string or as a number)
	 * @param value A value to compare against, max. 4095 bytes
	 * @return {@code this} for chained method calls.
	 * @throws IllegalArgumentException if key or value are too long
	 * @throws GameSDKException if anything goes wrong on the native side
	 * @see Comparison
	 * @see Cast
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#lobbysearchqueryfilter">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#lobbysearchqueryfilter</a>
	 */
	public LobbySearchQuery filter(String key, Comparison comparison, Cast cast, String value)
	{
		if(key.getBytes().length >= 256)
			throw new IllegalArgumentException("max key length is 255");
		if(value.getBytes().length >= 4096)
			throw new IllegalArgumentException("max value length is 4095");

		Result result = filter(pointer, key, comparison.nativeValue(), cast.nativeValue(), value);
		if(result != Result.OK)
		{
			throw new GameSDKException(result);
		}
		return this;
	}

	/**
	 * Sort the filtered Lobbies by their "near-ness" to a given value.
	 * <p>
	 * Available keys:
	 * <ul>
	 *     <li>"{@code owner_id}" - User ID of the Lobby owner
	 *     <li>"{@code capacity}" - Capacity of the Lobby
	 *     <li>"{@code slots}" - ???
	 *     <li>"{@code metadata.}" - A metadata entry (e.g. "{@code metadata.matchmaking_rating}")
	 * </ul>
	 * <p>
	 * Metadata keys <b>need</b> to be prefixed with "{@code metadata.}" or it will not work.
	 * @param key The key to filter by, see the list above, max. 255 bytes
	 * @param cast How to interpret the (metadata) value (as a string or as a number)
	 * @param baseValue Base value for the "near-ness", max. 4095 bytes
	 * @return {@code this} for chained method calls.
	 * @throws IllegalArgumentException if key or value are too long
	 * @throws GameSDKException if anything goes wrong on the native side
	 * @see Cast
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#lobbysearchquerysort">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#lobbysearchquerysort</a>
	 */
	public LobbySearchQuery sort(String key, Cast cast, String baseValue)
	{
		if(key.getBytes().length >= 256)
			throw new IllegalArgumentException("max key length is 255");

		Result result = sort(pointer, key, cast.nativeValue(), baseValue);
		if(result != Result.OK)
		{
			throw new GameSDKException(result);
		}
		return this;
	}

	/**
	 * Limits the number of returned Lobbies.
	 * @param limit Limit for the number of Lobbies to find.
	 * @return {@code this} for chained method calls.
	 * @throws GameSDKException if anything goes wrong on the native side
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#lobbysearchquerylimit">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#lobbysearchquerylimit</a>
	 */
	public LobbySearchQuery limit(int limit)
	{
		Result result = limit(pointer, limit);
		if(result != Result.OK)
		{
			throw new GameSDKException(result);
		}
		return this;
	}

	/**
	 * Filters Lobbies by their distance to the users current locations.
	 * @param distance The distance to search within.
	 * @return {@code this} for chained method calls.
	 * @throws GameSDKException if anything goes wrong on the native side
	 * @see Distance
	 * @see <a href="https://discord.com/developers/docs/game-sdk/lobbies#lobbysearchquerydistance">
	 *     https://discord.com/developers/docs/game-sdk/lobbies#lobbysearchquerydistance</a>
	 */
	public LobbySearchQuery distance(Distance distance)
	{
		Result result = distance(pointer, distance.nativeValue());
		if(result != Result.OK)
		{
			throw new GameSDKException(result);
		}
		return this;
	}

	private native Result filter(long pointer, String key, int comparison, int cast, String value);
	private native Result sort(long pointer, String key, int cast, String baseValue);
	private native Result limit(long pointer, int limit);
	private native Result distance(long pointer, int distance);
}
