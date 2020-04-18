import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.DiscordEventAdapter;
import de.jcm.discordgamesdk.RelationshipManager;
import de.jcm.discordgamesdk.activity.Activity;
import de.jcm.discordgamesdk.user.Relationship;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

/**
 * <p>An example how to obtain information about the user's relationships.</p>
 * <p>We set an activity and use its party size property to show how many of our friends are online and
 * update this whenever a friend's online status changes.</p>
 */
public class RelationshipExample
{
	// use an attribute for storing the Core, so we can use it in our event handler
	private static Core core;

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

			Activity activity = new Activity();

			// Register the event handle to handle relationship-related events
			params.registerEventHandler(new DiscordEventAdapter()
			{
				@Override
				public void onRelationshipRefresh()
				{
					// for debugging
					System.out.println("RelationshipExample.onRelationshipRefresh");

					// We are now ready to read information about relationships

					// filter for all our friends
					core.relationshipManager().filter(RelationshipManager.FRIEND_FILTER);
					int friendCount = core.relationshipManager().count(); // get how many relationships match our filter

					// filter for all our online friends (previous filter is reset automatically)
					core.relationshipManager().filter(RelationshipManager.FRIEND_FILTER.and(RelationshipManager.ONLINE_FILTER));
					int onlineFriendCount = core.relationshipManager().count(); // get how many relationships match our filter

					activity.setDetails("Chilling with my");
					activity.setState("online friends");
					// set the party size, so it will show up as (online count / total count)
					activity.party().size().setCurrentSize(onlineFriendCount);
					activity.party().size().setMaxSize(friendCount);
					// set the start, so the activity shows how much time passed since last update
					activity.timestamps().setStart(Instant.now());

					// update the user's activity
					core.activityManager().updateActivity(activity);
				}

				@Override
				public void onRelationshipUpdate(Relationship relationship)
				{
					// for debugging
					System.out.println("RelationshipExample.onRelationshipUpdate");
					System.out.println("relationship = " + relationship);

					// A relationship has changed -> update activity by calling onRelationshipRefresh manually
					onRelationshipRefresh();
				}
			});

			// Create the Core
			core = new Core(params);

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
