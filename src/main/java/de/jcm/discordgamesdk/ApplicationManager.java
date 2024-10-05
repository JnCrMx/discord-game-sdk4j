package de.jcm.discordgamesdk;

import de.jcm.discordgamesdk.impl.Command;
import de.jcm.discordgamesdk.impl.commands.Authenticate;
import de.jcm.discordgamesdk.user.DiscordUser;

import java.util.Date;
import java.util.Set;
import java.util.function.BiConsumer;

public class ApplicationManager
{
    public record DiscordOAuth2Token(String accessToken, Set<String> scopes, Date expires) {}
    public record Application(long id, String name, String icon, String description, String type,
                              String coverImage, String summary, boolean monetized, boolean verified,
                              String verifyKey, int flags, boolean hook, boolean storefrontAvailable) {}
    public record AuthenticationData(DiscordOAuth2Token token, Application application, DiscordUser user) {}

    private final Core.CorePrivate core;

    ApplicationManager(Core.CorePrivate core)
    {
        this.core = core;
    }

    public void getOAuth2Token(BiConsumer<Result, DiscordOAuth2Token> callback) {
        core.sendCommand(Command.Type.AUTHENTICATE, new Object(), c->{
            Result r = core.checkError(c);
            if(r != Result.OK)
            {
                callback.accept(r, null);
                return;
            }
            Authenticate.Response response = core.getGson().fromJson(c.getData(), Authenticate.Response.class);
            callback.accept(r, response.toDiscordOAuth2Token());
        });
    }

    public void authenticate(BiConsumer<Result, AuthenticationData> callback) {
        core.sendCommand(Command.Type.AUTHENTICATE, new Object(), c->{
            Result r = core.checkError(c);
            if(r != Result.OK)
            {
                callback.accept(r, null);
                return;
            }
            Authenticate.Response response = core.getGson().fromJson(c.getData(), Authenticate.Response.class);
            DiscordOAuth2Token token = response.toDiscordOAuth2Token();
            DiscordUser user = response.user;
            Application application = response.application.toApplication();

            callback.accept(r, new AuthenticationData(token, application, user));
        });
    }
}
