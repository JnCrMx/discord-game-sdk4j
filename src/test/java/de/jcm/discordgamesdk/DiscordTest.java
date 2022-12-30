package de.jcm.discordgamesdk;

import de.jcm.discordgamesdk.activity.Activity;
import de.jcm.discordgamesdk.activity.ActivityActionType;
import de.jcm.discordgamesdk.activity.ActivityType;
import de.jcm.discordgamesdk.image.ImageHandle;
import de.jcm.discordgamesdk.image.ImageType;
import de.jcm.discordgamesdk.user.DiscordUser;
import de.jcm.discordgamesdk.user.Relationship;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

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

	@Test
	void activityTest()
	{
		try(CreateParams params = new CreateParams())
		{
			params.setClientID(Config.CLIENT_ID);
			Assertions.assertEquals(Config.CLIENT_ID, params.getClientID());

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

						core.activityManager().sendInvite(Config.RELATIONSHIP_ID,
						                                  ActivityActionType.JOIN,
						                                  "Join me baka!");
					});
				}
				/*Assertions.assertEquals(Result.OK, core.activityManager().registerCommand("cmd.exe"),
				                        "register_command failed");
				Assertions.assertEquals(Result.OK, core.activityManager().registerSteam(1234),
				                        "register_steam failed");*/

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
			params.setClientID(Config.CLIENT_ID);
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

			params.setClientID(Config.CLIENT_ID);
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

				/*Assertions.assertDoesNotThrow(()->core.userManager().getCurrentUserPremiumType(),
				                              "get_current_user_premium_type failed.");*/
				Assertions.assertDoesNotThrow(()->core.userManager().currentUserHasFlag(UserManager.USER_FLAG_PARTNER),
				                              "current_user_has_flag failed.");

				core.userManager().getUser(Config.USER_ID, (result, user) ->
				{
					Assertions.assertEquals(Result.OK, result, "get_user failed.");
					Assertions.assertNotNull(user, "get_user returned null.");
					Assertions.assertEquals(Config.USER_ID, user.getUserId(), "get_user did not return correct user ID.");

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
			params.setClientID(Config.CLIENT_ID);
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
						core.overlayManager().openActivityInvite(ActivityActionType.JOIN, r->
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
				Assertions.assertTrue(openActivityInviteComplete.get(), "openActivityInvite did not complete");
				Assertions.assertTrue(openGuildInviteComplete.get(), "open_guild_invite did not complete");
				Assertions.assertTrue(openVoiceSettingsComplete.get(), "open_voice_settings did not complete");
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

			params.setClientID(Config.CLIENT_ID);
			params.registerEventHandler(new DiscordEventAdapter()
			{
				@Override
				public void onRelationshipRefresh()
				{
					System.out.println("DiscordTest.onRelationshipRefresh");

					Relationship relationship = coreRef.get()
							.relationshipManager().getWith(Config.RELATIONSHIP_ID);
					Assertions.assertEquals(Config.RELATIONSHIP_ID, relationship.getUser().getUserId(),
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
			params.setClientID(Config.CLIENT_ID);
			try(Core core = new Core(params))
			{
				AtomicBoolean complete = new AtomicBoolean(false);

				ImageHandle handle = new ImageHandle(ImageType.USER, Config.RELATIONSHIP_ID, 256);
				core.imageManager().fetch(handle, false, (result, handle1)->
				{
					Assertions.assertEquals(Result.OK, result,
					                        "fetch failed.");

					BufferedImage image = core.imageManager().getAsBufferedImage(handle);
					Assertions.assertDoesNotThrow(()->ImageIO.write(image, "png", new File("test.png")));
					complete.set(true);
				});

				for(int i = 0; i < 1000 && !complete.get(); i++)
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
				Assertions.assertTrue(complete.get(), "Did not complete in time");
			}
		}
	}

	@Test
	void voiceTest()
	{
		try(CreateParams params = new CreateParams())
		{
			params.setClientID(Config.CLIENT_ID);
			try(Core core = new Core(params))
			{
				/*VoiceInputMode inputMode = new VoiceInputMode(
						VoiceInputMode.InputModeType.PUSH_TO_TALK,
						Integer.toString((int) (System.currentTimeMillis()%10))); // use pseudo-random shortcut

				core.voiceManager().setInputMode(inputMode, r->{
					Assertions.assertEquals(Result.OK, r, "set_input_mode failed");

					VoiceInputMode inputMode2 = core.voiceManager().getInputMode();
					Assertions.assertEquals(inputMode, inputMode2, "input mode not correct");
				});

				core.overlayManager().openVoiceSettings(r->{
					Assumptions.assumeTrue(r==Result.OK, "open_voice_settings failed: "+r);

					VoiceInputMode mode = core.voiceManager().getInputMode();
					System.out.println(mode);
				});*/

				boolean selfMute = core.voiceManager().isSelfMute();
				core.voiceManager().setSelfMute(!selfMute);
				boolean selfMute2 = core.voiceManager().isSelfMute();
				Assertions.assertNotEquals(selfMute, selfMute2, "self mute did not change");

				long testUid = 0;

				Assertions.assertFalse(core.voiceManager().isLocalMute(testUid));
				core.voiceManager().setLocalMute(testUid, true);
				Assertions.assertTrue(core.voiceManager().isLocalMute(testUid));

				Assertions.assertEquals(100, core.voiceManager().getLocalVolume(testUid));
				core.voiceManager().setLocalVolume(testUid, 200);
				Assertions.assertEquals(200, core.voiceManager().getLocalVolume(testUid));

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
