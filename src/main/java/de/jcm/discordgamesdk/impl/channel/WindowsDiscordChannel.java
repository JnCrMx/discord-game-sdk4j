package de.jcm.discordgamesdk.impl.channel;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.LogLevel;

import java.io.RandomAccessFile;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class WindowsDiscordChannel implements DiscordChannel {
    private final Core.CorePrivate core;
    private final FileChannel channel;
    private boolean blocking = true;

    public WindowsDiscordChannel(Core.CorePrivate core) throws IOException {
        this.core = core;
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
        long start = System.currentTimeMillis();
        int res = 0;
        if (blocking || (channel.size() - channel.position()) >= dst.remaining())
        {
            res = channel.read(dst);
        }
        log(LogLevel.VERBOSE, "read(ByteBuffer) returned " + res + " (" + (System.currentTimeMillis() - start)  + "ms)");
        return res;
    }

    public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
        long start = System.currentTimeMillis();
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
        log(LogLevel.VERBOSE, "read(ByteBuffer[], offset, length) returned " + res + " (" + (System.currentTimeMillis() - start)  + "ms)");
        return res;
    }

    public int write(ByteBuffer src) throws IOException {
        long start = System.currentTimeMillis();
        int res = channel.write(src);
        channel.force(false); // ensure that data is actually written to file
        log(LogLevel.VERBOSE, "write(ByteBuffer) returned " + res + " (" + (System.currentTimeMillis() - start)  + "ms)");
        return res;
    }

    private void log(LogLevel level, String msg)
    {
        if (core != null)
        {
            core.log(level, msg);
        }
    }
}
