package de.jcm.discordgamesdk.impl.commands;

import de.jcm.discordgamesdk.impl.DataProxies;

import java.util.List;

public class GetRelationships
{
	private GetRelationships() {}

	public static class Response
	{
		private List<DataProxies.RelationshipImpl> relationships;

		public List<DataProxies.RelationshipImpl> getRelationships()
		{
			return relationships;
		}
	}
}
