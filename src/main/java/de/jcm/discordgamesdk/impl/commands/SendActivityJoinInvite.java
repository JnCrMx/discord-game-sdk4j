package de.jcm.discordgamesdk.impl.commands;

public class SendActivityJoinInvite
{
	private SendActivityJoinInvite() {}

	public static class Args
	{
		private final String user_id;

		public Args(String user_id)
		{
			this.user_id = user_id;
		}
	}
}
