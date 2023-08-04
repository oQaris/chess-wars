package io.deeplay.server;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public class Server {
    private int port;

    public Server(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        int port = 8888;
        Server server = new Server(port);
        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
    }
}