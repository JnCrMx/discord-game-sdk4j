package de.jcm.discordgamesdk;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.net.UnixDomainSocketAddress;

public class UnixDiscordChannel implements DiscordChannel {
    private final SocketChannel channel;

    public UnixDiscordChannel() throws IOException {
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
        return channel.read(dst);
    }

    public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
        return channel.read(dsts, offset, length);
    }

    public int write(ByteBuffer src) throws IOException {
        return channel.write(src);
    }
}
