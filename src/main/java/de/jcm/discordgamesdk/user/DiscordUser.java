package de.jcm.discordgamesdk.user;

/**
 * <p>Representation of a Discord user.</p>
 * <p>Only used as output from SDK functions.</p>
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/users#data-models-user-struct">
 *     https://discordapp.com/developers/docs/game-sdk/users#data-models-user-struct</a>
 */
public class DiscordUser
{
	private final long userId;
	private final String username;
	private final String discriminator;
	private final String avatar;
	private final boolean bot;

	/**
	 * <p>Create a new Discord user object, holding the data provided as arguments.</p>
	 * <p>You probably do <b>not</b> want to construct the class, since it is mainly used for output.</p>
	 * @param userId ID of the user, a Discord snowflake
	 * @param username Discord-Name
	 * @param discriminator Discord-Tag
	 * @param avatar Resource key of the user's avator
	 * @param bot {@code true} if the user is a bot
	 */
	public DiscordUser(long userId, String username, String discriminator, String avatar, boolean bot)
	{
		this.userId = userId;
		this.username = username;
		this.discriminator = discriminator;
		this.avatar = avatar;
		this.bot = bot;
	}

	/**
	 * Returns the ID of the Discord user.
	 * @return A Discord snowflake
	 */
	public long getUserId()
	{
		return userId;
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

	/**
	 * <p>Generates a string representation of the user containing all its attributes.</p>
	 * <p>This is just one of <i>IntelliJ IDEA</i>'s default {@code toString()}-Methods,
	 * so don't expect anything special.</p>
	 * @return A string representation of the user
	 */
	@Override
	public String toString()
	{
		return "DiscordUser{" +
				"userId=" + userId +
				", username='" + username + '\'' +
				", discriminator='" + discriminator + '\'' +
				", avatar='" + avatar + '\'' +
				", bot=" + bot +
				'}';
	}
}
