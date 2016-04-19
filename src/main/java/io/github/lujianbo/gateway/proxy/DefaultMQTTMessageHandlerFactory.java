package io.github.lujianbo.gateway.proxy;

import io.github.lujianbo.common.MQTTConnection;
import io.github.lujianbo.context.MQTTContext;

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
