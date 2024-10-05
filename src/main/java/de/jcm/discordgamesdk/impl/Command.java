package de.jcm.discordgamesdk.impl;

import com.google.gson.JsonElement;

public class Command
{
	public enum Type
	{
		ACTIVITY_INVITE_USER,
		CLOSE_ACTIVITY_JOIN_REQUEST,
		DISPATCH,
		GET_IMAGE,
		GET_NETWORKING_CONFIG,
		GET_RELATIONSHIPS,
		GET_USER,
		OPEN_OVERLAY_ACTIVITY_INVITE,
		OPEN_OVERLAY_GUILD_INVITE,
		OPEN_OVERLAY_VOICE_SETTINGS,
		SEND_ACTIVITY_JOIN_INVITE,
		SET_ACTIVITY,
		SET_OVERLAY_LOCKED,
		SUBSCRIBE,
		AUTHENTICATE
	}

	public enum Event
	{
		ACTIVITY_INVITE,
		ACTIVITY_JOIN,
		ACTIVITY_JOIN_REQUEST,
		ACTIVITY_SPECTATE,
		CURRENT_USER_UPDATE,
		ERROR,
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
		SPEAKING_STOP,
		VOICE_SETTINGS_UPDATE_2,
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

	public boolean isError() {
		return getEvent() == Event.ERROR;
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
