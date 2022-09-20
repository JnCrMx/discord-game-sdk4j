package de.jcm.discordgamesdk.impl.events;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.impl.Command;
import de.jcm.discordgamesdk.impl.EventHandler;

public class OverlayUpdateEvent
{
	public static class Data
	{
		private boolean enabled;
		private boolean locked;

		public boolean isEnabled()
		{
			return enabled;
		}

		public boolean isLocked()
		{
			return locked;
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
			core.overlayData = data;
		}

		@Override
		public Class<?> getDataClass()
		{
			return Data.class;
		}

		private static class Args
		{
			private final int pid;

			public Args(int pid)
			{
				this.pid = pid;
			}
		};

		@Override
		public Object getRegisterArgs()
		{
			return new Args(core.pid);
		}
	}
}
