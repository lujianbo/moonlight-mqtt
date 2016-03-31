package io.github.lujianbo.mqtt.service;

import io.github.lujianbo.mqtt.domain.*;

/**
 * Created by jianbo on 2016/3/29.
 */
public interface MQTTWriter {

    public void writeConnectMessage(ConnectMessage message);

    public void writeConnackMessage(ConnackMessage message);

    public void writeDisconnectMessage(DisconnectMessage message);

    public void writePingreqMessage(PingreqMessage message);

    public void writePingrespMessage(PingrespMessage message);

    public void writeSubscribeMessage(SubscribeMessage message);

    public void writeSubackMessage(SubackMessage message);

    public void writeUnsubscribeMessage(UnsubscribeMessage message);

    public void writeUnsubackMessage(UnsubackMessage message);

    public void writePublishMessage(PublishMessage message);

    public void writePubackMessage(PubackMessage message);

    public void writePubrecMessage(PubrecMessage message);

    public void writePubrelMessage(PubrelMessage message);

    public void writePubcompMessage(PubcompMessage message);

    public void close();

    public void onException();
}
