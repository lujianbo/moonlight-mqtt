package io.github.lujianbo.sentinel.proxy;

/**
 * session注册接口的默认实现
 */
public class DefaultMQTTMessageHandlerFactory implements MQTTMessageHandlerFactory {


    @Override
    public MQTTMessageHandler register(MQTTConnection session) {
        return new DefaultMQTTMessageHandler(session);
    }
}
