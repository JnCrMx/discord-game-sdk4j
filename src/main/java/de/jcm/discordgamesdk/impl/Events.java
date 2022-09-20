package de.jcm.discordgamesdk.impl;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.impl.events.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class Events
{
	private final Core.CorePrivate core;
	public Events(Core.CorePrivate core)
	{
		this.core = core;
		registerEvents();
	}

	private void registerEvents()
	{
		handlers.put(Command.Event.READY, new ReadyEvent.Handler(core));

		handlers.put(Command.Event.CURRENT_USER_UPDATE, new CurrentUserUpdateEvent.Handler(core));
		handlers.put(Command.Event.OVERLAY_UPDATE, new OverlayUpdateEvent.Handler(core));
		handlers.put(Command.Event.RELATIONSHIP_UPDATE, new RelationshipUpdateEvent.Handler(core));
		handlers.put(Command.Event.VOICE_SETTINGS_UPDATE_2, new VoiceSettingsUpdate2Event.Handler(core));
	}

	private final Map<Command.Event, EventHandler<?>> handlers = new HashMap<>();
	public EventHandler<?> forEvent(Command.Event e)
	{
		return handlers.get(e);
	}

	public <T> void register(Command.Event event, Class<T> clazz, Consumer<T> consumer)
	{
		handlers.put(event, new EventHandler<T>(core)
		{
			@Override
			public void handle(Command command, T t)
			{
				consumer.accept(t);
			}

			@Override
			public Class<?> getDataClass()
			{
				return clazz;
			}
		});
	}

	public Set<Map.Entry<Command.Event, EventHandler<?>>> getEventTypes()
	{
		return handlers.entrySet();
	}
}
