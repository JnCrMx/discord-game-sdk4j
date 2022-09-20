package de.jcm.discordgamesdk.activity;

/**
 * A structure containing secrets used to handle and display join and spectate options.
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/activities#data-models-activitysecrets-struct">
 *     https://discordapp.com/developers/docs/game-sdk/activities#data-models-activitysecrets-struct</a>
 */
public class ActivitySecrets
{
	private String match;
	private String join;
	private String spectate;


	/**
	 * Sets the unique secret for the match context (whatever that is).
	 * @param secret A unique secret,
	 */
	public void setMatchSecret(String secret)
	{
		this.match = secret;
	}
	/**
	 * Gets the unique secret for the match context (whatever that is).
	 * @return The unique secret or an empty string if it is not set
	 */
	public String getMatchSecret()
	{
		return match;
	}

	/**
	 * Sets a unique secret for join request and invites.
	 * @param secret A unique secret
	 */
	public void setJoinSecret(String secret)
	{
		this.join = secret;
	}

	/**
	 * Gets the unique join secret.
	 * @return The unique secret or an empty string if it is not set
	 */
	public String getJoinSecret()
	{
		return join;
	}

	/**
	 * Sets a unique secret for spectate option and invites.
	 * @param secret A unique secret
	 */
	public void setSpectateSecret(String secret)
	{
		this.spectate = secret;
	}
	/**
	 * Gets the unique spectate secret.
	 * @return The unique secret or an empty string if it is not set
	 */
	public String getSpectateSecret()
	{
		return spectate;
	}
}
