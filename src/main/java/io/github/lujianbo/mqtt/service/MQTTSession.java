package io.github.lujianbo.mqtt.service;


import io.github.lujianbo.mqtt.domain.*;

public abstract class MQTTSession {

    abstract public void onConnectMessage(ConnectMessage message);

    abstract public void onConnackMessage(ConnackMessage message);

    abstract public void onDisconnectMessage(DisconnectMessage message);

    abstract public void onPingreqMessage(PingreqMessage message);

    abstract public void onPingrespMessage(PingrespMessage message);

    abstract public void onSubscribeMessage(SubscribeMessage message);

    abstract public void onSubackMessage(SubackMessage message);

    abstract public void onUnsubscribeMessage(UnsubscribeMessage message);

    abstract public void onUnsubackMessage(UnsubackMessage message);

    abstract public void onPublishMessage(PublishMessage message);

    abstract public void onPubackMessage(PubackMessage message);

    abstract public void onPubrecMessage(PubrecMessage message);

    abstract public void onPubrelMessage(PubrelMessage message);

    abstract public void onPubcompMessage(PubcompMessage message);

    abstract public void onClose();

    abstract public void onException();
}
