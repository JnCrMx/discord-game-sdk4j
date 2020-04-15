package de.jcm.discordgamesdk;

public class DiscordUser
{
	private long userId;
	private String username;
	private String discriminator;
	private String avatar;
	private boolean bot;

	public DiscordUser(long userId, String username, String discriminator, String avatar, boolean bot)
	{
		this.userId = userId;
		this.username = username;
		this.discriminator = discriminator;
		this.avatar = avatar;
		this.bot = bot;
	}

	public long getUserId()
	{
		return userId;
	}

	public String getUsername()
	{
		return username;
	}

	public String getDiscriminator()
	{
		return discriminator;
	}

	public String getAvatar()
	{
		return avatar;
	}

	public boolean isBot()
	{
		return bot;
	}

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
