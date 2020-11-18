package org.bartekxq.ping.server.tcp;

import org.bartekxq.ping.server.AbstractServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class TcpBlockingPingServer extends AbstractServer {

    private static final int PAGE_SIZE = 4096;

    public TcpBlockingPingServer(String localhost, int port) {
        super(localhost, port);
    }

    public void start() throws IOException {
        final ByteBuffer buff = ByteBuffer.allocateDirect(PAGE_SIZE).order(ByteOrder.nativeOrder());

        final ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(host, port));

        try (SocketChannel sc = serverSocket.accept()) {
            configureSocket(sc);

            while (!Thread.interrupted()) {
                if (pong(buff, sc))
                    return;
            }
        } finally {
            serverSocket.close();
        }
    }

    private void configureSocket(SocketChannel sc) throws IOException {
        sc.socket().setTcpNoDelay(true);
        sc.configureBlocking(true);
    }

    private static boolean pong(ByteBuffer buff, SocketChannel sc) throws IOException {
        int read;
        buff.clear();
        read = sc.read(buff);
        if (read == -1) {
            return true;
        }
        buff.flip();
        sc.write(buff);
        return false;
    }
}
