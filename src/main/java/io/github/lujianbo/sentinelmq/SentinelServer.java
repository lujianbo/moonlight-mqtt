package io.github.lujianbo.sentinelmq;

import io.github.lujianbo.sentinelmq.netty.NettyServer;
import io.github.lujianbo.sentinelmq.netty.handler.HandlerContext;
import io.github.lujianbo.sentinelmq.spi.DefaultMQTTProtocolHandler;
import io.github.lujianbo.sentinelmq.spi.DefaultMQTTTopicManager;

/**
 * 网关服务启动器
 */
public class SentinelServer {

    private NettyServer nettyServer = new NettyServer();

    private HandlerContext context;

    public SentinelServer() {

        DefaultMQTTTopicManager topicManager = new DefaultMQTTTopicManager("");
        topicManager.createTopic("topic/test/mytopic");

        context = new HandlerContext();
        context.setHost("localhost");
        context.setPort(8080);
        //配置handler
        context.setHandler(new DefaultMQTTProtocolHandler(topicManager));
    }

    public void start() {
        nettyServer.start(context);
    }

    public void stop() {
        nettyServer.stop();
    }

    private void registerHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    public static void main(String[] args) {
        SentinelServer sentinelServer = new SentinelServer();
        sentinelServer.start();
    }

}
