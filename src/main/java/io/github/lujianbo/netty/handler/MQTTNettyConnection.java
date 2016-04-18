package io.github.lujianbo.netty.handler;


import io.github.lujianbo.mqtt.protocol.MQTTProtocol;
import io.github.lujianbo.mqtt.service.MQTTConnection;
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
