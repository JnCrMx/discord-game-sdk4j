package de.jcm.discordgamesdk;

import java.io.RandomAccessFile;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class WindowsDiscordChannel implements DiscordChannel {
    private final FileChannel channel;

    public WindowsDiscordChannel() throws IOException {
        RandomAccessFile raf = new RandomAccessFile("\\\\?\\pipe\\discord-ipc-0", "rw");
        channel = raf.getChannel();
    }

    public void close() throws IOException {
        channel.close();
    }

    public void configureBlocking(boolean block) throws IOException {
        // nothing to do here
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
