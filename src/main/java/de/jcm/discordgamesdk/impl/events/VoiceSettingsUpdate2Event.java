package de.jcm.discordgamesdk.impl.events;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.impl.Command;
import de.jcm.discordgamesdk.impl.EventHandler;
import de.jcm.discordgamesdk.voice.VoiceInputMode;

import java.util.List;
import java.util.Map;

public class VoiceSettingsUpdate2Event
{
	public static class Data
	{
		private VoiceInputMode input_mode;
		private List<String> local_mutes;
		private Map<String, Integer> local_volumes;
		private boolean self_mute;
		private boolean self_deaf;

		public VoiceInputMode getInputMode()
		{
			return input_mode;
		}

		public List<String> getLocalMutes()
		{
			return local_mutes;
		}

		public Map<String, Integer> getLocalVolumes()
		{
			return local_volumes;
		}

		public boolean isSelfMute()
		{
			return self_mute;
		}

		public boolean isSelfDeaf()
		{
			return self_deaf;
		}
	}

	public static class Handler extends EventHandler<Data>
	{
		public Handler(Core.CorePrivate core)
		{
			super(core);
		}

		@Override
		public void handle(Command command, Data data)
		{
			core.voiceData = data;
		}

		@Override
		public Class<?> getDataClass()
		{
			return Data.class;
		}
	}
}
