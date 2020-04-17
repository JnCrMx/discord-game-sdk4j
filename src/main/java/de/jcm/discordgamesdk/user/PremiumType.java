package de.jcm.discordgamesdk.user;

/**
 * Type of a user's premium subscription.
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/users#data-models-premiumtype-enum">
 *     https://discordapp.com/developers/docs/game-sdk/users#data-models-premiumtype-enum</a>
 */
public enum PremiumType
{
	/**
	 * No premium subscription.
	 */
	NONE,
	/**
	 * Discord Nitro Classic subscription.
	 */
	TIER1,
	/**
	 * Discord Nitro subscription.
	 */
	TIER2
}
