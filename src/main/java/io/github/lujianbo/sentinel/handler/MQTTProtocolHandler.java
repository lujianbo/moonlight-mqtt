package io.github.lujianbo.sentinel.handler;

import io.github.lujianbo.sentinel.protocol.*;

/**
 * Handler处理器接口
 */
public interface MQTTProtocolHandler {

    public void onRead(MQTTConnection connection,ConnectProtocol message);

    public void onRead(MQTTConnection connection,ConnackProtocol message);

    public void onRead(MQTTConnection connection,DisconnectProtocol message);

    public void onRead(MQTTConnection connection,PingreqProtocol message);

    public void onRead(MQTTConnection connection,PingrespProtocol message);

    public void onRead(MQTTConnection connection,PubcompProtocol message);

    public void onRead(MQTTConnection connection,PublishProtocol message);

    public void onRead(MQTTConnection connection,PubackProtocol message);

    public void onRead(MQTTConnection connection,PubrelProtocol message);

    public void onRead(MQTTConnection connection,PubrecProtocol message);

    public void onRead(MQTTConnection connection,SubackProtocol message);

    public void onRead(MQTTConnection connection,SubscribeProtocol message);

    public void onRead(MQTTConnection connection,UnsubscribeProtocol message);

    public void onRead(MQTTConnection connection,UnsubackProtocol message);

    public void onClose(MQTTConnection connection);

    public void onException(MQTTConnection connection);

}
