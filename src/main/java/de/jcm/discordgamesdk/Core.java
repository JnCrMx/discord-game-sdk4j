package de.jcm.discordgamesdk;

import com.google.gson.Gson;
import de.jcm.discordgamesdk.impl.Error;
import de.jcm.discordgamesdk.impl.*;
import de.jcm.discordgamesdk.impl.commands.Subscribe;
import de.jcm.discordgamesdk.impl.events.OverlayUpdateEvent;
import de.jcm.discordgamesdk.impl.events.VoiceSettingsUpdate2Event;
import de.jcm.discordgamesdk.user.DiscordUser;
import de.jcm.discordgamesdk.user.Relationship;

import java.io.IOException;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * The main component for accessing Discord's game SDK.
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/discord#functions-in-the-sdk">
 *     https://discordapp.com/developers/docs/game-sdk/discord#functions-in-the-sdk</a>
 * @author JCM
 */
public class Core implements AutoCloseable
{
	/**
	 * <p>Default callback to use for operation returning a {@link Result}.</p>
	 *
	 * <p>Checks if the result is {@link Result#OK} and throws a {@link GameSDKException} if it is not.</p>
	 */
	public static final Consumer<Result> DEFAULT_CALLBACK = result ->
	{
		if(result!=Result.OK)
			throw new GameSDKException(result);
	};

	/**
	 * <p>Default log hook. Simply prints the log message
	 * in pattern "<code>[level] message</code>" to {@link System#out}.</p>
	 */
	public static final BiConsumer<LogLevel, String> DEFAULT_LOG_HOOK= (level, message) ->
	{
		System.out.printf("[%s] %s\n", level, message);
	};

	private final DiscordChannel channel;
	private ConnectionState state;
	private final Gson gson;
	private long nonce;
	private final Map<String, Consumer<Command>> handlers;
	private final Events events;
	private final DiscordEventAdapter eventAdapter;
	private BiConsumer<LogLevel, String> logHook = DEFAULT_LOG_HOOK;
	private LogLevel minLogLevel = LogLevel.VERBOSE;
	private final CorePrivate corePrivate;

	private final CreateParams createParams;
	private final AtomicBoolean open = new AtomicBoolean(true);

	private final ActivityManager activityManager;
	private final UserManager userManager;
	private final OverlayManager overlayManager;
	private final RelationshipManager relationshipManager;
	private final ImageManager imageManager;
	private final VoiceManager voiceManager;

	/**
	 * Creates an instance of the SDK from {@link CreateParams} and
	 * sets the log hook to {@link Core#DEFAULT_LOG_HOOK}.
	 * <p>
	 * Example:
	 * <pre>{@code
	 *  try(CreateParams params = new CreateParams())
	 *  {
	 *      params.setClientID(<client ID of your application>);
	 *      params.setFlags(CreateParams.getDefaultFlags());
	 *      try(Core core = new Core(params))
	 *      {
	 *          // do something with your Core
	 *      }
	 *  }}</pre>
	 *
	 * @param params Parameters to create Core from.
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/discord#create">
	 *     https://discordapp.com/developers/docs/game-sdk/discord#create</a>
	 */
	public Core(CreateParams params)
	{
		this.createParams = params;

		this.state = ConnectionState.HANDSHAKE;
		this.gson = new Gson();
		this.nonce = 0;
		this.handlers = new HashMap<>();
		this.corePrivate = new CorePrivate();
		this.events = new Events(corePrivate);
		this.eventAdapter = createParams.eventAdapter;

		try
		{
            if (System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("windows"))
            {
                throw new IOException("No windows support, yet :(");
            }
            else // Assume UDS are available, if it is not Windows.
            {
                this.channel = new UnixDiscordChannel();
            }
            this.sendHandshake();
			runCallbacks();
			channel.configureBlocking(false);
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}

		this.activityManager = new ActivityManager(corePrivate);
		this.overlayManager = new OverlayManager(corePrivate);
		this.userManager = new UserManager(corePrivate);
		this.relationshipManager = new RelationshipManager(corePrivate);
		this.imageManager = new ImageManager(corePrivate);
		this.voiceManager = new VoiceManager(corePrivate);
	}

	public class CorePrivate
	{
		private CorePrivate() {}

		public Queue<Runnable> workQueue = new ArrayDeque<>();

		public int pid = (int) ProcessHandle.current().pid();
		public DiscordUser currentUser;
		public Map<Long, Relationship> relationships = new HashMap<>();
		public OverlayUpdateEvent.Data overlayData = new OverlayUpdateEvent.Data();
		public VoiceSettingsUpdate2Event.Data voiceData = new VoiceSettingsUpdate2Event.Data();

