package de.jcm.discordgamesdk.impl.commands;

import de.jcm.discordgamesdk.impl.Command;

public class Subscribe
{
	public static class Response
	{
		private Command.Event evt;

		public Command.Event getEvent()
		{
			return evt;
		}
	}
}
