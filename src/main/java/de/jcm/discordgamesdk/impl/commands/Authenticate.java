package de.jcm.discordgamesdk.impl.commands;

import de.jcm.discordgamesdk.ApplicationManager;
import de.jcm.discordgamesdk.user.DiscordUser;

import java.util.Date;
import java.util.Set;

public class Authenticate
{
    private Authenticate() {}

    public static class Response
    {
        public static class Application {
            String id;
            String name;
            String icon;
            String description;
            String type;
            String cover_image;
            String summary;
            boolean is_monetized;
            boolean is_verified;
            String verify_key;
            int flags;
            boolean hook;
            boolean storefront_available;

            public ApplicationManager.Application toApplication() {
                return new ApplicationManager.Application(
                        Long.parseLong(id), name, icon, description, type, cover_image,
                        summary, is_monetized, is_verified, verify_key, flags, hook, storefront_available
                );
            }
        };

        public String access_token;
        public Set<String> scopes;
        public Date expires;

        public DiscordUser user;
        public Application application;

        public ApplicationManager.DiscordOAuth2Token toDiscordOAuth2Token() {
            return new ApplicationManager.DiscordOAuth2Token(access_token, scopes, expires);
        }
    }
}
