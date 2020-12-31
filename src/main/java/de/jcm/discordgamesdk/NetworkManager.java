package de.jcm.discordgamesdk;

public class NetworkManager
{
	private final long pointer;

	NetworkManager(long pointer)
	{
		this.pointer = pointer;
	}

	public long getPeerId()
	{
		return getPeerId(pointer);
	}

	public void flush()
	{
		Result result = flush(pointer);
		if(result != Result.OK)
			throw new GameSDKException(result);
	}

	public void openPeer(long peerId, String routeData)
	{
		Result result = openPeer(pointer, peerId, routeData);
		if(result != Result.OK)
			throw new GameSDKException(result);
	}

	public void updatePeer(long peerId, String routeData)
	{
		Result result = updatePeer(pointer, peerId, routeData);
		if(result != Result.OK)
			throw new GameSDKException(result);
	}

	public void closePeer(long peerId)
	{
		Result result = closePeer(pointer, peerId);
		if(result != Result.OK)
			throw new GameSDKException(result);
	}

	public void openChannel(long peerId, byte channelId, boolean reliable)
	{
		Result result = openChannel(pointer, peerId, channelId, reliable);
		if(result != Result.OK)
			throw new GameSDKException(result);
	}

	public void closeChannel(long peerId, byte channelId)
	{
		Result result = closeChannel(pointer, peerId, channelId);
		if(result != Result.OK)
			throw new GameSDKException(result);
	}

	public void sendMessage(long peerId, byte channelId, byte[] data)
	{
		Result result = sendMessage(pointer, peerId, channelId, data, 0, data.length);
		if(result != Result.OK)
			throw new GameSDKException(result);
	}

	private native long getPeerId(long pointer);

	private native Result flush(long pointer);

	private native Result openPeer(long pointer, long peerId, String routeData);
	private native Result updatePeer(long pointer, long peerId, String routeData);
	private native Result closePeer(long pointer, long peerId);

	private native Result openChannel(long pointer, long peerId, byte channelId, boolean reliable);
	private native Result closeChannel(long pointer, long peerId, byte channelId);
	private native Result sendMessage(long pointer, long peerId, byte channelId, byte[] data, int offset, int length);
}
