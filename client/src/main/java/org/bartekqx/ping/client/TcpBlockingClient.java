package org.bartekqx.ping.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;

public class TcpBlockingClient {

    private final ByteBuffer bb = ByteBuffer.allocateDirect(4096).order(ByteOrder.nativeOrder());

    private static final byte[] msg = "123123".getBytes();

    private final String host;
    private final int port;

    private SocketChannel socketChannel;

    public TcpBlockingClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws IOException {
        socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
        socketChannel.configureBlocking(true);
        socketChannel.socket().setTcpNoDelay(true);
    }

    public void ping() throws IOException{
        bb.clear();
        socketChannel.write(bb);
        bb.flip();
        socketChannel.read(bb);
    }
}
