package de.jcm.discordgamesdk.impl.channel;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface DiscordChannel {
	public void close() throws IOException;
	public void configureBlocking(boolean block) throws IOException;
	public int read(ByteBuffer dst) throws IOException;
	public long read(ByteBuffer[] dsts, int offset, int length) throws IOException;
	public int write(ByteBuffer src) throws IOException;
}
