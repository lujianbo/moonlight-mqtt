package io.github.lujianbo.mqtt.service;


public interface MQTTContext {

    /**
     * 返回MQTTSession的实现
     * */
    public MQTTSession getNewMQTTSession();

}
