package de.jcm.discordgamesdk.impl;

import com.google.gson.JsonElement;

public class Command
{
	public enum Type
	{
		DISPATCH,
		SUBSCRIBE,
		GET_USER,
		GET_RELATIONSHIPS
	}

	public enum Event
	{
		ACTIVITY_INVITE,
		ACTIVITY_JOIN,
		ACTIVITY_JOIN_REQUEST,
		ACTIVITY_SPECTATE,
		CURRENT_USER_UPDATE,
		LOBBY_DELETE,
		LOBBY_MEMBER_CONNECT,
		LOBBY_MEMBER_DISCONNECT,
		LOBBY_MEMBER_UPDATE,
		LOBBY_MESSAGE,
		LOBBY_UPDATE,
		OVERLAY_UPDATE,
		READY,
		RELATIONSHIP_UPDATE,
		SPEAKING_START,
		SPEAKING_STOP
	}

	private Type cmd;
	private JsonElement data;

	private JsonElement args;
	private Event evt;
	private String nonce;

	public Type getCmd()
	{
		return cmd;
	}

	public void setCmd(Type cmd)
	{
		this.cmd = cmd;
	}

	public JsonElement getData()
	{
		return data;
	}

	public void setData(JsonElement data)
	{
		this.data = data;
	}

	public JsonElement getArgs()
	{
		return args;
	}

	public void setArgs(JsonElement arg)
	{
		this.args = arg;
	}

	public Event getEvent()
	{
		return evt;
	}

	public void setEvt(Event evt)
	{
		this.evt = evt;
	}

	public String getNonce()
	{
		return nonce;
	}

	public void setNonce(String nonce)
	{
		this.nonce = nonce;
	}

	@Override
	public String toString()
	{
		return "Command{" +
				"cmd=" + cmd +
				", data=" + data +
				", args=" + args +
				", evt=" + evt +
				", nonce='" + nonce + '\'' +
				'}';
	}
}
