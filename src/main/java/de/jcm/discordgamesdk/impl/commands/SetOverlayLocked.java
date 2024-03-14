package de.jcm.discordgamesdk.impl.commands;

public class SetOverlayLocked
{
	private SetOverlayLocked() {}

	public static class Args
	{
		private boolean locked;
		private int pid;

		public Args(boolean locked, int pid)
		{
			this.locked = locked;
			this.pid = pid;
		}
	}
}