		private static final DiscordEventAdapter NULL_ADAPTER = new DiscordEventAdapter(){};
		public DiscordEventAdapter getEventAdapter()
		{
			return Optional.ofNullable(eventAdapter).orElse(NULL_ADAPTER);
		}

		public void ready()
		{
			state = ConnectionState.CONNECTED;
			registerEvents();
		}

		public Core getCore()
		{
			return Core.this;
		}

		public void sendCommand(Command.Type type, Object args, Consumer<Command> responseHandler)
		{
			Command command = new Command();
			command.setCmd(type);
			command.setArgs(gson.toJsonTree(args).getAsJsonObject());
			command.setNonce(Long.toString(++nonce));
			Core.this.sendCommand(command, responseHandler);
		}

		public void sendCommandNoResponse(Command.Type type, Object args, Consumer<Command> responseHandler)
		{
			Command command = new Command();
			command.setCmd(type);
			command.setArgs(gson.toJsonTree(args).getAsJsonObject());
			command.setNonce(Long.toString(0));

			try
			{
				sendString(gson.toJson(command));
			}
			catch(IOException e)
			{
				throw new RuntimeException(e);
			}
			corePrivate.workQueue.add(()->{
				Command c = new Command();
				c.setEvt(null);
				c.setNonce(Long.toString(0));
				c.setCmd(type);
				c.setData(null);
				responseHandler.accept(c);
			});
		}

		public Gson getGson()
		{
			return gson;
		}

		public void log(LogLevel level, String message)
		{
			if(level.compareTo(minLogLevel) <= 0)
			{
				logHook.accept(level, message);
			}
		}

		public Result checkError(Command c)
		{
			if(c.getEvent() == Command.Event.ERROR)
			{
				Error error = gson.fromJson(c.getData(), Error.class);
				log(LogLevel.ERROR, error.getMessage());

				return Result.fromCode(error.getCode());
			}
			return Result.OK;
		}
	}

	private void sendString(String message) throws IOException
	{
		byte[] bytes = message.getBytes();
		ByteBuffer buf = ByteBuffer.allocate(bytes.length + 8);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		buf.putInt(state.ordinal());
		buf.putInt(bytes.length);
		buf.put(bytes);

		channel.write(buf.flip());
		corePrivate.log(LogLevel.VERBOSE, "Sent string \""+message+"\" at state "+state);
	}

	private static class Res
	{
		public ConnectionState result;
		public String data;

		public Res(ConnectionState result, String data)
		{
			this.result = result;
			this.data = data;
		}
	}

	private Res receiveString() throws IOException
	{
		ByteBuffer header = ByteBuffer.allocate(8);
		channel.read(header);
		header.flip();
		header.order(ByteOrder.LITTLE_ENDIAN);
		if(header.remaining() == 0)
		{
			return null;
		}

		int status = header.getInt(); // ignored for now?
		int length = header.getInt();

		ByteBuffer data = ByteBuffer.allocate(length);
		int read = 0;
		do
		{
			read += (int) channel.read(new ByteBuffer[]{data}, 0, 1);
		}
		while(read < length);
		String s = new String(data.flip().array());
		ConnectionState state1 = ConnectionState.values()[status];

		corePrivate.log(LogLevel.VERBOSE, "Received string \""+s+"\" at state "+state1);

		return new Res(state1, s);
	}

