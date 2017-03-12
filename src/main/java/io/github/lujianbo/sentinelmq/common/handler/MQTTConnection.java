package io.github.lujianbo.sentinelmq.common.handler;

import io.github.lujianbo.sentinelmq.common.protocol.*;

import java.util.concurrent.ConcurrentHashMap;


/**
 * 代表的是一个实际的MQTT 协议实现者的连接
 */
public abstract class MQTTConnection {

    /**
     * 存储clientId
     */
    protected String clientId;
    /**
     * 存储一些属性
     */
    protected ConcurrentHashMap<String, String> Attributes = new ConcurrentHashMap<>();

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public ConcurrentHashMap<String, String> getAttributes() {
        return Attributes;
    }

    public void setAttributes(ConcurrentHashMap<String, String> attributes) {
        Attributes = attributes;
    }

    abstract public void write(MQTTProtocol message);

    abstract public void close();

    abstract public void onException();


}
