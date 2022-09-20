package de.jcm.discordgamesdk.impl.commands;

public class OpenOverlayVoiceSettings
{
	private OpenOverlayVoiceSettings() {}

	public static class Args
	{
		private final int pid;

		public Args(int pid)
		{
			this.pid = pid;
		}
	}
}
