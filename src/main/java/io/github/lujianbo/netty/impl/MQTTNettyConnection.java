package io.github.lujianbo.netty.impl;


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
    public void write(ConnectProtocol message) {
        channel.writeAndFlush(message);
    }

    @Override
    public void write(ConnackProtocol message) {
        channel.writeAndFlush(message);
    }

    @Override
    public void write(DisconnectProtocol message) {
        channel.writeAndFlush(message);
    }

    @Override
    public void write(PingreqProtocol message) {
        channel.writeAndFlush(message);
    }

    @Override
    public void write(PingrespProtocol message) {
        channel.writeAndFlush(message);
    }

    @Override
    public void write(PubcompProtocol message) {
        channel.writeAndFlush(message);
    }

    @Override
    public void write(PublishProtocol message) {
        channel.writeAndFlush(message);
    }

    @Override
    public void write(PubackProtocol message) {
        channel.writeAndFlush(message);
    }

    @Override
    public void write(PubrelProtocol message) {
        channel.writeAndFlush(message);
    }

    @Override
    public void write(PubrecProtocol message) {
        channel.writeAndFlush(message);
    }

    @Override
    public void write(SubackProtocol message) {
        channel.writeAndFlush(message);
    }

    @Override
    public void write(SubscribeProtocol message) {
        channel.writeAndFlush(message);
    }

    @Override
    public void write(UnsubscribeProtocol message) {
        channel.writeAndFlush(message);
    }

    @Override
    public void write(UnsubackProtocol message) {
        channel.writeAndFlush(message);
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
