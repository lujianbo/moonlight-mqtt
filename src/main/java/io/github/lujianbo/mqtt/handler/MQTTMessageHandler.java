package io.github.lujianbo.mqtt.handler;

import io.github.lujianbo.mqtt.protocol.*;

/**
 * Handler处理器接口
 */
public interface MQTTMessageHandler {

    public void onRead(MQTTProtocol message);
}
