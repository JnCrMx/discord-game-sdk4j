package de.jcm.discordgamesdk.user;

import com.google.gson.annotations.SerializedName;

/**
 * <p>Representation of a Discord user.</p>
 * <p>Only used as output from SDK functions.</p>
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/users#data-models-user-struct">
 *     https://discordapp.com/developers/docs/game-sdk/users#data-models-user-struct</a>
 */
public class DiscordUser
{
	@SerializedName("id")
	private final String userId;

	private final String username;
	private final String discriminator;
	private final String avatar;
	private final String avatar_decoration;

	private final Boolean bot;
	private final Integer flags;

	/**
	 * <p>Create a new Discord user object, holding the data provided as arguments.</p>
	 * <p>You probably do <b>not</b> want to construct the class, since it is mainly used for output.</p>
	 * @param userId ID of the user, a Discord snowflake
	 * @param username Discord-Name
	 * @param discriminator Discord-Tag
	 * @param avatar Resource key of the user's avator
	 * @param bot {@code true} if the user is a bot
	 */
	public DiscordUser(long userId, String username, String discriminator, String avatar, Boolean bot)
	{
		this.userId = Long.toString(userId);
		this.username = username;
		this.discriminator = discriminator;
		this.avatar = avatar;
		this.avatar_decoration = null;
		this.bot = bot;
		this.flags = null;
	}
	/**
	 * <p>Create a new Discord user object used to request a user by UID.</p>
	 * <p>You probably do <b>not</b> want to construct the class, since it is mainly used for output.</p>
	 * @param userId ID of the user, a Discord snowflake
	 */
	public DiscordUser(long userId)
	{
		this(userId, null, null, null, null);
	}

	/**
	 * Returns the ID of the Discord user.
	 * @return A Discord snowflake
	 */
	public long getUserId()
	{
		return Long.parseLong(userId);
	}

	/**
	 * Returns the Discord-Name of the user.
	 * @return The Discord-Name
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * Returns the Discord-Tag of the user.
	 * @return The Discord-Tag
	 */
	public String getDiscriminator()
	{
		return discriminator;
	}

	/**
	 * <p>Returns the resource key to the users avatar.</p>
	 * <p>You can find the avatar image at:<br>
	 *     https://cdn.discordapp.com/avatars/&lt;user id&gt;/&lt;resource key&gt;.png</p>
	 * @return A resource key
	 */
	public String getAvatar()
	{
		return avatar;
	}

	/**
	 * Tells you if the user is a bot.
	 * @return {@code true} if the user is a bot
	 */
	public boolean isBot()
	{
		return bot;
	}

	public int getFlags()
	{
		return flags;
	}

	/**
	 * <p>Generates a string representation of the user containing all its attributes.</p>
	 * <p>This is just one of <i>IntelliJ IDEA</i>'s default {@code toString()}-Methods,
	 * so don't expect anything special.</p>
	 *
	 * @return A string representation of the user
	 */
	@Override
	public String toString()
	{
		return "DiscordUser{" +
				"userId='" + userId + '\'' +
				", username='" + username + '\'' +
				", discriminator='" + discriminator + '\'' +
				", avatar='" + avatar + '\'' +
				", avatar_decoration='" + avatar_decoration + '\'' +
				", bot=" + bot +
				", flags=" + flags +
				'}';
	}
}
