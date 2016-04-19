package io.github.lujianbo.mqtt.driver;


import io.github.lujianbo.mqtt.gateway.common.MQTTConnection;

/**
 * 通信框架,通过这个接口将session进行注册和提交
 * */
public interface MQTTMessageHandlerFactory {

    /**
     *
     * */
    public MQTTMessageHandler register(MQTTConnection session);

}
