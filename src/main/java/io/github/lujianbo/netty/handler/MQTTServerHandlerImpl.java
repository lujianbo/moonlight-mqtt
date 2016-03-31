package io.github.lujianbo.netty.handler;

import io.github.lujianbo.mqtt.domain.*;
import io.github.lujianbo.mqtt.service.MQTTWriter;
import io.netty.channel.Channel;


/**
 * 实现了MQTT逻辑部分与netty框架之间的桥梁
 */
public class MQTTServerHandlerImpl implements MQTTWriter {

    public Channel channel;

    public MQTTServerHandlerImpl(Channel channel){
        this.channel=channel;
    }

    @Override
    public void writeConnectMessage(ConnectMessage message) {
        channel.write(message);
    }

    @Override
    public void writeConnackMessage(ConnackMessage message) {
        channel.write(message);
    }

    @Override
    public void writeDisconnectMessage(DisconnectMessage message) {
        channel.write(message);
    }

    @Override
    public void writePingreqMessage(PingreqMessage message) {
        channel.write(message);
    }

    @Override
    public void writePingrespMessage(PingrespMessage message) {
        channel.write(message);
    }

    @Override
    public void writeSubscribeMessage(SubscribeMessage message) {
        channel.write(message);
    }

    @Override
    public void writeSubackMessage(SubackMessage message) {
        channel.write(message);
    }

    @Override
    public void writeUnsubscribeMessage(UnsubscribeMessage message) {
        channel.write(message);
    }

    @Override
    public void writeUnsubackMessage(UnsubackMessage message) {
        channel.write(message);
    }

    @Override
    public void writePublishMessage(PublishMessage message) {
        channel.write(message);
    }

    @Override
    public void writePubackMessage(PubackMessage message) {
        channel.write(message);
    }

    @Override
    public void writePubrecMessage(PubrecMessage message) {
        channel.write(message);
    }

    @Override
    public void writePubrelMessage(PubrelMessage message) {
        channel.write(message);
    }

    @Override
    public void writePubcompMessage(PubcompMessage message) {
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
