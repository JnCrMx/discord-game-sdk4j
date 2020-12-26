package de.jcm.discordgamesdk;

import de.jcm.discordgamesdk.activity.Activity;
import de.jcm.discordgamesdk.activity.ActivityActionType;
import de.jcm.discordgamesdk.activity.ActivityType;
import de.jcm.discordgamesdk.image.ImageHandle;
import de.jcm.discordgamesdk.image.ImageType;
import de.jcm.discordgamesdk.lobby.*;
import de.jcm.discordgamesdk.user.DiscordUser;
import de.jcm.discordgamesdk.user.Relationship;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class DiscordTest
{
	/* TODO: Find Java 8 replacement
	@BeforeAll
	static void checkPreconditions()
	{
		Predicate<? super ProcessHandle> processDetector = process ->
		{
			if(!process.isAlive())
				return false;

			String command = process.info().command().orElse("");
			if(command.endsWith("\\Discord.exe"))
				return true;
			if(command.endsWith("/discord"))
				return true;
			if(command.equals("discord"))
				return true;
			if(command.endsWith("/Discord"))
				return true;
			if(command.equals("Discord"))
				return true;

			return false;
		};
		Assumptions.assumeTrue(ProcessHandle.allProcesses().anyMatch(processDetector),
		                       "Discord is not running");
	}
	*/

	@BeforeAll
	static void initCore()
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
	}

	@Test
	void activityTest()
	{
		try(CreateParams params = new CreateParams())
		{
			params.setClientID(698611073133051974L);
			Assertions.assertEquals(698611073133051974L, params.getClientID());

			params.setFlags(CreateParams.getDefaultFlags());
			Assertions.assertEquals(CreateParams.getDefaultFlags(), params.getFlags());

			try(Core core = new Core(params))
			{
				try(Activity activity = new Activity())
				{
					activity.setState("World!");
					Assertions.assertEquals("World!", activity.getState());

					activity.setDetails("Hello");
					Assertions.assertEquals("Hello", activity.getDetails());

					activity.setType(ActivityType.PLAYING);
					Assertions.assertEquals(ActivityType.PLAYING, activity.getType());

					activity.party().setID("1234");
					Assertions.assertEquals("1234", activity.party().getID());

					activity.party().size().setCurrentSize(10);
					activity.party().size().setMaxSize(100);

					activity.timestamps().setStart(Instant.now());
					activity.timestamps().setEnd(Instant.now().plusSeconds(16));

					activity.assets().setLargeImage("test");
					activity.assets().setLargeText("Just a test!");

					activity.assets().setSmallImage("test");
					activity.assets().setSmallText("It's a TEST!!!");

					activity.secrets().setMatchSecret("match");
					activity.secrets().setJoinSecret("join");
					activity.secrets().setSpectateSecret("spectate");

					activity.setInstance(true);

					core.activityManager().updateActivity(activity, result->
					{
						Assertions.assertEquals(Result.OK, result,
						                        "update_activity failed.");

						core.activityManager().sendInvite(691614879399936078L,
						                                  ActivityActionType.SPECTATE,
						                                  "Join me baka!");
					});
				}
				Assertions.assertEquals(Result.OK, core.activityManager().registerCommand("cmd.exe"),
				                        "register_command failed");
				Assertions.assertEquals(Result.OK, core.activityManager().registerSteam(1234),
				                        "register_steam failed");

				for(int i=0; i<1000; i++)
				{
					if(i==500)
					{
						core.activityManager().clearActivity();
					}

					core.runCallbacks();
					try
					{
						Thread.sleep(16);
					}
					catch(InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Test
	void eventTest()
	{
		try(CreateParams params = new CreateParams())
		{
			params.setClientID(698611073133051974L);
			params.registerEventHandler(new DiscordEventAdapter()
			{
				@Override
				public void onActivitySpectate(String secret)
				{
					System.out.println("DiscordTest.onActivitySpectate");
					System.out.println("secret = " + secret);
				}

				@Override
				public void onActivityJoinRequest(DiscordUser user)
				{
					System.out.println("DiscordTest.onActivityJoinRequest");
					System.out.println("user = " + user);
				}

				@Override
				public void onOverlayToggle(boolean locked)
				{
					System.out.println("DiscordTest.onOverlayToggle");
					System.out.println("locked = " + locked);
				}
			});

			try(Core core = new Core(params))
			{
				try(Activity activity = new Activity())
				{
					activity.party().setID("1234");
					activity.party().size().setCurrentSize(10);
					activity.party().size().setMaxSize(100);

					activity.secrets().setMatchSecret("match");
					activity.secrets().setJoinSecret("join");
					activity.secrets().setSpectateSecret("spectate");

					core.activityManager().updateActivity(activity);
				}

				for(int i=0; i<1000; i++)
				{
					core.runCallbacks();
					try
					{
						Thread.sleep(16);
					}
					catch(InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Test
	void userTest()
	{
		try(CreateParams params = new CreateParams())
		{
			// We sadly need this rather ugly reference to access the Core in our EventAdapter.
			AtomicReference<Core> coreRef = new AtomicReference<>();
			// Goals to quit when we are done
			AtomicBoolean receivedCurrentUser = new AtomicBoolean(false);
			AtomicBoolean receivedUser = new AtomicBoolean(false);

			params.setClientID(698611073133051974L);
			params.registerEventHandler(new DiscordEventAdapter()
			{
				@Override
				public void onCurrentUserUpdate()
				{
					DiscordUser currentUser = coreRef.get().userManager().getCurrentUser();
					Assertions.assertNotNull(currentUser, "get_current_user returned null.");

					receivedCurrentUser.set(true);
				}
			});
			try(Core core = new Core(params))
			{
				coreRef.set(core);
				/*
				 Calling it directly after initialization of the Core will fail
				 because onCurrentUserUpdate() hasn't been fired at this point.
				 */
				Assertions.assertThrows(GameSDKException.class, ()->core.userManager().getCurrentUser(),
				                        "We can access the current user too early. This is weird.");

				Assertions.assertDoesNotThrow(()->core.userManager().getCurrentUserPremiumType(),
				                              "get_current_user_premium_type failed.");
				Assertions.assertDoesNotThrow(()->core.userManager().currentUserHasFlag(UserManager.USER_FLAG_PARTNER),
				                              "current_user_has_flag failed.");

				long userId = 352386023159758848L;
				core.userManager().getUser(userId, (result, user) ->
				{
					Assertions.assertEquals(Result.OK, result, "get_user failed.");
					Assertions.assertNotNull(user, "get_user returned null.");
					Assertions.assertEquals(userId, user.getUserId(), "get_user did not return correct user ID.");

					receivedUser.set(true);
				});

				for(int i=0; i<2000 &&
						// Quit when we are done
						!(
								receivedCurrentUser.get() &&
								receivedUser.get()
						); i++)
				{
					core.runCallbacks();
					try
					{
						Thread.sleep(16);
					}
					catch(InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				Assertions.assertTrue(receivedUser.get(),
				                      "Did not receive information about a user.");
				Assertions.assertTrue(receivedCurrentUser.get(),
				                      "Did not receive information about the current user.");
			}
		}
	}

	@Test
	void overlayTest()
	{
		try(CreateParams params = new CreateParams())
		{
			params.setClientID(698611073133051974L);
			try(Core core = new Core(params))
			{
				Assertions.assertDoesNotThrow(()->core.overlayManager().isEnabled(),
				                              "is_enabled failed.");
				boolean locked = core.overlayManager().isLocked();
				core.overlayManager().setLocked(!locked, result->
				{
					Assertions.assertEquals(Result.OK, result, "set_locked failed.");
					boolean newLocked = core.overlayManager().isLocked();
					Assertions.assertEquals(!locked, newLocked, "locked did not change.");
				});

				AtomicBoolean openActivityInviteComplete = new AtomicBoolean(false);
				AtomicBoolean openGuildInviteComplete = new AtomicBoolean(false);
				AtomicBoolean openVoiceSettingsComplete = new AtomicBoolean(false);

				try(Activity activity = new Activity())
				{
					activity.party().setID("1234");
					activity.party().size().setCurrentSize(10);
					activity.party().size().setMaxSize(100);

					activity.secrets().setMatchSecret("match");
					activity.secrets().setJoinSecret("join");
					activity.secrets().setSpectateSecret("spectate");

					core.activityManager().updateActivity(activity, result->
					{
						Assumptions.assumeTrue(result == Result.OK,
						                       "Cannot set activity.");
						core.overlayManager().openActivityInvite(ActivityActionType.SPECTATE, r->
						{
							Assertions.assertEquals(Result.OK, r, "open_activity_invite failed.");
							openActivityInviteComplete.set(true);
						});
						core.overlayManager().openGuildInvite("discord-developers", r->
						{
							Assertions.assertEquals(Result.OK, r, "open_guild_invite failed.");
							openGuildInviteComplete.set(true);
						});
						core.overlayManager().openVoiceSettings(r-> {
							Assertions.assertEquals(Result.OK, r, "open_voice_settings failed.");
							openVoiceSettingsComplete.set(true);
						});
					});
				}

				for(int i=0; i<1000 &&
						// Quit when we are done
						!(
								openActivityInviteComplete.get() &&
								openGuildInviteComplete.get() &&
								openVoiceSettingsComplete.get()
						); i++)
				{
					core.runCallbacks();
					try
					{
						Thread.sleep(16);
					}
					catch(InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Test
	void relationshipTest()
	{
		try(CreateParams params = new CreateParams())
		{
			// We sadly need this rather ugly reference to access the Core in our EventAdapter.
			AtomicReference<Core> coreRef = new AtomicReference<>();

			params.setClientID(698611073133051974L);
			params.registerEventHandler(new DiscordEventAdapter()
			{
				@Override
				public void onRelationshipRefresh()
				{
					System.out.println("DiscordTest.onRelationshipRefresh");

					long userId = 691614879399936078L;
					Relationship relationship = coreRef.get()
							.relationshipManager().getWith(userId);
					Assertions.assertEquals(userId, relationship.getUser().getUserId(),
					                        "Relationship has wrong user ID.");

					coreRef.get().relationshipManager().filter(RelationshipManager.NO_FILTER);
					coreRef.get().relationshipManager().asList().forEach(System.out::println);
				}

				@Override
				public void onRelationshipUpdate(Relationship relationship)
				{
					System.out.println("DiscordTest.onRelationshipUpdate");
					System.out.println(relationship);
				}
			});
			try(Core core = new Core(params))
			{
				coreRef.set(core);

				for(int i = 0; i < 1000; i++)
				{
					core.runCallbacks();
					try
					{
						Thread.sleep(16);
					}
					catch(InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Test
	void imageTest()
	{
		try(CreateParams params = new CreateParams())
		{
			params.setClientID(698611073133051974L);
			try(Core core = new Core(params))
			{
				ImageHandle handle = new ImageHandle(ImageType.USER, 691614879399936078L, 256);
				core.imageManager().fetch(handle, false, (result, handle1)->
				{
					Assertions.assertEquals(Result.OK, result,
					                        "fetch failed.");

					BufferedImage image = core.imageManager().getAsBufferedImage(handle);
					try
					{
						ImageIO.write(image, "png", new File("test.png"));
					}
					catch(IOException e)
					{
						e.printStackTrace();
					}
				});

				for(int i = 0; i < 1000; i++)
				{
					core.runCallbacks();
					try
					{
						Thread.sleep(16);
					}
					catch(InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Test
	void lobbyTest()
	{
		try(CreateParams params = new CreateParams())
		{
			AtomicReference<Core> coreRef = new AtomicReference<>();
			params.setClientID(698611073133051974L);
			params.registerEventHandler(new DiscordEventAdapter()
			{
				@Override
				public void onLobbyUpdate(long lobbyId)
				{
					System.out.println("Lobby "+lobbyId+" updated");
					System.out.println(coreRef.get().lobbyManager().getLobbyMetadata(lobbyId));
				}

				@Override
				public void onLobbyDelete(long lobbyId, int reason)
				{
					System.out.println("Lobby "+lobbyId+" deleted because of "+reason);
				}

				@Override
				public void onMemberConnect(long lobbyId, long userId)
				{
					System.out.println("User "+userId+" connected to lobby "+lobbyId);
				}

				@Override
				public void onMemberUpdate(long lobbyId, long userId)
				{
					System.out.println("User "+userId+" in lobby "+lobbyId+" updated");
					System.out.println(coreRef.get().lobbyManager().getMemberMetadata(lobbyId, userId));
				}

				@Override
				public void onMemberDisconnect(long lobbyId, long userId)
				{
					System.out.println("User "+userId+" disconnected from lobby "+lobbyId);
				}

				@Override
				public void onLobbyMessage(long lobbyId, long userId, byte[] data)
				{
					System.out.println("Message in "+lobbyId+" from "+userId+": "+data.length+" bytes \""+new String(data)+"\"");
				}

				@Override
				public void onNetworkMessage(long lobbyId, long userId, byte channelId, byte[] data)
				{
					System.out.println("Message in "+lobbyId+" channel "+channelId+" from "+userId+": "+data.length+" bytes");
				}
			});
			try(Core core = new Core(params))
			{
				coreRef.set(core);

				LobbyTransaction txn = core.lobbyManager().getLobbyCreateTransaction();
				txn.setType(LobbyType.PUBLIC);
				txn.setCapacity(10);
				txn.setMetadata("test", "1234");
				txn.setLocked(false);
				core.lobbyManager().createLobby(txn, (result, lobby) -> {
					Assertions.assertEquals(Result.OK, result, "create_lobby failed");

					System.out.println(lobby);

					Assertions.assertEquals(LobbyType.PUBLIC, lobby.getType(), "wrong type");
					Assertions.assertEquals(10, lobby.getCapacity(), "wrong capacity");
					Assertions.assertEquals("1234",
					                        core.lobbyManager().getLobbyMetadataValue(lobby, "test"),
					                        "wrong metadata");
					System.out.println(core.lobbyManager().getLobbyMetadata(lobby));
					Assertions.assertEquals(false, lobby.isLocked(), "wrong lock state");

					System.out.println(core.lobbyManager().getMemberUsers(lobby));

					LobbyTransaction updateTxn = core.lobbyManager().getLobbyUpdateTransaction(lobby);
					updateTxn.setCapacity(100);
					core.lobbyManager().updateLobby(lobby, updateTxn, result1 -> {
						Assertions.assertEquals(Result.OK, result1, "update_lobby failed");
						/*core.lobbyManager().deleteLobby(lobby, result2 -> {
							Assertions.assertEquals(Result.OK, result2, "delete_lobby failed");
						});*/
					});

					LobbyMemberTransaction memberTxn = core.lobbyManager().getMemberUpdateTransaction(lobby, lobby.getOwnerId());
					memberTxn.setMetadata("test", "12345678");
					core.lobbyManager().updateMember(lobby, lobby.getOwnerId(), memberTxn, result1 -> {
						Assertions.assertEquals(Result.OK, result1, "update_lobby failed");
					});

					core.lobbyManager().sendLobbyMessage(lobby, "Hello World".getBytes(StandardCharsets.UTF_8), result1 -> {
						Assertions.assertEquals(Result.OK, result1, "send_lobby_message failed");
					});

					Assertions.assertDoesNotThrow(()->core.lobbyManager().connectNetwork(lobby),
							"connect_network failed");

					core.lobbyManager().openNetworkChannel(lobby, (byte) 0, true);

					//Assertions.assertDoesNotThrow(()->core.lobbyManager().disconnectNetwork(lobby), "disconnect_network failed");
				});

				for(int i = 0; i < 10000; i++)
				{
					core.runCallbacks();
					try
					{
						Thread.sleep(16);
					}
					catch(InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Test
	void lobbyTest2()
	{
		try(CreateParams params = new CreateParams())
		{
			params.setClientID(698611073133051974L);
			try(Core core = new Core(params))
			{
				LobbySearchQuery searchQuery = core.lobbyManager().getSearchQuery()
						.filter("metadata.test", LobbySearchQuery.Comparison.EQUAL, LobbySearchQuery.Cast.NUMBER, "1234")
						.limit(10);
				core.lobbyManager().search(searchQuery, r->{
					Assertions.assertEquals(Result.OK, r, "search failed");

					List<Lobby> list = core.lobbyManager().getLobbies();
					list.forEach(System.out::println);

					Optional<Lobby> lobbyOpt = list.stream().filter(l->!l.isLocked()).findAny();
					Assumptions.assumeTrue(lobbyOpt.isPresent(), "no suitable lobby found");

					Lobby lobby = lobbyOpt.get();
					core.lobbyManager().connectLobby(lobby.getId(), lobby.getSecret(), (result, lobby1) -> {
						Assertions.assertEquals(Result.OK, result, "connect_lobby failed");

						core.lobbyManager().connectNetwork(lobby);
						core.lobbyManager().openNetworkChannel(lobby, (byte) 0, true);
						core.lobbyManager().sendNetworkMessage(lobby, lobby.getOwnerId(), (byte) 0,
								"Hallo".getBytes(StandardCharsets.UTF_8));
						core.lobbyManager().flushNetwork();
					});
				});

				for(int i = 0; i < 1000; i++)
				{
					core.runCallbacks();
					try
					{
						Thread.sleep(16);
					}
					catch(InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}
}
