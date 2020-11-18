package org.bartekxq.ping.server;

public abstract class AbstractServer {

    protected final String host;
    protected final int port;

    public AbstractServer(String localhost, int port) {
        this.port = port;
        this.host = localhost;
    }
}
