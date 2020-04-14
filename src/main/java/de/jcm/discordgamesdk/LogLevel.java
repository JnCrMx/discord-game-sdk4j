package de.jcm.discordgamesdk;

import java.util.function.BiConsumer;

/**
 * <p>Log levels for {@link Core#setLogHook(LogLevel, BiConsumer)}.</p>
 * <p>Every log level also includes the previous ones.
 * For example {@link LogLevel#WARN} also logs {@link LogLevel#ERROR}.</p>
 * <p>Note that the native enum starts at index {@code 1} while this enum starts at index {@code 0}.
 * This should not bother you too much, but might be useful to know.</p>
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/discord#data-models-loglevel-enum">
 *     https://discordapp.com/developers/docs/game-sdk/discord#data-models-loglevel-enum</a>
 */
public enum LogLevel
{
	/**
	 * Log errors.
	 */
	ERROR,
	/**
	 * Log warnings, and errors.
	 */
	WARN,
	/**
	 * Log information, warnings, and errors.
	 */
	INFO,
	/**
	 * Log everything!
	 */
	DEBUG
}
