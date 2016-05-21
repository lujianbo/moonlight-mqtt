package io.github.lujianbo.sentinelmq.common.handler;

import io.github.lujianbo.sentinelmq.common.protocol.*;

/**
 * 负责处理MQTT协议相关的内容,作为Mqtt的协议前置处理，其主要目的是将MQTT协议抽象到
 * 一个通用的发布订阅服务上
 */
public interface MQTTProtocolHandler {

    /**
     * 处理MQTT的连接和登陆部分的代码
     */
    public void onRead(MQTTConnection connection, ConnectProtocol message);

    /**
     * 登陆信息的返回，在服务端不会出现该数据包
     */
    public void onRead(MQTTConnection connection, ConnackProtocol message);


    public void onRead(MQTTConnection connection, DisconnectProtocol message);

    /**
     * 心跳协议的实现
     */
    public void onRead(MQTTConnection connection, PingreqProtocol message);


    public void onRead(MQTTConnection connection, PingrespProtocol message);

    public void onRead(MQTTConnection connection, PublishProtocol message);


    public void onRead(MQTTConnection connection, PubackProtocol message);

    public void onRead(MQTTConnection connection, PubcompProtocol message);

    public void onRead(MQTTConnection connection, PubrelProtocol message);

    public void onRead(MQTTConnection connection, PubrecProtocol message);

    /**
     * 在服务器端不会接收到 SubackProtocol
     */
    public void onRead(MQTTConnection connection, SubackProtocol message);

    /**
     * 订阅相关的消息服务
     */
    public void onRead(MQTTConnection connection, SubscribeProtocol message);

    /**
     * 取消订阅的操作
     */
    public void onRead(MQTTConnection connection, UnsubscribeProtocol message);


    public void onRead(MQTTConnection connection, UnsubackProtocol message);


    public void onClose(MQTTConnection connection);


    public void onException(MQTTConnection connection);

}
