import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class IdResolver
{
	public static void main(String[] args) throws IOException
	{
		// Set parameters for the Core
		try(CreateParams params = new CreateParams())
		{
			params.setClientID(698611073133051974L);
			params.setFlags(CreateParams.getDefaultFlags());
			// Create the Core
			try(Core core = new Core(params))
			{
				AtomicBoolean done = new AtomicBoolean(false);
				Thread t = new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							File inputFile = new File("ids.txt");
							File outputFile = new File("names.txt");

							Scanner scanner = new Scanner(inputFile);
							PrintStream print = new PrintStream(outputFile);

							while(scanner.hasNextLine())
							{
								Object wait = new Object();

								String line = scanner.nextLine();
								long id = Long.parseLong(line);

								core.userManager().getUser(id, (result, user)->
								{
									print.println(user.getUsername()+"#"+user.getDiscriminator());
									synchronized(wait)
									{
										wait.notify();
									}
								});
								synchronized(wait)
								{
									wait.wait();
								}
							}

							scanner.close();
							print.close();
						}
						catch(IOException | InterruptedException e)
						{
							e.printStackTrace();
						}
						done.set(true);
					}
				});
				t.start();

				while(!done.get())
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
