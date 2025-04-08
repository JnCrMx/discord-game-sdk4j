package de.jcm.discordgamesdk.impl.events;

import com.google.gson.JsonObject;
import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.impl.Command;
import de.jcm.discordgamesdk.impl.EventHandler;
import de.jcm.discordgamesdk.user.DiscordUser;

public class ActivityJoinRequestEvent {

    public static class Handler extends EventHandler<DiscordUser> {
        public Handler(Core.CorePrivate core) {
            super(core);
        }

        @Override
        public void handle(Command command, DiscordUser discordUser) {
            JsonObject data = command.getData().getAsJsonObject();
            JsonObject userData = data.getAsJsonObject("user");
            long userID = 0;
            String username = null;
            String discriminator = null;
            String avatar = null;
            boolean bot = false;
            if (!userData.get("id").isJsonNull()) userID = Long.parseLong(userData.get("id").getAsString());
            if (!userData.get("username").isJsonNull()) username = userData.get("username").getAsString();
            if (!userData.get("discriminator").isJsonNull()) discriminator = userData.get("discriminator").getAsString();
            if (!userData.get("avatar").isJsonNull()) avatar = userData.get("avatar").getAsString();
            if (!userData.get("bot").isJsonNull()) bot = userData.get("bot").getAsBoolean();
            DiscordUser user = new DiscordUser(userID, username, discriminator, avatar, bot);
            core.getEventAdapter().onActivityJoinRequest(user);
        }

        @Override
        public Class<?> getDataClass() {
            return DiscordUser.class;
        }
    }

}
