package de.jcm.discordgamesdk.impl.commands;

public class OpenOverlayGuildInvite
{
	private OpenOverlayGuildInvite() {}

	public static class Args
	{
		private final String code;
		private final int pid;

		public Args(String code, int pid)
		{
			this.code = code;
			this.pid = pid;
		}
	}
}
