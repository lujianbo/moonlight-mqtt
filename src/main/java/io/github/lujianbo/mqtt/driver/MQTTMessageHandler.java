package io.github.lujianbo.mqtt.driver;

import io.github.lujianbo.mqtt.common.protocol.*;

/**
 * Handler处理器接口
 */
public interface MQTTMessageHandler {

    public void onRead(ConnectProtocol message);

    public void onRead(ConnackProtocol message);

    public void onRead(DisconnectProtocol message);

    public void onRead(PingreqProtocol message);

    public void onRead(PingrespProtocol message);

    public void onRead(PubcompProtocol message);

    public void onRead(PublishProtocol message);

    public void onRead(PubackProtocol message);

    public void onRead(PubrelProtocol message);

    public void onRead(PubrecProtocol message);

    public void onRead(SubackProtocol message);

    public void onRead(SubscribeProtocol message);

    public void onRead(UnsubscribeProtocol message);

    public void onRead(UnsubackProtocol message);

    public void onClose();

    public void onException();

}
