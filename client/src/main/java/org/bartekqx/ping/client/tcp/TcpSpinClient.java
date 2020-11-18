package org.bartekqx.ping.client.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;

public class TcpSpinClient {
    private final ByteBuffer bb = ByteBuffer.allocateDirect(4096).order(ByteOrder.nativeOrder());

    private final int msgSize;
    private final String host;
    private final int port;

    private SocketChannel socketChannel;

    public TcpSpinClient(int msgSize, String host, int port) {
        this.msgSize = msgSize;
        this.host = host;
        this.port = port;
    }

    public void start() throws IOException {
        socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
        socketChannel.configureBlocking(false);
        socketChannel.socket().setTcpNoDelay(true);
    }

    public void ping() throws IOException{
        bb.clear();
        bb.limit(msgSize);
        socketChannel.write(bb);
        bb.clear();
        int read = 0;
        do {
            read += socketChannel.read(bb);
        } while (read < msgSize);
    }

    public void close() throws IOException {
        socketChannel.close();
    }
}
