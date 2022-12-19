package de.jcm.discordgamesdk.impl.channel;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.LogLevel;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.net.UnixDomainSocketAddress;

public class UnixDiscordChannel implements DiscordChannel {
    private final Core.CorePrivate core;
    private final SocketChannel channel;

    public UnixDiscordChannel(Core.CorePrivate core) throws IOException {
        this.core = core;

        String path = System.getenv("XDG_RUNTIME_DIR");
        if (path == null)
        {
            path = System.getenv("TMPDIR");
            if (path == null)
            {
                path = System.getenv("TMP");
                if (path == null)
                {
                    path = System.getenv("TEMP");
                    if (path == null)
                    {
                        path = "/tmp";
                    }
                }
            }
        }
        if (new File(path + "/app/com.discordapp.Discord").exists())
        {
            path = path + "/app/com.discordapp.Discord/discord-ipc-0";
        }
        else
        {
            path = path + "/discord-ipc-0";
        }
        channel = SocketChannel.open(UnixDomainSocketAddress.of(path));
    }

    public void close() throws IOException {
        channel.close();
    }

    public void configureBlocking(boolean block) throws IOException {
        channel.configureBlocking(block);
    }

    public int read(ByteBuffer dst) throws IOException {
        long start = System.currentTimeMillis();
        int res = channel.read(dst);
        core.log(LogLevel.VERBOSE, "read(ByteBuffer) returned " + res + " (" + (System.currentTimeMillis() - start)  + "ms)");
        return res;
    }

    public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
        long start = System.currentTimeMillis();
        long res = channel.read(dsts, offset, length);
        core.log(LogLevel.VERBOSE, "read(ByteBuffer[], offset, length) returned " + res + " (" + (System.currentTimeMillis() - start)  + "ms)");
        return res;
    }

    public int write(ByteBuffer src) throws IOException {
        long start = System.currentTimeMillis();
        int res= channel.write(src);
        core.log(LogLevel.VERBOSE, "write(ByteBuffer) returned " + res + " (" + (System.currentTimeMillis() - start)  + "ms)");
        return res;
    }
}
