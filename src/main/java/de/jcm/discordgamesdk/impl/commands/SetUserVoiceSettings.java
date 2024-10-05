package de.jcm.discordgamesdk.impl.commands;

public class SetUserVoiceSettings {
    public static class Args {
        public String user_id;

        public Args(String user_id) {
            this.user_id = user_id;
        }
    }
    public static class Mute extends Args {
        public boolean mute;

        public Mute(String user_id, boolean mute) {
            super(user_id);
            this.mute = mute;
        }
    }
    public static class Volume extends Args {
        public int volume;

        public Volume(String user_id, int volume) {
            super(user_id);
            this.volume = volume;
        }
    }
}
