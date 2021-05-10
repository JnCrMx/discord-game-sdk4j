package de.jcm.discordgamesdk;

import java.util.Objects;
import java.util.stream.Stream;
import java.util.concurrent.atomic.AtomicBoolean;

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

	private final long pointer;
	private final AtomicBoolean open = new AtomicBoolean(true);

	/**
	 * Allocates a new structure and initializes it with default parameters.
	 */
	public CreateParams()
	{
		this.pointer = allocate();
	}

	/**
	 * Sets the application/client ID.
	 * @param id Application/client ID.
	 */
	public void setClientID(long id)
	{
		setClientID(pointer, id);
	}

	/**
	 * Gets the application/client ID.
	 * @return Application/client ID.
	 */
	public long getClientID()
	{
		return getClientID(pointer);
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
		setFlags(pointer, Flags.toLong(flags));
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
		setFlags(pointer, flags);
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
		return getFlags(pointer);
	}

	/**
	 * Registers an event handler to later receive events from the created Core.
	 * @param eventHandler An EventHandler
	 */
	public void registerEventHandler(DiscordEventAdapter eventHandler)
	{
		registerEventHandler(pointer, Objects.requireNonNull(eventHandler));
	}

	private native long allocate();
	private native void free(long pointer);

	private native void setClientID(long pointer, long id);
	private native long getClientID(long pointer);

	private native void setFlags(long pointer, long flags);
	private native long getFlags(long pointer);

	private native void registerEventHandler(long pointer, DiscordEventAdapter handler);

	/**
	 * Gets the default flags for new Cores.
	 * @return The default flags.
	 */
	public static native long getDefaultFlags();

	/**
	 * <p>Frees the allocated native structure.</p>
	 * <p>You should call this when you do not need the structure anymore.
	 * Do <b>not</b> call this if you still want to use a {@link Core} created from the params.
	 * It will cause the JVM to crash with an access violation exception.</p>
	 * <p>If you a using a <i>try-with-resources</i> block make sure that you only use the created
	 * {@link Core} - especially {@link Core#runCallbacks()} - <b>inside</b> the block, because
	 * the CreateParams will be closed by the end of the block.</p>
	 */
	@Override
	public void close()
	{
		if(open.compareAndSet(true, false))
		{
			free(pointer);
		}
	}

	/**
	 * <p>Return the pointer to the native structure.</p>
	 * <p>This is <b>not</b> an API method. Do <b>not</b> call it.</p>
	 * @return A native pointer.
	 */
	public long getPointer()
	{
		return pointer;
	}
}
