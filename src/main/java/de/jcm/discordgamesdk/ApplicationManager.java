package de.jcm.discordgamesdk;

import de.jcm.discordgamesdk.impl.Command;
import de.jcm.discordgamesdk.impl.commands.Authenticate;
import de.jcm.discordgamesdk.user.DiscordUser;

import java.util.Date;
import java.util.Set;
import java.util.function.BiConsumer;

public class ApplicationManager
{
    /**
     * A Discord OAuth2 token.
     * @param accessToken the access token itself
     * @param scopes a set of the scopes the token is authorized for
     * @param expires expiration date of the token
     */
    public record DiscordOAuth2Token(String accessToken, Set<String> scopes, Date expires) {}

    /**
     * A Discord application.
     * @param id ID of the application
     * @param name Name of the application
     * @param icon Asset ID of the icon of the application
     * @param description Description of the application
     * @param type Type of the application
     * @param coverImage Asset ID of the cover image of the application
     * @param summary Summary of the application
     * @param monetized {@code true} if the application is monetized
     * @param verified {@code true} if the application is verified
     * @param verifyKey ???
     * @param flags Flags of the application
     * @param hook ???
     * @param storefrontAvailable {@code true} if the application is available on the storefront
     */
    public record Application(long id, String name, String icon, String description, String type,
                              String coverImage, String summary, boolean monetized, boolean verified,
                              String verifyKey, int flags, boolean hook, boolean storefrontAvailable) {}

    /**
     * Combined data consisting of a {@link DiscordOAuth2Token}, an {@link Application}, and a {@link DiscordUser}
     * @param token Obtained token
     * @param application Information about this application
     * @param user Current Discord user
     */
    public record AuthenticationData(DiscordOAuth2Token token, Application application, DiscordUser user) {}

    private final Core.CorePrivate core;

    ApplicationManager(Core.CorePrivate core)
    {
        this.core = core;
    }

    /**
     * Requests authorization from the user (if not obtained already) and returns
     * the OAuth2 token in the callback.
     * @param callback Callback to process the returned {@link Result} and {@link DiscordOAuth2Token}.
     */
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

    /**
     * Requests authorization from the user (if not obtained already) and returns
     * the OAuth2 token and some additional information about user and application in a callback.
     * @param callback Callback to process the returned {@link Result} and {@link AuthenticationData}.
     */
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
