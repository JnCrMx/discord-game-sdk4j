package de.jcm.discordgamesdk;

import de.jcm.discordgamesdk.user.DiscordUser;
import de.jcm.discordgamesdk.user.PremiumType;

import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * Manager to fetch information about Discord users, especially the current user.
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/users">
 *     https://discordapp.com/developers/docs/game-sdk/users</a>
 */
public class UserManager
{
	private final long pointer;
	private final Core core;

	/**
	 * Discord Partner
	 * @see #currentUserHasFlag(int)
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/users#data-models-userflag-enum">
	 *     https://discordapp.com/developers/docs/game-sdk/users#data-models-userflag-enum</a>
	 */
	public static final int USER_FLAG_PARTNER = 2;
	/**
	 * HypeSquad Events participant
	 * @see #currentUserHasFlag(int)
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/users#data-models-userflag-enum">
	 *     https://discordapp.com/developers/docs/game-sdk/users#data-models-userflag-enum</a>
	 */
	public static final int USER_FLAG_HYPE_SQUAD_EVENTS = 4;
	/**
	 * HypeSquad House Bravery
	 * @see #currentUserHasFlag(int)
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/users#data-models-userflag-enum">
	 *     https://discordapp.com/developers/docs/game-sdk/users#data-models-userflag-enum</a>
	 */
	public static final int USER_FLAG_HYPE_SQUAD_HOUSE1 = 64;
	/**
	 * HypeSquad House Brilliance
	 * @see #currentUserHasFlag(int)
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/users#data-models-userflag-enum">
	 *     https://discordapp.com/developers/docs/game-sdk/users#data-models-userflag-enum</a>
	 */
	public static final int USER_FLAG_HYPE_SQUAD_HOUSE2 = 128;
	/**
	 * HypeSquad House Balance
	 * @see #currentUserHasFlag(int)
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/users#data-models-userflag-enum">
	 *     https://discordapp.com/developers/docs/game-sdk/users#data-models-userflag-enum</a>
	 */
	public static final int USER_FLAG_HYPE_SQUAD_HOUSE3 = 256;

	UserManager(long pointer, Core core)
	{
		this.pointer = pointer;
		this.core = core;
	}

	/**
	 * <p>Fetches information about the current user.</p>
	 * <p>You need to wait for a {@link DiscordEventAdapter#onCurrentUserUpdate()} to be fired,
	 * before using this method.</p>
	 * @return The current DiscordUser
	 * @throws GameSDKException if something went wrong fetching the user information
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/users#getcurrentuser">
	 *     https://discordapp.com/developers/docs/game-sdk/users#getcurrentuser</a>
	 */
	public DiscordUser getCurrentUser()
	{
		Object ret = core.execute(()->getCurrentUser(pointer));
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			return (DiscordUser) ret;
		}
	}

	/**
	 * <p>Fetches information about a Discord user.</p>
	 * <p>The user is provided to the callback together with the result of the operation.</p>
	 * @param userId ID of the user to fetch information of
	 * @param callback Callback to provide the result to
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/users#getuser">
	 *     https://discordapp.com/developers/docs/game-sdk/users#getuser</a>
	 */
	public void getUser(long userId, BiConsumer<Result, DiscordUser> callback)
	{
		core.execute(()->getUser(pointer, userId, Objects.requireNonNull(callback)));
	}

	/**
	 * <p>Fetches the type of premium subscription the current user has.</p>
	 * @return The PremiumType
	 * @throws GameSDKException if something went wrong fetching the user information
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/users#getcurrentuserpremiumtype">
	 *     https://discordapp.com/developers/docs/game-sdk/users#getcurrentuserpremiumtype</a>
	 */
	public PremiumType getCurrentUserPremiumType()
	{
		Object ret = core.execute(()->getCurrentUserPremiumType(pointer));
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			return PremiumType.values()[(Integer) ret];
		}
	}

	/**
	 * <p>Checks if the current user has a certain flag set.</p>
	 * @param flag Flag to check the user for
	 * @return {@code true}, if the user has the flag set, {@code false} otherwise
	 * @throws GameSDKException if something went wrong fetching the user information
	 * @see #USER_FLAG_PARTNER
	 * @see #USER_FLAG_HYPE_SQUAD_EVENTS
	 * @see #USER_FLAG_HYPE_SQUAD_HOUSE1
	 * @see #USER_FLAG_HYPE_SQUAD_HOUSE2
	 * @see #USER_FLAG_HYPE_SQUAD_HOUSE3
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/users#currentuserhasflag">
	 *     https://discordapp.com/developers/docs/game-sdk/users#currentuserhasflag</a>
	 */
	public boolean currentUserHasFlag(int flag)
	{
		Object ret = core.execute(()->currentUserHasFlag(pointer, flag));
		if(ret instanceof Result)
		{
			throw new GameSDKException((Result) ret);
		}
		else
		{
			return (Boolean) ret;
		}
	}

	private native Object getCurrentUser(long pointer);
	private native void getUser(long pointer, long userId, BiConsumer<Result, DiscordUser> callback);
	private native Object getCurrentUserPremiumType(long pointer);
	private native Object currentUserHasFlag(long pointer, int flag);
}
