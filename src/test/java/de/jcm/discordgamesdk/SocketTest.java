package de.jcm.discordgamesdk;

import com.google.gson.Gson;
import de.jcm.discordgamesdk.impl.Command;
import de.jcm.discordgamesdk.impl.ConnectionState;
import de.jcm.discordgamesdk.impl.HandshakeMessage;
import de.jcm.discordgamesdk.user.DiscordUser;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;

public class SocketTest
{
	String receive(SocketChannel channel) throws IOException
	{
		ByteBuffer header = ByteBuffer.allocate(8);
		channel.read(header);
		header.flip();
		header.order(ByteOrder.LITTLE_ENDIAN);

		int status = header.getInt();
		int length = header.getInt();

		System.err.printf("Received message of state %s and length %d\n", ConnectionState.values()[status], length);

		ByteBuffer data = ByteBuffer.allocate(length);
		int read = 0;
		do
		{
			read += (int) channel.read(new ByteBuffer[]{data}, 0, 1);
		}
		while(read < length);
		return new String(data.flip().array());
	}

	void send(SocketChannel channel, ConnectionState state, String message) throws IOException
	{
		byte[] bytes = message.getBytes();
		ByteBuffer buf = ByteBuffer.allocate(bytes.length + 8);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		buf.putInt(state.ordinal());
		buf.putInt(bytes.length);
		buf.put(bytes);

		channel.write(buf.flip());
	}

	@Test
	void testProtocol() throws IOException
	{
		SocketChannel channel = SocketChannel.open(UnixDomainSocketAddress.of("/run/user/1000/discord-ipc-0"));

		/*String hello = "{\"v\":1,\"client_id\":\"698611073133051974\"}";
		String subscribe = "{\"cmd\":\"SUBSCRIBE\",\"nonce\":7,\"evt\":\"RELATIONSHIP_UPDATE\",\"args\":null}";
		String getUser = "{\"cmd\":\"GET_USER\",\"nonce\":\"7\",\"evt\":null,\"args\":{\"id\":\"352386023159758848\"}}";
		String getRelationships = "{\"cmd\":\"GET_RELATIONSHIPS\",\"nonce\":\"7\",\"evt\":null,\"args\":{}}";*/

		Gson gson = new Gson();

		HandshakeMessage handshakeMessage = new HandshakeMessage("698611073133051974");
		String handshake = gson.toJson(handshakeMessage);
		System.out.println(handshake);

		Command getUser = new Command();
		getUser.setCmd(Command.Type.GET_USER);
		getUser.setNonce("1");
		DiscordUser u = new DiscordUser(352386023159758848L, null, null, null, null);
		getUser.setArgs(gson.toJsonTree(u).getAsJsonObject());

		System.out.println(gson.toJson(u));
		System.out.println(gson.toJson(getUser));

		send(channel, ConnectionState.HANDSHAKE, handshake);
		System.out.println(receive(channel));

		send(channel, ConnectionState.CONNECTED, gson.toJson(getUser));
		String answer = receive(channel);
		Command cmd = gson.fromJson(answer, Command.class);
		System.out.println(gson.fromJson(cmd.getData(), DiscordUser.class));
	}

	@Test
	void testNewCore()
	{
		String discordLibraryPath;
		if(System.getProperty("os.name").toLowerCase().contains("windows"))
		{
			discordLibraryPath = "./discord_game_sdk/lib/x86_64/discord_game_sdk.dll";
		}
		else
		{
			discordLibraryPath = "./discord_game_sdk/lib/x86_64/discord_game_sdk.so";
		}
		Core.init(new File(discordLibraryPath));

		CreateParams params = new CreateParams();
		DiscordEventHandler handler = new DiscordEventHandler();
		params.registerEventHandler(handler);
		params.setClientID(698611073133051974L);
		Core core = new Core(params);

		handler.addListener(new DiscordEventAdapter()
		{
			@Override
			public void onCurrentUserUpdate()
			{
				System.out.println(core.userManager().getCurrentUser());
			}
		});
		core.userManager().getUser(269831647468191754L, (r,u)->{
			System.out.println(r+" "+u);
		});

		for(int i=0; i<1000; i++)
		{
			core.runCallbacks();
			try
			{
				Thread.sleep(16);
			}
			catch(InterruptedException e)
			{
				throw new RuntimeException(e);
			}
		}
	}
}
