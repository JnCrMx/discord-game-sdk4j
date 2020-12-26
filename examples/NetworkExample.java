import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.DiscordEventAdapter;
import de.jcm.discordgamesdk.lobby.*;
import de.jcm.discordgamesdk.user.DiscordUser;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NetworkExample extends DiscordEventAdapter
{
	private CreateParams createParams;
	private Core core;

	private DiscordUser currentUser;
	private Lobby lobby;
	private long myPeerId;
	private String myRoute;

	private List<Long> openConnections = new ArrayList<>();

	public NetworkExample() throws InterruptedException
	{
		createParams = new CreateParams();
		createParams.setClientID(698611073133051974L);
		createParams.registerEventHandler(this);

		core = new Core(createParams);

		int i = 0;
		for(long l = 0; ; l = (l + 1) % 100)
		{
			core.runCallbacks();
			Thread.sleep(16);

			if(l == 0)
			{
				for(long peer : openConnections)
				{
					core.networkManager().sendMessage(peer, (byte) 1,
							(i+":"+System.currentTimeMillis()).getBytes(StandardCharsets.UTF_8));
				}
				core.networkManager().flush();
				i++;
			}
		}
	}

	@Override
	public void onMessage(long peerId, byte channelId, byte[] data)
	{
		long receiveTime = System.currentTimeMillis();
		System.out.println("Message from "+Long.toUnsignedString(peerId)+" on channel "+channelId);
		
		String msg = new String(data, StandardCharsets.UTF_8);
		String[] parts = msg.split(":");

		String index = parts[0];

		long sendTime = Long.parseLong(parts[1]);
		long ping = receiveTime - sendTime;
		System.out.println(index+": latency = "+ping);
	}

	@Override
	public void onRouteUpdate(String routeData)
	{
		myRoute = routeData;
		if(lobby != null)
		{
			LobbyMemberTransaction txn = core.lobbyManager().getMemberUpdateTransaction(lobby, currentUser.getUserId());
			txn.setMetadata("route", routeData);
			core.lobbyManager().updateMember(lobby, currentUser.getUserId(), txn, result -> {
				System.out.println(core.lobbyManager().getMemberMetadata(lobby, currentUser.getUserId()));
			});
		}
	}

	@Override
	public void onMemberUpdate(long lobbyId, long userId)
	{
		if(userId == currentUser.getUserId())
			return;

		Map<String, String> metadata = core.lobbyManager().getMemberMetadata(lobby, userId);
		if(metadata.containsKey("peer_id") && metadata.containsKey("route"))
		{
			long peerId = Long.parseUnsignedLong(metadata.get("peer_id"));
			String route = metadata.get("route");

			if(openConnections.contains(userId))
			{
				System.out.println("Updating route for " + Long.toUnsignedString(peerId) + ": " + route);
				core.networkManager().updatePeer(peerId, route);
			}
			else
			{
				System.out.println("Connecting to " + Long.toUnsignedString(peerId) + " over " + route);
				core.networkManager().openPeer(peerId, route);

				System.out.println("Opening reliable channel 1");
				core.networkManager().openChannel(peerId, (byte) 1, true);

				openConnections.add(peerId);
			}
		}
	}

	private void initNetwork()
	{
		myPeerId = core.networkManager().getPeerId();

		LobbyMemberTransaction txn = core.lobbyManager().getMemberUpdateTransaction(lobby, currentUser.getUserId());
		txn.setMetadata("peer_id", Long.toUnsignedString(myPeerId));

		if(myRoute != null)
		{
			txn.setMetadata("route", myRoute);
		}

		core.lobbyManager().updateMember(lobby, currentUser.getUserId(), txn, result -> {
			System.out.println(core.lobbyManager().getMemberMetadata(lobby, currentUser.getUserId()));
		});

		List<Long> users = core.lobbyManager().getMemberUserIds(lobby);
		for(Long userId : users)
		{
			if(userId == currentUser.getUserId())
				continue;

			Map<String, String> metadata = core.lobbyManager().getMemberMetadata(lobby, userId);
			if(metadata.containsKey("peer_id") && metadata.containsKey("route"))
			{
				long peerId = Long.parseUnsignedLong(metadata.get("peer_id"));
				String route = metadata.get("route");

				System.out.println("Connecting to "+Long.toUnsignedString(peerId)+" over "+route);
				core.networkManager().openPeer(peerId, route);

				System.out.println("Opening reliable channel 1");
				core.networkManager().openChannel(peerId, (byte) 1, true);

				openConnections.add(peerId);
			}
		}
	}

	private void letsGo()
	{
		LobbySearchQuery query = core.lobbyManager().getSearchQuery();
		core.lobbyManager().search(query, r->{
			if(core.lobbyManager().lobbyCount() == 0)
			{
				LobbyTransaction txn = core.lobbyManager().getLobbyCreateTransaction();
				txn.setType(LobbyType.PUBLIC);
				txn.setCapacity(10);
				core.lobbyManager().createLobby(txn, lobby -> {
					this.lobby = lobby;

					initNetwork();
				});
			}
			else
			{
				Lobby lobby = core.lobbyManager().getLobbies().get(0);
				core.lobbyManager().connectLobby(lobby.getId(), lobby.getSecret(), (result, lobby1) -> {
					this.lobby = lobby;

					initNetwork();
				});
			}
		});
	}

	@Override
	public void onCurrentUserUpdate()
	{
		boolean ready = this.currentUser == null;
		this.currentUser = core.userManager().getCurrentUser();

		if(ready)
		{
			letsGo();
		}
	}

	public static void main(String[] args)
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

		try
		{
			new NetworkExample();
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
