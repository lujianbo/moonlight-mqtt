package io.github.lujianbo.netty;

import io.github.lujianbo.engine.core.MQTTEngine;
import io.github.lujianbo.netty.handler.HandlerContext;
import io.github.lujianbo.netty.handler.MQTTServerInitializer;
import io.github.lujianbo.netty.handler.MowServerInitializer;
import io.github.lujianbo.sentinel.SentinelServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;


public class NettyServer extends SentinelServer {

    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private EventLoopGroup workerGroup = new NioEventLoopGroup();
    /**
     * SSL支持
     */
    public static final boolean SSL = System.getProperty("ssl") != null;

    static final int PORT = Integer.parseInt(System.getProperty("port", SSL ? "8443" : "8080"));

    public NettyServer(MQTTEngine engine) {
        super(engine);
    }

    public void start() {
        try {

            HandlerContext context = new HandlerContext();

            /**
             * 配置主要的处理器
             * */
            context.setHandler(this.mqttProtocolHandler);

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
