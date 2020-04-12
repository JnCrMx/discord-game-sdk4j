package de.jcm.discordgamesdk.activity;

public class ActivityAssets
{
	private long pointer;

	public ActivityAssets(long pointer)
	{
		this.pointer = pointer;
	}

	public void setLargeImage(String assetKey)
	{
		if(assetKey.getBytes().length>=128)
			throw new IllegalArgumentException("max length is 127");
		setLargeImage(pointer, assetKey);
	}
	public String getLargeImage()
	{
		return getLargeImage(pointer);
	}

	public void setLargeText(String text)
	{
		if(text.getBytes().length>=128)
			throw new IllegalArgumentException("max length is 127");
		setLargeText(pointer, text);
	}
	public String getLargeText()
	{
		return getLargeText(pointer);
	}


	public void setSmallImage(String assetKey)
	{
		if(assetKey.getBytes().length>=128)
			throw new IllegalArgumentException("max length is 127");
		setSmallImage(pointer, assetKey);
	}
	public String getSmallImage()
	{
		return getSmallImage(pointer);
	}

	public void setSmallText(String text)
	{
		if(text.getBytes().length>=128)
			throw new IllegalArgumentException("max length is 127");
		setSmallText(pointer, text);
	}
	public String getSmallText()
	{
		return getSmallText(pointer);
	}

	private native void setLargeImage(long pointer, String assetKey);
	private native String getLargeImage(long pointer);

	private native void setLargeText(long pointer, String text);
	private native String getLargeText(long pointer);


	private native void setSmallImage(long pointer, String assetKey);
	private native String getSmallImage(long pointer);

	private native void setSmallText(long pointer, String text);
	private native String getSmallText(long pointer);
}
