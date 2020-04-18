import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.activity.Activity;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

/**
 * Example how to set a custom activity.
 */
public class ActivityExample
{
	public static void main(String[] args) throws IOException
	{
		File discordLibrary = DownloadNativeLibrary.downloadDiscordLibrary();
		if(discordLibrary == null)
		{
			System.err.println("Error downloading Discord SDK.");
			System.exit(-1);
		}
		// Initialize the Core
		Core.init(discordLibrary);

		// Set parameters for the Core
		try(CreateParams params = new CreateParams())
		{
			params.setClientID(698611073133051974L);
			params.setFlags(CreateParams.getDefaultFlags());
			// Create the Core
			try(Core core = new Core(params))
			{
				// Create the Activity
				try(Activity activity = new Activity())
				{
					activity.setDetails("Running an example");
					activity.setState("and having fun");

					// Setting a start time causes an "elapsed" field to appear
					activity.timestamps().setStart(Instant.now());

					// We are in a party with 10 out of 100 people.
					activity.party().size().setMaxSize(100);
					activity.party().size().setCurrentSize(10);

					// Make a "cool" image show up
					activity.assets().setLargeImage("test");

					// Setting a join secret and a party ID causes an "Ask to Join" button to appear
					activity.party().setID("Party!");
					activity.secrets().setJoinSecret("Join!");

					// Finally, update the current activity to our activity
					core.activityManager().updateActivity(activity);
				}

				// Run callbacks forever
				while(true)
				{
					core.runCallbacks();
					try
					{
						// Sleep a bit to save CPU
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
