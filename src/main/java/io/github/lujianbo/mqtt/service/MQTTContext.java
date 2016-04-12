package io.github.lujianbo.mqtt.service;


import io.github.lujianbo.mqtt.domain.MQTTMessage;

public interface MQTTContext {

    /**
     * 返回MQTTSession的实现
     * */
    public MQTTMessageHandler register(MQTTSession session);


}
