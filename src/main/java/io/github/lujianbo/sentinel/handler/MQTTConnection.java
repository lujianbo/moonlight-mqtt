package io.github.lujianbo.sentinel.handler;

import io.github.lujianbo.sentinel.protocol.*;


/**
 *  代表的是一个MQTT的链接
 * */
public interface MQTTConnection {

    public void write(ConnectProtocol message);

    public void write(ConnackProtocol message);

    public void write(DisconnectProtocol message);

    public void write(PingreqProtocol message);

    public void write(PingrespProtocol message);

    public void write(PubcompProtocol message);

    public void write(PublishProtocol message);

    public void write(PubackProtocol message);

    public void write(PubrelProtocol message);

    public void write(PubrecProtocol message);

    public void write(SubackProtocol message);

    public void write(SubscribeProtocol message);

    public void write(UnsubscribeProtocol message);

    public void write(UnsubackProtocol message);

    public void close();

    public void onException();
}
