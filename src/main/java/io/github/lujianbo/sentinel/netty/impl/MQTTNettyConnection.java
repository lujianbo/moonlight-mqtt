package io.github.lujianbo.sentinel.netty.impl;


import io.github.lujianbo.sentinel.protocol.*;
import io.github.lujianbo.sentinel.handler.MQTTConnection;
import io.netty.channel.Channel;

/**
 * Netty 对于 MQTT 部分的实现
 */
public class MQTTNettyConnection implements MQTTConnection {

    private Channel channel;

    public MQTTNettyConnection(Channel channel) {
        this.channel = channel;
    }


    @Override
    public void onWrite(ConnectProtocol message) {
        channel.write(message);
    }

    @Override
    public void onWrite(ConnackProtocol message) {
        channel.write(message);
    }

    @Override
    public void onWrite(DisconnectProtocol message) {
        channel.write(message);
    }

    @Override
    public void onWrite(PingreqProtocol message) {
        channel.write(message);
    }

    @Override
    public void onWrite(PingrespProtocol message) {
        channel.write(message);
    }

    @Override
    public void onWrite(PubcompProtocol message) {
        channel.write(message);
    }

    @Override
    public void onWrite(PublishProtocol message) {
        channel.write(message);
    }

    @Override
    public void onWrite(PubackProtocol message) {
        channel.write(message);
    }

    @Override
    public void onWrite(PubrelProtocol message) {
        channel.write(message);
    }

    @Override
    public void onWrite(PubrecProtocol message) {
        channel.write(message);
    }

    @Override
    public void onWrite(SubackProtocol message) {
        channel.write(message);
    }

    @Override
    public void onWrite(SubscribeProtocol message) {
        channel.write(message);
    }

    @Override
    public void onWrite(UnsubscribeProtocol message) {
        channel.write(message);
    }

    @Override
    public void onWrite(UnsubackProtocol message) {
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
