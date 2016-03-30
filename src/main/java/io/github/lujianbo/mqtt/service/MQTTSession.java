package io.github.lujianbo.mqtt.service;


import io.github.lujianbo.mqtt.domain.*;

public interface MQTTSession {

    public void onConnectMessage(ConnectMessage message);

    public void onConnackMessage(ConnackMessage message);

    public void onDisconnectMessage(DisconnectMessage message);

    public void onPingreqMessage(PingreqMessage message);

    public void onPingrespMessage(PingrespMessage message);

    public void onSubscribeMessage(SubscribeMessage message);

    public void onSubackMessage(SubackMessage message);

    public void onUnsubscribeMessage(UnsubscribeMessage message);

    public void onUnsubackMessage(UnsubackMessage message);

    public void onPublishMessage(PublishMessage message);

    public void onPubackMessage(PubackMessage message);

    public void onPubrecMessage(PubrecMessage message);

    public void onPubrelMessage(PubrelMessage message);

    public void onPubcompMessage(PubcompMessage message);

    public void onClose();

    public void onException();
}
