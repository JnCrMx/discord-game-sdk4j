package de.jcm.discordgamesdk.impl.commands;

import de.jcm.discordgamesdk.activity.Activity;

public class SetActivity
{
	private SetActivity() {}
	public static class Args
	{
		private int pid;
		private Activity activity;

		public Args(int pid, Activity activity)
		{
			this.pid = pid;
			this.activity = activity;
		}
	}
}
