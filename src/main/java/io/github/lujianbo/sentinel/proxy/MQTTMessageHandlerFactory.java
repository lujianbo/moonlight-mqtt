package io.github.lujianbo.sentinel.proxy;


/**
 * 通信框架,通过这个接口将session进行注册和提交
 * */
public interface MQTTMessageHandlerFactory {

    /**
     *
     * */
    public MQTTMessageHandler register(MQTTConnection session);

}
