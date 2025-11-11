package de.jcm.discordgamesdk;

import com.google.gson.Gson;
import de.jcm.discordgamesdk.activity.Activity;
import de.jcm.discordgamesdk.activity.ActivityActionType;
import de.jcm.discordgamesdk.impl.Command;
import de.jcm.discordgamesdk.impl.ConnectionState;
import de.jcm.discordgamesdk.impl.HandshakeMessage;
import de.jcm.discordgamesdk.impl.channel.DiscordChannel;
import de.jcm.discordgamesdk.user.DiscordUser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class SocketTest
{
	String receive(DiscordChannel channel) throws IOException
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

	void send(DiscordChannel channel, ConnectionState state, String message) throws IOException
	{
		byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
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
		DiscordChannel channel = Core.getDiscordChannel();

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
		DiscordUser u = new DiscordUser(Config.USER_ID, null, null, null, null);
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
		CreateParams params = new CreateParams();
		DiscordEventHandler handler = new DiscordEventHandler();
		params.registerEventHandler(handler);
		params.setClientID(Config.CLIENT_ID);
		Core core = new Core(params);

		handler.addListener(new DiscordEventAdapter()
		{
			@Override
			public void onCurrentUserUpdate()
			{
				System.out.println(core.userManager().getCurrentUser());
			}
		});
		core.userManager().getUser(Config.RELATIONSHIP_ID, (r,u)->{
			System.out.println(r+" "+u);
		});

		Activity activity = new Activity();
		activity.setState("test");
		activity.party().setID("1234");
		activity.party().size().setCurrentSize(10);
		activity.party().size().setMaxSize(100);

		activity.secrets().setMatchSecret("match");
		activity.secrets().setJoinSecret("join1");
		activity.secrets().setSpectateSecret("spectate");
		core.activityManager().updateActivity(activity, r->{
			System.out.println(r);
			core.activityManager().sendInvite(Config.RELATIONSHIP_ID, ActivityActionType.JOIN, "Join me baka!");
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
