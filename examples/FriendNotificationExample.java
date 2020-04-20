import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.DiscordEventAdapter;
import de.jcm.discordgamesdk.RelationshipManager;
import de.jcm.discordgamesdk.activity.ActivityType;
import de.jcm.discordgamesdk.user.DiscordUser;
import de.jcm.discordgamesdk.user.Relationship;
import de.jcm.discordgamesdk.user.RelationshipType;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * <p>Example to show a notification when a friend starts or stops playing a game.</p>
 * <p>This example makes use of the following components:
 * <ul>
 *     <li>{@link de.jcm.discordgamesdk.RelationshipManager}</li>
 * </ul></p>
 */
public class FriendNotificationExample extends DiscordEventAdapter
{
	// use an attribute for storing the Core, so we can use it in our event handling methods
	private Core core;

	private TrayIcon trayIcon;

	public FriendNotificationExample()
	{
		CreateParams params = new CreateParams();
		params.setClientID(698611073133051974L);
		params.setFlags(CreateParams.getDefaultFlags());

		// We extend DiscordEventAdapter and therefore are the event handler
		params.registerEventHandler(this);

		// Create the Core
		core = new Core(params);

		SystemTray tray = SystemTray.getSystemTray();

		trayIcon = new TrayIcon(new BufferedImage(1,1, BufferedImage.TYPE_4BYTE_ABGR));
		try
		{
			tray.add(trayIcon);
		}
		catch(AWTException e)
		{
			e.printStackTrace();
		}
	}

	public void run()
	{
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

	private HashMap<Long, Relationship> cache = new HashMap<>();

	@Override
	public void onRelationshipRefresh()
	{
		core.relationshipManager().filter(RelationshipManager.FRIEND_FILTER);

		core.relationshipManager().asList().forEach(r->{
			cache.put(r.getUser().getUserId(), r);
		});

		// release the Relationships put into filter Predicate
		System.gc();
	}

	// Fires when a friend's status (presence, activity) changes
	@Override
	public void onRelationshipUpdate(Relationship relationship)
	{
		if(relationship.getType()== RelationshipType.FRIEND)
		{
			Relationship previous = cache.get(relationship.getUser().getUserId());

			DiscordUser user = relationship.getUser();

			// find differences

			// online status
			if(previous.getPresence().getStatus() != relationship.getPresence().getStatus())
			{
				String message = null;
				switch(relationship.getPresence().getStatus())
				{
					case OFFLINE:
						message = "went offline";
						break;
					case ONLINE:
						message = "is now online";
						break;
					case IDLE:
						message = "went AFK";
						break;
					case DO_NO_DISTURB:
						message = "does not want to be disturbed";
						break;
				}

				trayIcon.displayMessage(String.format("%s#%s", user.getUsername(), user.getDiscriminator()),
				                        message, TrayIcon.MessageType.NONE);
			}

			// playing
			if(relationship.getPresence().getActivity().getType() == ActivityType.PLAYING &&
					previous.getPresence().getActivity().getApplicationId() == 0 &&
					relationship.getPresence().getActivity().getApplicationId() != 0)
			{
				trayIcon.displayMessage(String.format("%s#%s", user.getUsername(), user.getDiscriminator()),
				                        String.format("started playing %s", relationship.getPresence().getActivity().getName()),
				                        TrayIcon.MessageType.NONE);
			}
			else if(previous.getPresence().getActivity().getType() == ActivityType.PLAYING &&
					previous.getPresence().getActivity().getApplicationId() != 0 &&
					relationship.getPresence().getActivity().getApplicationId() == 0)
			{
				trayIcon.displayMessage(String.format("%s#%s", user.getUsername(), user.getDiscriminator()),
				                        String.format("stopped playing %s", previous.getPresence().getActivity().getName()),
				                        TrayIcon.MessageType.NONE);
			}
			else if(previous.getPresence().getActivity().getType() == ActivityType.PLAYING &&
					relationship.getPresence().getActivity().getType() == ActivityType.PLAYING &&
					previous.getPresence().getActivity().getApplicationId() !=
						relationship.getPresence().getActivity().getApplicationId())
			{
				trayIcon.displayMessage(String.format("%s#%s", user.getUsername(), user.getDiscriminator()),
				                        String.format("stopped playing %s and started playing %s",
				                                      previous.getPresence().getActivity().getName(),
				                                      relationship.getPresence().getActivity().getName()),
				                        TrayIcon.MessageType.NONE);
			}

			// streaming
			if(relationship.getPresence().getActivity().getType() == ActivityType.STREAMING &&
					previous.getPresence().getActivity().getApplicationId() == 0 &&
					relationship.getPresence().getActivity().getApplicationId() != 0)
			{
				trayIcon.displayMessage(String.format("%s#%s", user.getUsername(), user.getDiscriminator()),
				                        String.format("started streaming %s", relationship.getPresence().getActivity().getName()),
				                        TrayIcon.MessageType.NONE);
			}
			else if(previous.getPresence().getActivity().getType() == ActivityType.STREAMING &&
					previous.getPresence().getActivity().getApplicationId() != 0 &&
					relationship.getPresence().getActivity().getApplicationId() == 0)
			{
				trayIcon.displayMessage(String.format("%s#%s", user.getUsername(), user.getDiscriminator()),
				                        String.format("stopped streaming %s", previous.getPresence().getActivity().getName()),
				                        TrayIcon.MessageType.NONE);
			}
			else if(previous.getPresence().getActivity().getType() == ActivityType.STREAMING &&
					relationship.getPresence().getActivity().getType() == ActivityType.STREAMING &&
					previous.getPresence().getActivity().getApplicationId() !=
							relationship.getPresence().getActivity().getApplicationId())
			{
				trayIcon.displayMessage(String.format("%s#%s", user.getUsername(), user.getDiscriminator()),
				                        String.format("stopped streaming %s and started streaming %s",
				                                      previous.getPresence().getActivity().getName(),
				                                      relationship.getPresence().getActivity().getName()),
				                        TrayIcon.MessageType.NONE);
			}

			// listening
			if(previous.getPresence().getActivity().getType() != ActivityType.LISTENING &&
					relationship.getPresence().getActivity().getType() == ActivityType.LISTENING)
			{
				trayIcon.displayMessage(String.format("%s#%s", user.getUsername(), user.getDiscriminator()),
				                        String.format("started listening to %s", relationship.getPresence().getActivity().getName()),
				                        TrayIcon.MessageType.NONE);
			}
			else if(previous.getPresence().getActivity().getType() == ActivityType.LISTENING &&
					relationship.getPresence().getActivity().getType() != ActivityType.LISTENING)
			{
				trayIcon.displayMessage(String.format("%s#%s", user.getUsername(), user.getDiscriminator()),
				                        String.format("stopped listening to %s", previous.getPresence().getActivity().getName()),
				                        TrayIcon.MessageType.NONE);
			}

			cache.replace(relationship.getUser().getUserId(), relationship);

			// release the Relationship we just replaced
			System.gc();
		}
	}

	public static void main(String[] args)
	{
		try
		{
			File discordLibrary = DownloadNativeLibrary.downloadDiscordLibrary();
			if(discordLibrary == null)
			{
				System.err.println("Error downloading Discord SDK.");
				System.exit(-1);
			}
			// Initialize the Core
			Core.init(discordLibrary);
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.err.println("Error downloading Discord SDK.");
			System.exit(-1);
		}

		FriendNotificationExample example = new FriendNotificationExample();
		example.run();
	}
}
