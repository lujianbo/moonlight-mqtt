package io.github.lujianbo.sentinelmq;

import io.github.lujianbo.sentinelmq.common.handler.MQTTProtocolHandler;
import io.github.lujianbo.sentinelmq.netty.MQTTServer;
import io.github.lujianbo.sentinelmq.spi.DefaultMQTTProtocolHandler;
import io.github.lujianbo.sentinelmq.spi.DefaultMQTTTopicManager;

/**
 * 网关服务启动器
 */
public class SentinelServer {

    private MQTTServer nettyServer = new MQTTServer();

    private MQTTProtocolHandler handler;

    /**
     * webSocket的支持
     * */
    private String path="/mqtt";

    public SentinelServer() {
        DefaultMQTTTopicManager topicManager = new DefaultMQTTTopicManager("");
        topicManager.createTopic("topic/test/mytopic");
        handler=new DefaultMQTTProtocolHandler(topicManager);
    }

    public void start() {
        nettyServer.start(8080,path,handler);
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
