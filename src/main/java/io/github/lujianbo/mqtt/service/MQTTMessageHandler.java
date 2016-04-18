package io.github.lujianbo.mqtt.service;

import io.github.lujianbo.mqtt.domain.*;

/**
 * Handler处理器接口
 */
public interface MQTTMessageHandler {

    public void onRead(MQTTProtocol message);
}
