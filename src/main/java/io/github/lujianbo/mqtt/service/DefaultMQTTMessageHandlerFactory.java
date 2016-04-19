package io.github.lujianbo.mqtt.service;

import io.github.lujianbo.mqtt.common.handler.MQTTConnection;
import io.github.lujianbo.mqtt.common.handler.MQTTMessageHandler;
import io.github.lujianbo.mqtt.common.handler.MQTTMessageHandlerFactory;
import io.github.lujianbo.mqtt.manager.MQTTContext;

/**
 * session注册接口的默认实现
 */
public class DefaultMQTTMessageHandlerFactory implements MQTTMessageHandlerFactory {


    private MQTTContext context;

    public DefaultMQTTMessageHandlerFactory(MQTTContext context){
        this.context=context;
    }

    @Override
    public MQTTMessageHandler register(MQTTConnection session) {
        return new DefaultMQTTMessageHandler(context,session);
    }
}
