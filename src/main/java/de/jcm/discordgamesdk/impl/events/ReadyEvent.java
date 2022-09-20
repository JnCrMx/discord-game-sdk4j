package de.jcm.discordgamesdk.impl.events;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.impl.Command;
import de.jcm.discordgamesdk.impl.EventHandler;
import de.jcm.discordgamesdk.user.DiscordUser;

public class ReadyEvent
{
	public static class Data
	{
		int v;
		Config config;
		DiscordUser user;

		static class Config
		{
			String cdn_host;
			String api_endpoint;
			String environment;

			@Override
			public String toString()
			{
				return "Config{" +
						"cdn_host='" + cdn_host + '\'' +
						", api_endpoint='" + api_endpoint + '\'' +
						", environment='" + environment + '\'' +
						'}';
			}
		}

		@Override
		public String toString()
		{
			return "ReadyData{" +
					"v=" + v +
					", config=" + config +
					", user=" + user +
					'}';
		}
	}

	public static class Handler extends EventHandler<Data>
	{
		public Handler(Core.CorePrivate core)
		{
			super(core);
		}

		@Override
		public void handle(Command command, Data data)
		{
			core.ready();
			core.currentUser = data.user;
			core.getEventAdapter().onCurrentUserUpdate();
		}

		@Override
		public Class<?> getDataClass()
		{
			return Data.class;
		}

		@Override
		public boolean shouldRegister()
		{
			return false;
		}
	}
}
