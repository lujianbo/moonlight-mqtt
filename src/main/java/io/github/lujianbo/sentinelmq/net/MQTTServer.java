package io.github.lujianbo.sentinelmq.net;


import io.github.lujianbo.sentinelmq.common.handler.MQTTProtocolHandler;
import io.github.lujianbo.sentinelmq.net.handler.MQTTServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;


/**
 * netty实现网络部分,支持webocket
 */
public class MQTTServer {

    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    public void start(int port, String webSocketPath, MQTTProtocolHandler handler) {
        try {
            //设置
            MQTTServerInitializer initializer = new MQTTServerInitializer();
            initializer.setHandler(handler);
            initializer.setPath(webSocketPath);
            //绑定
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(initializer);
            b.bind(port).sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

}
