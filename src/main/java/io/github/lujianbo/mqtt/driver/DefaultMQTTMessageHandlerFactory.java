package io.github.lujianbo.mqtt.driver;

import io.github.lujianbo.mqtt.gateway.common.MQTTConnection;
import io.github.lujianbo.mqtt.context.MQTTContext;

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
