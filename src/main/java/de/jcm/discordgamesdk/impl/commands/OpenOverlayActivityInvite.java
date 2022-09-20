package de.jcm.discordgamesdk.impl.commands;

public class OpenOverlayActivityInvite
{
	private OpenOverlayActivityInvite() {}

	public static class Args
	{
		private final int type;
		private final int pid;

		public Args(int type, int pid)
		{
			this.type = type;
			this.pid = pid;
		}
	}
}
