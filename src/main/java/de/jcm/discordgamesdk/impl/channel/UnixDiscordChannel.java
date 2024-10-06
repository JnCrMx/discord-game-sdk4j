package de.jcm.discordgamesdk.impl.channel;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.net.UnixDomainSocketAddress;
import java.util.*;
import java.util.stream.IntStream;

public class UnixDiscordChannel implements DiscordChannel {
	private final SocketChannel channel;
	private String path;

	private static String[] getSockets()
	{
		List<String> candidates = new ArrayList<>();
		candidates.add(System.getenv("XDG_RUNTIME_DIR"));
		candidates.add(System.getenv("TMPDIR"));
		candidates.add(System.getenv("TMP"));
		candidates.add(System.getenv("TEMP"));

		candidates.removeIf(Objects::isNull);
		candidates.removeIf(p->!new File(p).exists());

		List<String> flatpakCandidates = candidates.stream().map(p->p+"/app/com.discordapp.Discord").toList();
		List<String> snapCandidates = candidates.stream().map(p->p+"/snap.discord").toList();
		List<String> additionalSnapCandidates = candidates.stream()
				.flatMap(p->Arrays.stream(
						Objects.requireNonNull(new File(p).listFiles((file, name) -> name.startsWith("snap.discord_")))
				).map(File::getAbsolutePath)).toList();

		candidates.addAll(flatpakCandidates);
		candidates.addAll(snapCandidates);
		candidates.addAll(additionalSnapCandidates);

		candidates.removeIf(Objects::isNull);
		candidates.removeIf(p->!new File(p).exists());

		return candidates.stream()
				.flatMap(p->
						IntStream.iterate(0, x->x+1)
								.mapToObj(i->p+"/discord-ipc-"+i)
								.takeWhile(pp->new File(pp).exists())
				).toArray(String[]::new);
	}

	public UnixDiscordChannel() throws IOException {
		path = System.getenv("DISCORD_IPC_PATH");
		if(path == null) {
			String instance = System.getenv("DISCORD_INSTANCE_ID");
			int i = 0;
			if (instance != null) {
				i = Integer.parseInt(instance);
			}

			String[] sockets = getSockets();
			if (i < 0 || i >= sockets.length) {
				throw new NoSuchInstanceException(i);
			}
			path = sockets[i];
		}

		channel = SocketChannel.open(UnixDomainSocketAddress.of(path));
	}

	public void close() throws IOException {
		channel.close();
	}

	public void configureBlocking(boolean block) throws IOException {
		channel.configureBlocking(block);
	}

	public boolean isAvailable() {
		return new File(path).exists() && channel.isConnected();
	}

	public int read(ByteBuffer dst) throws IOException {
		return channel.read(dst);
	}

	public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
		return channel.read(dsts, offset, length);
	}

	public int write(ByteBuffer src) throws IOException {
		return channel.write(src);
	}
}
