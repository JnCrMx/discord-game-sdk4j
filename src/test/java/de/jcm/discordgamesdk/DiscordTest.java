package de.jcm.discordgamesdk;

import de.jcm.discordgamesdk.activity.Activity;
import de.jcm.discordgamesdk.activity.ActivityType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Instant;
import java.util.function.Predicate;

public class DiscordTest
{
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

	@Test
	void activityTest()
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
}
