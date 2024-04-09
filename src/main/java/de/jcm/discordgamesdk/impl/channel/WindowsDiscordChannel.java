package de.jcm.discordgamesdk.impl.channel;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class WindowsDiscordChannel implements DiscordChannel {
	private final FileChannel channel;
	private boolean blocking = true;
	private String path;

	public WindowsDiscordChannel() throws IOException {
		path = System.getenv("DISCORD_IPC_PATH");
		if(path == null) {
			String instance = System.getenv("DISCORD_INSTANCE_ID");
			int i = 0;
			if (instance != null) {
				i = Integer.parseInt(instance);
			}
			path = "\\\\?\\pipe\\discord-ipc-"+i;
		}
		RandomAccessFile raf = new RandomAccessFile(path, "rw");
		channel = raf.getChannel();
	}

	public void close() throws IOException {
		channel.close();
	}

	public void configureBlocking(boolean block) throws IOException {
		blocking = block;
	}

	public boolean isAvailable() {
		return new File(path).exists() && channel.isOpen();
	}

	public int read(ByteBuffer dst) throws IOException {
		int res = 0;
		if (blocking || (channel.size() - channel.position()) >= dst.remaining())
		{
			res = channel.read(dst);
		}
		return res;
	}

	public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
		long res = 0;
		long remaining = 0;
		for (int i = offset; !blocking && i < offset+length; i++)
		{
			remaining += dsts[i].remaining();
		}
		if (blocking || (channel.size() - channel.position()) >= remaining)
		{
			res = channel.read(dsts, offset, length);
		}
		return res;
	}

	public int write(ByteBuffer src) throws IOException {
		int res = channel.write(src);
		channel.force(false); // ensure that data is actually written to file
		return res;
	}
}
