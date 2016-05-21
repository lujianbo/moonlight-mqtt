package io.github.lujianbo.sentinelmq.netty;

import io.github.lujianbo.sentinelmq.common.handler.MQTTProtocolHandler;
import io.github.lujianbo.sentinelmq.spi.engine.core.MQTTEngine;
import io.github.lujianbo.sentinelmq.netty.handler.HandlerContext;
import io.github.lujianbo.sentinelmq.netty.handler.MowServerInitializer;
import io.github.lujianbo.sentinelmq.SentinelServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;


/**
 * netty实现网络部分,支持webocket
 * */
public class NettyServer {

    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private EventLoopGroup workerGroup = new NioEventLoopGroup();
    /**
     * SSL支持
     */
    public static final boolean SSL = System.getProperty("ssl") != null;

    static final int PORT = Integer.parseInt(System.getProperty("port", SSL ? "8443" : "8080"));

    public void start(MQTTProtocolHandler handler) {
        try {

            HandlerContext context = new HandlerContext();

            /**
             * 配置主要的处理器
             * */
            context.setHandler(handler);

            MowServerInitializer initializer = new MowServerInitializer(context);

            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(initializer);
            b.bind(PORT).sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

}
