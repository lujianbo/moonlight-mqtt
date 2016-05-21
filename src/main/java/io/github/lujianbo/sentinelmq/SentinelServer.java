package io.github.lujianbo.sentinelmq;

import io.github.lujianbo.sentinelmq.netty.NettyServer;
import io.github.lujianbo.sentinelmq.spi.DefaultMQTTProtocolHandler;
import io.github.lujianbo.sentinelmq.common.handler.MQTTProtocolHandler;

/**
 * 网关服务启动器
 */
public class SentinelServer {

    private MQTTProtocolHandler mqttProtocolHandler;

    private NettyServer nettyServer;

    public SentinelServer() {
        mqttProtocolHandler=new DefaultMQTTProtocolHandler();
        nettyServer=new NettyServer();
    }

    public void start(){
        nettyServer.start(mqttProtocolHandler);
    }

    public void stop(){
        nettyServer.stop();
    }

    private void registerHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    public static void main(String[] args) {
        SentinelServer sentinelServer=new SentinelServer();
        sentinelServer.start();
    }

}
