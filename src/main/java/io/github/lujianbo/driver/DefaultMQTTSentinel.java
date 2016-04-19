package io.github.lujianbo.driver;

import io.github.lujianbo.driver.common.BroadcastMessage;

/**
 * 默认的 MQTTSentinel,代表了是应用内的链接而已
 */
public class DefaultMQTTSentinel extends MQTTSentinel{


    @Override
    public void broadcast(BroadcastMessage message) {

    }

    @Override
    public void close(String clientId) {

    }
}
