package io.github.lujianbo.sentinel.netty.handler;


import io.github.lujianbo.sentinel.protocol.MQTTProtocol;
import io.github.lujianbo.sentinel.proxy.MQTTConnection;
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
