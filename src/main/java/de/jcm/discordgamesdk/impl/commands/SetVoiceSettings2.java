package de.jcm.discordgamesdk.impl.commands;

import de.jcm.discordgamesdk.voice.VoiceInputMode;

public class SetVoiceSettings2 {
    private SetVoiceSettings2() {}

    public static class InputMode {
        public VoiceInputMode input_mode;

        public InputMode(VoiceInputMode input_mode) {
            this.input_mode = input_mode;
        }
    }
    public static class SelfMute {
        public boolean self_mute;

        public SelfMute(boolean self_mute) {
            this.self_mute = self_mute;
        }
    }
    public static class SelfDeaf {
        public boolean self_deaf;

        public SelfDeaf(boolean self_deaf) {
            this.self_deaf = self_deaf;
        }
    }
}
