import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.GameSDKException;
import de.jcm.discordgamesdk.Result;
import de.jcm.discordgamesdk.lobby.Lobby;
import de.jcm.discordgamesdk.lobby.LobbySearchQuery;
import de.jcm.discordgamesdk.lobby.LobbyTransaction;
import de.jcm.discordgamesdk.lobby.LobbyType;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class VoiceExample
{
	private final CreateParams params;
	private final Core core;

	private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	private ScheduledFuture<?> runCallbacksFuture;

	public VoiceExample()
	{
		this.params = new CreateParams();
		this.params.setFlags(CreateParams.getDefaultFlags());
		this.params.setClientID(698611073133051974L);
		this.core = new Core(params);
	}

	public void start()
	{
		runCallbacksFuture = executor.scheduleAtFixedRate(
				core::runCallbacks, 0, 16, TimeUnit.MILLISECONDS);

		LobbySearchQuery query = core.lobbyManager().getSearchQuery();
		query.filter("metadata.example",
		             LobbySearchQuery.Comparison.EQUAL,
		             LobbySearchQuery.Cast.STRING,
		             "voice");
		System.out.println("Searching for Lobbies...");
		core.lobbyManager().search(query, result->{
			if(result != Result.OK)
			{
				stop(result);
				return;
			}

			List<Lobby> lobbies = core.lobbyManager().getLobbies();

			Optional<Lobby> opt = lobbies.stream()
			       .filter(l->l.getCapacity() > core.lobbyManager().memberCount(l))
			       .findAny();

			if(opt.isPresent())
			{
				System.out.println("Lobby found: "+opt.get().getId());
				core.lobbyManager().connectLobby(opt.get(), this::startVoice);
			}
			else
			{
				System.out.println("No Lobby found; creating one.");

				LobbyTransaction txn = core.lobbyManager().getLobbyCreateTransaction();
				txn.setType(LobbyType.PUBLIC);
				txn.setCapacity(10);
				txn.setLocked(false);
				txn.setMetadata("example", "voice");

				core.lobbyManager().createLobby(txn, this::startVoice);
			}
		});
	}

	private void startVoice(Result result, Lobby lobby)
	{
		System.out.println("Starting voice chat in Lobby "+lobby.getId());
		if(result != Result.OK)
		{
			stop(result);
			return;
		}

		core.lobbyManager().connectVoice(lobby);
		executor.scheduleAtFixedRate(()->{
			for(long uid : core.lobbyManager().getMemberUserIds(lobby))
			{
				if(uid == core.userManager().getCurrentUser().getUserId())
					continue;

				// adjust the volume, so you can hopefully hear a difference when speaking
				int t = (int) (System.currentTimeMillis() / 10.0);
				core.voiceManager().setLocalVolume(uid, t%201);
			}
		}, 1000, 20, TimeUnit.MILLISECONDS);

		core.overlayManager().openVoiceSettings();
	}

	public void stop(Result result)
	{
		new GameSDKException(result).printStackTrace();
		executor.execute(()-> runCallbacksFuture.cancel(false));
	}

	public static void main(String[] args) throws IOException
	{
		VoiceExample example = new VoiceExample();
		example.start();
	}
}
