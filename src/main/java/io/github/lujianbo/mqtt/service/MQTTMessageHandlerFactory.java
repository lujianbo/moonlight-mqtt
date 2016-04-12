package io.github.lujianbo.mqtt.service;


public interface MQTTMessageHandlerFactory {

    /**
     *
     * */
    public MQTTMessageHandler register(MQTTSession session);


}
