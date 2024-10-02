package de.jcm.discordgamesdk.impl.events;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.LogLevel;
import de.jcm.discordgamesdk.impl.Command;
import de.jcm.discordgamesdk.impl.EventHandler;
import de.jcm.discordgamesdk.user.DiscordUser;

public class ActivityJoinEvent {
    public static class Handler extends EventHandler<DiscordUser>
    {
        public Handler(Core.CorePrivate core)
        {
            super(core);
        }

        @Override
        public void handle(Command command, DiscordUser user)
        {
            core.log(LogLevel.VERBOSE, "Secret: " + command.getData().getAsJsonObject().get("secret").getAsString());
            core.getEventAdapter().onActivityJoin(command.getData().getAsJsonObject().get("secret").getAsString());
        }

        @Override
        public Class<?> getDataClass()
        {
            return DiscordUser.class;
        }

    }
}
