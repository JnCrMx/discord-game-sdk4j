package de.jcm.discordgamesdk.activity;

/**
 * A structure containing secrets used to handle and display join and spectate options.
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/activities#data-models-activitysecrets-struct">
 *     https://discordapp.com/developers/docs/game-sdk/activities#data-models-activitysecrets-struct</a>
 */
public class ActivitySecrets
{
	private final long pointer;

	ActivitySecrets(long pointer)
	{
		this.pointer = pointer;
	}

	/**
	 * Sets the unique secret for the match context (whatever that is).
	 * @param secret A unique secret, max 127 characters
	 * @throws IllegalArgumentException if {@code secret} is too long
	 */
	public void setMatchSecret(String secret)
	{
		if(secret.getBytes().length>=128)
			throw new IllegalArgumentException("max length is 128");
		setMatchSecret(pointer, secret);
	}
	/**
	 * Gets the unique secret for the match context (whatever that is).
	 * @return The unique secret or an empty string if it is not set
	 */
	public String getMatchSecret()
	{
		return getMatchSecret(pointer);
	}

	/**
	 * Sets a unique secret for join request and invites.
	 * @param secret A unique secret, max 127 characters
	 * @throws IllegalArgumentException if {@code secret} is too long
	 */
	public void setJoinSecret(String secret)
	{
		if(secret.getBytes().length>=128)
			throw new IllegalArgumentException("max length is 128");
		setJoinSecret(pointer, secret);
	}

	/**
	 * Gets the unique join secret.
	 * @return The unique secret or an empty string if it is not set
	 */
	public String getJoinSecret()
	{
		return getJoinSecret(pointer);
	}

	/**
	 * Sets a unique secret for spectate option and invites.
	 * @param secret A unique secret, max 127 characters
	 * @throws IllegalArgumentException if {@code secret} is too long
	 */
	public void setSpectateSecret(String secret)
	{
		if(secret.getBytes().length>=128)
			throw new IllegalArgumentException("max length is 128");
		setSpectateSecret(pointer, secret);
	}
	/**
	 * Gets the unique spectate secret.
	 * @return The unique secret or an empty string if it is not set
	 */
	public String getSpectateSecret()
	{
		return getSpectateSecret(pointer);
	}

	private native void setMatchSecret(long pointer, String secret);
	private native String getMatchSecret(long pointer);

	private native void setJoinSecret(long pointer, String secret);
	private native String getJoinSecret(long pointer);

	private native void setSpectateSecret(long pointer, String secret);
	private native String getSpectateSecret(long pointer);
}