	private void sendCommand(Command command, Consumer<Command> responseHandler)
	{
		handlers.put(command.getNonce(), responseHandler);
		try
		{
			sendString(gson.toJson(command));
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	private void sendHandshake() throws IOException
	{
		HandshakeMessage handshakeMessage = new HandshakeMessage(Long.toString(createParams.getClientID()));
		sendString(gson.toJson(handshakeMessage));
	}

	private void registerEvents()
	{
		for(Map.Entry<Command.Event, EventHandler<?>> e : events.getEventTypes())
		{
			Command.Event event = e.getKey();
			EventHandler<?> handler = e.getValue();
			if(!handler.shouldRegister()) continue;

			Command command = new Command();
			command.setCmd(Command.Type.SUBSCRIBE);
			command.setEvt(event);
			command.setArgs(gson.toJsonTree(handler.getRegisterArgs()));
			command.setNonce(Long.toString(++nonce));
			sendCommand(command, o->
					corePrivate.log(LogLevel.DEBUG, "Registered event "+gson.fromJson(o.getData(), Subscribe.Response.class).getEvent()));
		}
	}

	private Command receiveCommand() throws IOException
	{
		Res r = receiveString();
		if(r == null)
			return null;
		return gson.fromJson(r.data, Command.class);
	}

	private void handleCommand(Command command)
	{
		if(command.getNonce() != null)
		{
			handlers.remove(command.getNonce()).accept(command);
		}
		else if(command.getEvent() != null)
		{
			EventHandler<?> handler = events.forEvent(command.getEvent());
			Object data = gson.fromJson(command.getData(), handler.getDataClass());
			handler.handleObject(command, data);
		}
	}

	/**
	 * <p>Returns the {@link ActivityManager} associated with this core.</p>
	 * <p>An ActivityManager is used to set the User's activity/status.</p>
	 * @return An {@link ActivityManager}
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/discord#getactivitymanager">
	 *     https://discordapp.com/developers/docs/game-sdk/discord#getactivitymanager</a>
	 */
	public ActivityManager activityManager()
	{
		return activityManager;
	}

	/**
	 * <p>Returns the {@link UserManager} associated with this core.</p>
	 * <p>A UserManager is used to receive information about Discord users.</p>
	 * @return A {@link UserManager}
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/discord#getusermanager">
	 *     https://discordapp.com/developers/docs/game-sdk/discord#getusermanager</a>
	 */
	public UserManager userManager()
	{
		return userManager;
	}

	/**
	 * <p>Returns the {@link OverlayManager} associated with this core.</p>
	 * <p>An OverlayManager is used to control the overlay for this game.</p>
	 * @return An {@link OverlayManager}
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/discord#getoverlaymanager">
	 *     https://discordapp.com/developers/docs/game-sdk/discord#getoverlaymanager</a>
	 */
	public OverlayManager overlayManager()
	{
		return overlayManager;
	}

	/**
	 * <p>Returns the {@link RelationshipManager} associated with this core.</p>
	 * <p>A RelationshipManager is used to receive information about the user's
	 * relationships with other Discord users (e.g. friends).</p>
	 * @return A {@link RelationshipManager}
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/discord#getrelationshipmanager">
	 *     https://discordapp.com/developers/docs/game-sdk/discord#getrelationshipmanager</a>
	 */
	public RelationshipManager relationshipManager()
	{
		return relationshipManager;
	}

	/**
	 * <p>Returns the {@link ImageManager} associated with this core.</p>
	 * <p>An ImageManager is used to fetch images and information
	 * about images (dimensions) from Discord (mainly avatars).</p>
	 * @return An {@link ImageManager}
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/discord#getimagemanager">
	 *     https://discordapp.com/developers/docs/game-sdk/discord#getimagemanager</a>
	 */
	public ImageManager imageManager()
	{
		return imageManager;
	}

	/**
	 * Returns the {@link VoiceManager} associated with this core.
	 * <p>
	 * A VoiceManager is used to control Discord Lobby voice channels.
	 * It can be used to configure input modes, to mute and deaf the current user,
	 * to locally mute other users and to locally adjust their volume.
	 * @return A {@link VoiceManager}
	 * @see <a href="https://discord.com/developers/docs/game-sdk/discord#getvoicemanager">
	 *     https://discord.com/developers/docs/game-sdk/discord#getvoicemanager</a>
	 */
	public VoiceManager voiceManager()
	{
		return voiceManager;
	}

	/**
	 * <p>Listens for new events and runs pending callbacks.</p>
	 * <p>This method should be called in a main loop every few millis.</p>
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/discord#setloghook">
	 *     https://discordapp.com/developers/docs/game-sdk/discord#runcallbacks</a>
	 */
	public void runCallbacks()
	{
		Runnable r;
		while((r = corePrivate.workQueue.poll()) != null)
			r.run();

		try
		{
			Command c = receiveCommand();
			if(c != null)
			{
				handleCommand(c);
			}
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * Registers a log function.
	 * @param minLevel Minimal level of message to receive.
	 * @param logHook Hook to send log messages to.
	 * @see Core#DEFAULT_LOG_HOOK
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/discord#setloghook">
	 *     https://discordapp.com/developers/docs/game-sdk/discord#setloghook</a>
	 */
	public void setLogHook(LogLevel minLevel, BiConsumer<LogLevel, String> logHook)
	{
		this.logHook = logHook;
		this.minLogLevel = minLevel;
	}

	/**
	 * Returns true if this {@link Core} instance is open, i.e. {@link #close()} has not
	 * been called yet. Calling certain SDK methods will throw {@link CoreClosedException}
	 * if the {@link Core} is not open.
	 * @return True if this {@link Core} is still open, false otherwise
	 */
	public boolean isOpen()
	{
		return open.get();
	}

	/**
	 * <p>Closes and destroys the instance.</p>
	 * <p>This should be called at the end of the program.</p>
	 *
	 * @see <a href="https://discordapp.com/developers/docs/game-sdk/discord#destroy">
	 *     https://discordapp.com/developers/docs/game-sdk/discord#destroy</a>
	 */
	@Override
	public void close()
	{
		try
		{
			channel.close();
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}
