package de.jcm.discordgamesdk;

import java.util.stream.Stream;

/**
 * Initial parameters to create a {@link Core} from.
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/discord#create-parameters">
 *     https://discordapp.com/developers/docs/game-sdk/discord#create-parameters</a>
 */
public class CreateParams implements AutoCloseable
{
	/**
	 * Enum representing possible Flags in {@link CreateParams}.
	 */
	public enum Flags
	{
		/**
		 * This flag requires Discord to be active in order to use the SDK.
		 * If Discord isn't active, the application will terminate and
		 * tires to start itself via Discord.
		 * <p>
		 * This might cause issues on many platforms (e.g. on some Linux distributions).
		 */
		DEFAULT(0),
		/**
		 * This flag does <i>not</i> require Discord to be active to use the SDK.
		 * <p>
		 * Nevertheless, {@link Core#runCallbacks()} will fail if Discord is not active,
		 * but it will just throw a {@link GameSDKException} and will not terminate
		 * the application.
		 */
		NO_REQUIRE_DISCORD(1)
		;

		private final long value;

		Flags(long value)
		{
			this.value = value;
		}

		/**
		 * Converts an array of flags to their binary representation.
		 * @param flags Flags to convert.
		 * @return A binary representation of the given Flags.
		 */
		public static long toLong(Flags... flags)
		{
			long l = 0;
			for(Flags f : flags)
			{
				l |= f.value;
			}
			return l;
		}

		/**
		 * Converts the binary representation of flags to an array of Flags.
		 * <p>
		 * However, unknown flags that are present in the binary presentation
		 * will <b>not</b> be included in the array.
		 * Hence, {@code Flags.toLong(Flags.fromLong(myLong))} is <b>not</b> guaranteed
		 * to be equal to {@code myLong}.
		 * @param l Binary representation
		 * @return Array of Flags that are set for the given long
		 */
		public static Flags[] fromLong(long l)
		{
			return Stream.of(Flags.values()).filter(f->(l & f.value) != 0).toArray(Flags[]::new);
		}
	}

	long flags;
	long clientID;
	DiscordEventAdapter eventAdapter;

	/**
	 * Create the CreateParams.
	 */
	public CreateParams()
	{
	}

	/**
	 * Sets the application/client ID.
	 * @param id Application/client ID.
	 */
	public void setClientID(long id)
	{
		this.clientID = id;
	}

	/**
	 * Gets the application/client ID.
	 * @return Application/client ID.
	 */
	public long getClientID()
	{
		return clientID;
	}

	/**
	 * Sets flags for the Core.
	 * @param flags Flags to initialize the Core with.
	 * @see #setFlags(long)
	 * @see Flags
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/discord#data-models-createflags-enum">
	 *     https://discordapp.com/developers/docs/game-sdk/discord#data-models-createflags-enum</a>
	 */
	public void setFlags(Flags... flags)
	{
		setFlags(Flags.toLong(flags));
	}

	/**
	 * Sets flags for the Core.
	 * @param flags Flags to initialize the Core with.
	 * @see #setFlags(Flags...)
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/discord#data-models-createflags-enum">
	 *     https://discordapp.com/developers/docs/game-sdk/discord#data-models-createflags-enum</a>
	 */
	public void setFlags(long flags)
	{
		this.flags = flags;
	}

	/**
	 * Gets the flags set for the Core.
	 * <p>
	 * Use {@link Flags#fromLong(long)} to convert this to an array of flags.
	 * @return Flags that have been set.
	 * @see #setFlags(long)
	 * @see #setFlags(Flags...)
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/discord#data-models-createflags-enum">
	 *     https://discordapp.com/developers/docs/game-sdk/discord#data-models-createflags-enum</a>
	 */
	public long getFlags()
	{
		return flags;
	}

	/**
	 * Registers an event handler to later receive events from the created Core.
	 * @param eventHandler An EventHandler
	 */
	public void registerEventHandler(DiscordEventAdapter eventHandler)
	{
		eventAdapter = eventHandler;
	}
	/**
	 * Gets the default flags for new Cores.
	 * @return The default flags.
	 */
	public static long getDefaultFlags()
	{
		return Flags.DEFAULT.value;
	}

	/**
	 * No operation, only kept for backwards compatibility
	 */
	@Override
	@Deprecated
	public void close()
	{
	}
}
