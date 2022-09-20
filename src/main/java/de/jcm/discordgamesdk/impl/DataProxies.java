package de.jcm.discordgamesdk.impl;

import de.jcm.discordgamesdk.activity.Activity;
import de.jcm.discordgamesdk.activity.ActivityType;
import de.jcm.discordgamesdk.user.*;

import java.util.Optional;

public class DataProxies
{
	public static class EmojiImpl
	{
		private String name;
	}

	public static class ActivityImpl
	{
		private String created_at;
		private EmojiImpl emoji;
		private String id;
		private String name;
		private String state;
		private int type;

		public Activity toActivity()
		{
			Activity activity = new Activity();
			if(state != null) activity.setState(state.substring(0, Math.min(state.length(), 126)));
			activity.setType(ActivityType.values()[type]);
			return activity;
		}
	}

	public static class PresenceImpl
	{
		private String status;
		private ActivityImpl activity;

		public Presence toPresence()
		{
			OnlineStatus s;
			switch(status)
			{
				case "dnd":
					s = OnlineStatus.DO_NO_DISTURB;
					break;
				case "idle":
					s = OnlineStatus.IDLE;
					break;
				case "online":
					s = OnlineStatus.ONLINE;
					break;
				case "offline":
				default:
					s = OnlineStatus.OFFLINE;
					break;
			}

			return new Presence(s, Optional.ofNullable(activity).map(ActivityImpl::toActivity).orElse(null));
		}
	}

	public static class RelationshipImpl
	{
		public int type;
		public DiscordUser user;
		public PresenceImpl presence;

		public Relationship toRelationship()
		{
			return new Relationship(
					RelationshipType.values()[type],
					user,
					presence.toPresence()
			);
		}
	}
}
