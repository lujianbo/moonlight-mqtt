package io.github.lujianbo.mqtt.common;

/**
 * 代表一个 MQTT 连接/用户的抽象 .
 */
public abstract class MQTTSession {

    private final String clientId;

    public MQTTSession(String clientId) {
        this.clientId = clientId;
    }

}
