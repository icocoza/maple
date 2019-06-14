package com.ccz.modules.server;

import com.ccz.modules.server.handler.IServiceActionHandler;
import com.ccz.modules.server.initializer.ServiceServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.FutureListener;

import java.net.InetSocketAddress;

public class ServiceServer {

    private ServerBootstrap bootstrap;
    private ChannelFuture channelFuture;

    private int port;
    private String websocketPath;
    private IServiceActionHandler serviceActionHandler;

    public ServiceServer(int port, String websocketPath, IServiceActionHandler serviceActionHandler) {
        this.port = port;
        this.websocketPath = websocketPath;
        this.serviceActionHandler = serviceActionHandler;
    }

    public void start() throws InterruptedException {

        bootstrap = new ServerBootstrap();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.localAddress(new InetSocketAddress(port));

        bootstrap.childHandler(new ServiceServerInitializer(websocketPath, serviceActionHandler));

        channelFuture = bootstrap.bind().addListener((FutureListener<Void>) future -> {
            if (future.isSuccess()) {
                System.out.printf("Websocket & Http Listening ... : %d\n", port);
            } else {
                System.out.printf("Server Listening failed at port: %d!\n", port);
            }
        }).sync();

//        List<String> nodeList = Arrays.asList(serverConfig.getSentinelNodes().split(",", -1));

//        RedisCommandManager.getInst().initRedisSentinel(serverConfig.getSentinelMasterName(), nodeList, serverConfig.getSentinelPassword());
//        RedisCommander redisCommander = new RedisCommander(RedisCommandManager.getInst().getRedisConnection());
//        redisCommander.set("ccz", "test");

    }

    public void closeSync() throws InterruptedException {
        channelFuture.channel().closeFuture().sync();
    }

    public void stop() {
        if (channelFuture == null)
            return;
        Channel ch = channelFuture.channel();
        if (ch != null)
            ch.close();
        channelFuture = null;
    }

}
