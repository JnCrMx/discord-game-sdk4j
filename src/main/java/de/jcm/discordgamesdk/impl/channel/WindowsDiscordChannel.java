package de.jcm.discordgamesdk.impl.channel;

import java.io.RandomAccessFile;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class WindowsDiscordChannel implements DiscordChannel {
    private final FileChannel channel;
    private boolean blocking = true;

    public WindowsDiscordChannel() throws IOException {
        RandomAccessFile raf = new RandomAccessFile("\\\\?\\pipe\\discord-ipc-0", "rw");
        channel = raf.getChannel();
    }

    public void close() throws IOException {
        channel.close();
    }

    public void configureBlocking(boolean block) throws IOException {
        blocking = block;
    }

    public int read(ByteBuffer dst) throws IOException {
        if (!blocking && (channel.size() - channel.position()) < dst.remaining())
        {
            return 0;
        }
        return channel.read(dst);
    }

    public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
        long remaining = 0;
        if (!blocking)
        {
            for (int i = offset; i < offset+length; i++)
            {
                remaining += dsts[i].remaining();
            }
            if ((channel.size() - channel.position()) < remaining)
            {
                return 0;
            }
        }
        return channel.read(dsts, offset, length);
    }

    public int write(ByteBuffer src) throws IOException {
        return channel.write(src);
    }
}
