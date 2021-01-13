package de.jcm.discordgamesdk;

import de.jcm.discordgamesdk.lobby.LobbyTransaction;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A few utils that might be useful when working with the SDK or Discord in general.
 */
public class DiscordUtils
{
	private DiscordUtils()
	{
		throw new RuntimeException("DiscordUtils is a static class and no instance of it can be obtained.");
	}

	/**
	 * Creates a {@link Consumer} that takes a {@link Result} and then completes a {@link CompletableFuture}.
	 * <p>
	 * The future is completely normally with a {@code null} value,
	 * if and only if the {@link Result} is {@link Result#OK}.
	 * <p>
	 * In any other case it is completed exceptionally with
	 * a {@link GameSDKException} wrapping the {@link Result}.
	 * <p>
	 * This method is mainly intended for chaining SDK calls together,
	 * so that one executes after another completed.
	 * @param future A {@link CompletableFuture} that should be completed
	 * @return A {@link Consumer} completing the future
	 * @see CompletableFuture
	 * @see CompletionStage
	 */
	public static Consumer<Result> completer(CompletableFuture<Void> future)
	{
		return result -> {
			if(result == Result.OK)
			{
				future.complete(null);
			}
			else
			{
				future.completeExceptionally(new GameSDKException(result));
			}
		};
	}

	/**
	 * Creates a {@link BiConsumer} that takes a {@link Result} and then completes a {@link CompletableFuture}.
	 * <p>
	 * The future is completely normally with the value passed to the {@link BiConsumer}
	 * (e.g. as part of {@link LobbyManager#createLobby(LobbyTransaction, BiConsumer)}),
	 * if and only if the {@link Result} is {@link Result#OK}.
	 * <p>
	 * In any other case it is completed exceptionally with
	 * a {@link GameSDKException} wrapping the {@link Result}.
	 * <p>
	 * This method is mainly intended for chaining SDK calls together,
	 * so that one executes after another completed.
	 * @param future A {@link CompletableFuture} that should be completed
	 * @param <T> Type of the returned value (not the {@link Result}, but the other value)
	 * @return A {@link BiConsumer} completing the future
	 * @see CompletableFuture
	 * @see CompletionStage
	 */
	public static <T> BiConsumer<Result, T> returningCompleter(CompletableFuture<T> future)
	{
		return (result, t) -> {
			if(result == Result.OK)
			{
				future.complete(t);
			}
			else
			{
				future.completeExceptionally(new GameSDKException(result));
			}
		};
	}

	/**
	 * Gets an Instant from a Discord Snowflake.
	 * <p>
	 * Discord Snowflakes are used for all sorts of IDs (e.g. User ID, Lobby ID).
	 * They contain information about the time they were created,
	 * which this methods extracts.
	 * @param snowflake A Discord Snowflake
	 * @return The {@link Instant} at which the Discord object (User, Lobby, etc.) was created
	 * @see <a href="https://discord.com/developers/docs/reference#snowflakes">
	 *     https://discord.com/developers/docs/reference#snowflakes</a>
	 */
	public static Instant dateTimeFromSnowflake(long snowflake)
	{
		long discordTime = snowflake >> 22;
		long unixTime = discordTime + 1420070400000L;
		return Instant.ofEpochMilli(unixTime);
	}
}
