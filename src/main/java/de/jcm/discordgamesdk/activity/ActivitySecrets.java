package de.jcm.discordgamesdk.activity;

/**
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/activities#data-models-activitysecrets-struct">
 *     https://discordapp.com/developers/docs/game-sdk/activities#data-models-activitysecrets-struct</a>
 */
public class ActivitySecrets
{
	private long pointer;

	ActivitySecrets(long pointer)
	{
		this.pointer = pointer;
	}

	public void setMatchSecret(String secret)
	{
		if(secret.getBytes().length>=128)
			throw new IllegalArgumentException("max length is 128");
		setMatchSecret(pointer, secret);
	}
	public String getMatchSecret()
	{
		return getMatchSecret(pointer);
	}

	public void setJoinSecret(String secret)
	{
		if(secret.getBytes().length>=128)
			throw new IllegalArgumentException("max length is 128");
		setJoinSecret(pointer, secret);
	}
	public String getJoinSecret()
	{
		return getJoinSecret(pointer);
	}

	public void setSpectateSecret(String secret)
	{
		if(secret.getBytes().length>=128)
			throw new IllegalArgumentException("max length is 128");
		setSpectateSecret(pointer, secret);
	}
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
