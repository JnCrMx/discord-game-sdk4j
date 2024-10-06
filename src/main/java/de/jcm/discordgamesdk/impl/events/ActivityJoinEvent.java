package de.jcm.discordgamesdk.impl.events;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.impl.Command;
import de.jcm.discordgamesdk.impl.EventHandler;

public class ActivityJoinEvent {
    public static class Data {
        String secret;
    }

    public static class Handler extends EventHandler<Data> {
        public Handler(Core.CorePrivate core) {
            super(core);
        }

        @Override
        public void handle(Command command, Data data) {
            core.getEventAdapter().onActivityJoin(data.secret);
        }

        @Override
        public Class<?> getDataClass() {
            return Data.class;
        }
    }
}
