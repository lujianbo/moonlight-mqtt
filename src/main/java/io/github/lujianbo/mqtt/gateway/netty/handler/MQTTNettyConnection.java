package io.github.lujianbo.mqtt.gateway.netty.handler;


import io.github.lujianbo.mqtt.gateway.common.protocol.MQTTProtocol;
import io.github.lujianbo.mqtt.gateway.common.MQTTConnection;
import io.netty.channel.Channel;

/**
 *
 */
public class MQTTNettyConnection implements MQTTConnection {

    private Channel channel;

    public MQTTNettyConnection(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void write(MQTTProtocol message) {
        channel.write(message);
    }

    @Override
    public void close() {
        channel.close();
    }

    @Override
    public void onException() {
        channel.close();
    }
}
