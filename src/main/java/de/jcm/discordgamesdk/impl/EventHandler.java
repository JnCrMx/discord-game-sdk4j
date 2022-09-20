package de.jcm.discordgamesdk.impl;

import de.jcm.discordgamesdk.Core;

public abstract class EventHandler<Data>
{
	protected final Core.CorePrivate core;

	protected EventHandler(Core.CorePrivate core)
	{
		this.core = core;
	}

	public abstract void handle(Command command, Data data);

	public final void handleObject(Command command, Object o)
	{
		handle(command, (Data)o);
	}
	public abstract Class<?> getDataClass();

	public boolean shouldRegister() { return true; }
	public Object getRegisterArgs() { return null; }
}
