package de.jcm.discordgamesdk.impl;

public class HandshakeMessage
{
	private final int v;
	private final String client_id;

	public int getVersion()
	{
		return v;
	}

	public String getClientID()
	{
		return client_id;
	}

	public HandshakeMessage(String client_id)
	{
		this.v = 1;
		this.client_id = client_id;
	}

	public HandshakeMessage(int version, String client_id)
	{
		this.v = version;
		this.client_id = client_id;
	}
}
