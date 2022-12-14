package de.jcm.discordgamesdk.impl.events;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.impl.Command;
import de.jcm.discordgamesdk.impl.DataProxies;
import de.jcm.discordgamesdk.impl.EventHandler;

public class RelationshipUpdateEvent
{
	public static class Handler extends EventHandler<DataProxies.RelationshipImpl>
	{
		public Handler(Core.CorePrivate core)
		{
			super(core);
		}

		@Override
		public void handle(Command command, DataProxies.RelationshipImpl data)
		{
			core.relationships.put(data.user.getUserId(), data.toRelationship());
			core.getEventAdapter().onRelationshipUpdate(data.toRelationship());
		}

		@Override
		public Class<?> getDataClass()
		{
			return DataProxies.RelationshipImpl.class;
		}
	}
}
