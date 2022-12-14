import de.jcm.discordgamesdk.*;
import de.jcm.discordgamesdk.activity.Activity;
import de.jcm.discordgamesdk.lobby.Lobby;
import de.jcm.discordgamesdk.lobby.LobbySearchQuery;
import de.jcm.discordgamesdk.lobby.LobbyTransaction;
import de.jcm.discordgamesdk.lobby.LobbyType;
import de.jcm.discordgamesdk.user.DiscordUser;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LobbyExample extends JFrame
{
	private static CreateParams createParams;
	private static Core core;

	private static ScheduledExecutorService executor;
	private static ScheduledFuture<?> callbackFuture;
	private static ScheduledFuture<?> updateFuture;

	private DiscordUser selfUser;
	private final List<Lobby> myLobbies = new ArrayList<>();
	private final List<Lobby> joinedLobbies = new ArrayList<>();
	private final Activity activity = new Activity();

	private final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Set<Object> seen = ConcurrentHashMap.newKeySet();
		return t -> seen.add(keyExtractor.apply(t));
	}

	public final DiscordEventAdapter eventAdapter = new DiscordEventAdapter()
	{
		@Override
		public void onLobbyUpdate(long lobbyId)
		{
			if(myLobbies.removeIf(l->l.getId() == lobbyId))
			{
				myLobbies.add(core.lobbyManager().getLobby(lobbyId));
			}
			if(joinedLobbies.removeIf(l->l.getId() == lobbyId))
			{
				joinedLobbies.add(core.lobbyManager().getLobby(lobbyId));
			}
		}

		@Override
		public void onLobbyDelete(long lobbyId, int reason)
		{
			myLobbies.removeIf(l->l.getId() == lobbyId);
			joinedLobbies.removeIf(l->l.getId() == lobbyId);
		}

		@Override
		public void onMemberConnect(long lobbyId, long userId)
		{
			// Add some code here
		}

		@Override
		public void onMemberUpdate(long lobbyId, long userId)
		{
			// Add some code here
		}

		@Override
		public void onMemberDisconnect(long lobbyId, long userId)
		{
			// Add some code here
		}

		@Override
		public void onCurrentUserUpdate()
		{
			selfUser = core.userManager().getCurrentUser();
			setTitle("Lobby Manager: "+
					         selfUser.getUsername()+"#"+selfUser.getDiscriminator()+" "+
					         selfUser.getUserId());
		}

		@Override
		public void onActivityJoin(String secret)
		{
			core.lobbyManager().connectLobbyWithActivitySecret(secret, (result, lobby) -> {
				JOptionPane.showMessageDialog(null, result.name(), "Result",
				                              result == Result.OK ?
						                              JOptionPane.INFORMATION_MESSAGE :
						                              JOptionPane.ERROR_MESSAGE);
				if(result == Result.OK)
				{
					joinedLobbies.add(lobby);
				}
			});
		}
	};

	public LobbyExample()
	{
		JPanel contentPane = new JPanel(new BorderLayout());

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				ArrayList<CompletableFuture<Void>> futures = new ArrayList<>();
				for(Lobby lobby : myLobbies)
				{
					CompletableFuture<Void> f = new CompletableFuture<>();
					core.lobbyManager().deleteLobby(lobby, result -> {
						if(result == Result.OK)
							f.complete(null);
						else
							f.completeExceptionally(new GameSDKException(result));
					});
					futures.add(f);
				}
				try
				{
					CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();
				}
				catch(InterruptedException | ExecutionException interruptedException)
				{
					interruptedException.printStackTrace();
				}

				callbackFuture.cancel(true);
				updateFuture.cancel(true);

				executor.shutdown();
				core.close();
				createParams.close();
			}
		});

		JPanel createLobby = new JPanel(new FlowLayout(FlowLayout.LEADING));

		createLobby.add(new JLabel("Type: "));
		JComboBox<LobbyType> type = new JComboBox<>(LobbyType.values());
		createLobby.add(type);

		createLobby.add(new JLabel("Capacity: "));
		JSpinner capacity = new JSpinner(new SpinnerNumberModel(10, 1, 1000, 1));
		createLobby.add(capacity);

		JCheckBox locked = new JCheckBox("Locked");
		createLobby.add(locked);

		JButton createButton = new JButton("Create Lobby");
		createButton.addActionListener(e->{
			LobbyTransaction txn = core.lobbyManager().getLobbyCreateTransaction();
			txn.setType((LobbyType) type.getSelectedItem());
			txn.setCapacity((Integer) capacity.getValue());
			txn.setLocked(locked.isSelected());
			core.lobbyManager().createLobby(txn, (result, lobby) -> {
				JOptionPane.showMessageDialog(null, result.name(), "Result",
				                              result == Result.OK ?
						                              JOptionPane.INFORMATION_MESSAGE :
						                              JOptionPane.ERROR_MESSAGE);
				if(result == Result.OK)
				{
					myLobbies.add(lobby);
				}
			});
		});
		createLobby.add(createButton);

		JPanel joinPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));

		joinPanel.add(new JLabel("ID: "));
		JTextField connectLobbyId = new JTextField(16);
		joinPanel.add(connectLobbyId);

		joinPanel.add(new JLabel("Secret: "));
		JTextField connectSecret = new JTextField(16);
		joinPanel.add(connectSecret);

		JButton connectByIdButton = new JButton("Connect");
		connectByIdButton.addActionListener(e->{
			long lobbyId = Long.parseLong(connectLobbyId.getText());
			String secret = connectSecret.getText();
			core.lobbyManager().connectLobby(lobbyId, secret, (result, lobby) -> {
				JOptionPane.showMessageDialog(null, result.name(), "Result",
				                              result == Result.OK ?
						                              JOptionPane.INFORMATION_MESSAGE :
						                              JOptionPane.ERROR_MESSAGE);
				if(result == Result.OK)
				{
					joinedLobbies.add(lobby);
				}
			});
		});
		joinPanel.add(connectByIdButton);

		JPanel northPanel = new JPanel();
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.PAGE_AXIS));
		northPanel.add(createLobby);
		northPanel.add(joinPanel);
		contentPane.add(northPanel, BorderLayout.NORTH);

		JPanel lobbyPanel = new JPanel();
		lobbyPanel.setLayout(new BoxLayout(lobbyPanel, BoxLayout.PAGE_AXIS));
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(lobbyPanel);
		contentPane.add(scrollPane, BorderLayout.CENTER);

		setContentPane(contentPane);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Lobby Manager");
		setBounds(1920/2-1200/2, 1080/2-600/2, 1200, 600);

		updateFuture = executor.scheduleAtFixedRate(()->{
			LobbySearchQuery searchQuery = core.lobbyManager().getSearchQuery();
			core.lobbyManager().search(searchQuery);

			List<Lobby> publicLobbies = core.lobbyManager().getLobbies();
			ArrayList<Lobby> lobbies = Stream.of(publicLobbies, myLobbies, joinedLobbies)
			                                 .flatMap(List::stream)
			                                 .filter(distinctByKey(Lobby::getId))
			                                 .collect(Collectors.toCollection(ArrayList::new));

			lobbyPanel.removeAll();
			for(Lobby lobby : lobbies)
			{
				boolean own = selfUser != null && selfUser.getUserId() == lobby.getOwnerId();

				JPanel panel = new JPanel();
				panel.setLayout(new BorderLayout());

				JPanel north = new JPanel(new FlowLayout(FlowLayout.LEADING));
				{
					JRadioButton pub = new JRadioButton(lobby.getType().name(), lobby.getType() == LobbyType.PUBLIC);
					pub.setEnabled(false);
					north.add(pub);

					JLabel id = new JLabel(String.valueOf(lobby.getId()));
					id.setFont(new Font("monospace", Font.PLAIN, 16));
					id.setToolTipText("Click to copy");
					id.addMouseListener(new MouseAdapter()
					{
						@Override
						public void mouseClicked(MouseEvent e)
						{
							StringSelection selection = new StringSelection(String.valueOf(lobby.getId()));
							clipboard.setContents(selection, selection);
						}
					});
					north.add(id);
				}
				panel.add(north, BorderLayout.NORTH);

				JPanel center = new JPanel(new FlowLayout(FlowLayout.LEADING));
				{
					center.add(new JLabel("Secret: "));
					JLabel secret = new JLabel(lobby.getSecret());
					secret.setFont(new Font("monospace", Font.PLAIN, 16));
					secret.setToolTipText("Click to copy");
					secret.addMouseListener(new MouseAdapter()
					{
						@Override
						public void mouseClicked(MouseEvent e)
						{
							StringSelection selection = new StringSelection(lobby.getSecret());
							clipboard.setContents(selection, selection);
						}
					});
					center.add(secret);

					center.add(new JSeparator());

					int memberCount = core.lobbyManager().memberCount(lobby);
					JLabel capacityL = new JLabel(memberCount+"/"+lobby.getCapacity());
					center.add(capacityL);

					center.add(new JSeparator());

					JLabel lockedL = new JLabel(lobby.isLocked() ? "Locked" : "Unlocked");
					center.add(lockedL);
				}
				panel.add(center, BorderLayout.CENTER);

				JPanel east = new JPanel();
				{
					JButton metadataButton = new JButton("Get metadata");
					metadataButton.addActionListener(e->{
						String data = core.lobbyManager().getLobbyMetadata(lobby).entrySet().stream()
						                  .map(entry->entry.getKey()+" = "+entry.getValue())
						                  .collect(Collectors.joining("\n"));
						JOptionPane.showMessageDialog(null, data, "Metadata", JOptionPane.PLAIN_MESSAGE);
					});
					east.add(metadataButton);

					JButton connectButton = new JButton("Connect");
					connectButton.addActionListener(e->{
						core.lobbyManager().connectLobby(lobby.getId(), lobby.getSecret(), (result, lobby1) -> {
							JOptionPane.showMessageDialog(null, result.name(), "Result",
							                              result == Result.OK ?
									                              JOptionPane.INFORMATION_MESSAGE :
									                              JOptionPane.ERROR_MESSAGE);
							if(result == Result.OK)
							{
								if(myLobbies.removeIf(l -> l.getId() == lobby.getId()))
								{
									myLobbies.add(lobby1);
								}
							}
						});
					});
					east.add(connectButton);

					JButton disconnectButton = new JButton("Disconnect");
					disconnectButton.addActionListener(e->{
						core.lobbyManager().disconnectLobby(lobby.getId(), result -> {
							JOptionPane.showMessageDialog(null, result.name(), "Result",
							                              result == Result.OK ?
									                              JOptionPane.INFORMATION_MESSAGE :
									                              JOptionPane.ERROR_MESSAGE);
							if(result == Result.OK)
							{
								joinedLobbies.removeIf(l -> l.getId() == lobby.getId());
							}
						});
					});
					east.add(disconnectButton);

					JButton update = new JButton("Update");
					update.setEnabled(own);
					update.addActionListener(e->{
						LobbyTransaction txn = core.lobbyManager().getLobbyUpdateTransaction(lobby);
						txn.setType((LobbyType) type.getSelectedItem());
						txn.setCapacity((Integer) capacity.getValue());
						txn.setLocked(locked.isSelected());
						core.lobbyManager().updateLobby(lobby, txn, result -> {
							JOptionPane.showMessageDialog(null, result.name(), "Result",
							                              result == Result.OK ?
									                              JOptionPane.INFORMATION_MESSAGE :
									                              JOptionPane.ERROR_MESSAGE);
							if(result == Result.OK)
							{
								if(myLobbies.removeIf(l -> l.getId() == lobby.getId()))
								{
									myLobbies.add(core.lobbyManager().getLobby(lobby.getId()));
								}
							}
						});
					});
					east.add(update);

					JButton delete = new JButton("Delete");
					delete.setEnabled(own);
					delete.addActionListener(e->{
						core.lobbyManager().deleteLobby(lobby, result -> {
							JOptionPane.showMessageDialog(null, result.name(), "Result",
							                              result == Result.OK ?
									                              JOptionPane.INFORMATION_MESSAGE :
									                              JOptionPane.ERROR_MESSAGE);
							myLobbies.removeIf(l->l.getId() == lobby.getId());
						});
					});
					east.add(delete);

					JButton setAsActivity = new JButton("Set as Activity");
					setAsActivity.addActionListener(e->{
						activity.setDetails("Testing lobbies");
						activity.setState("and having fun!");
						activity.party().setID(String.valueOf(lobby.getId()));
						activity.party().size().setCurrentSize(core.lobbyManager().memberCount(lobby));
						activity.party().size().setMaxSize(lobby.getCapacity());
						activity.secrets().setJoinSecret(core.lobbyManager().getLobbyActivitySecret(lobby));
						core.activityManager().updateActivity(activity, result->{
							JOptionPane.showMessageDialog(null, result.name(), "Result",
							                              result == Result.OK ?
									                              JOptionPane.INFORMATION_MESSAGE :
									                              JOptionPane.ERROR_MESSAGE);
						});
					});
					east.add(setAsActivity);
				}
				panel.add(east, BorderLayout.EAST);
				panel.setBorder(new LineBorder(Color.DARK_GRAY));

				lobbyPanel.add(panel);
				panel.setMaximumSize(new Dimension(panel.getMaximumSize().width, panel.getMinimumSize().height));
			}
			lobbyPanel.add(Box.createGlue());
			lobbyPanel.revalidate();
		}, 1, 1, TimeUnit.SECONDS);
	}

	public static void main(String[] args) throws IOException
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
		}

		executor = Executors.newSingleThreadScheduledExecutor();
		LobbyExample frame = new LobbyExample();

		createParams = new CreateParams();
		createParams.setClientID(792838852557537310L);
		createParams.registerEventHandler(frame.eventAdapter);
		core = new Core(createParams);

		callbackFuture = executor.scheduleAtFixedRate(core::runCallbacks, 0, 16, TimeUnit.MILLISECONDS);

		frame.setVisible(true);
	}
}
