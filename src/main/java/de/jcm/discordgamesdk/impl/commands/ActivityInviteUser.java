package de.jcm.discordgamesdk.impl.commands;

public class ActivityInviteUser
{
	private ActivityInviteUser() {}

	public static class Args
	{
		private final int type;
		private final String user_id;
		private final String content;
		private final int pid;

		public Args(int type, String user_id, String content, int pid)
		{
			this.type = type;
			this.user_id = user_id;
			this.content = content;
			this.pid = pid;
		}
	}
}
