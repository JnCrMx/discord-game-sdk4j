package de.jcm.discordgamesdk;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Initial parameters to create a {@link Core} from.
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/discord#create-parameters">
 *     https://discordapp.com/developers/docs/game-sdk/discord#create-parameters</a>
 */
public class CreateParams implements AutoCloseable
{
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
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/discord#data-models-createflags-enum">
	 *     https://discordapp.com/developers/docs/game-sdk/discord#data-models-createflags-enum</a>
	 */
	public void setFlags(long flags)
	{
		setFlags(pointer, flags);
	}

	/**
	 * Gets the flags set for the Core.
	 * @return Flags that have been set.
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
