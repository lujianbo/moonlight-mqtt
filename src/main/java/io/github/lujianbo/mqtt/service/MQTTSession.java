package io.github.lujianbo.mqtt.service;

import io.github.lujianbo.mqtt.domain.MQTTMessage;

public interface MQTTSession {

    /**
     * write操作
     */
    public void write(MQTTMessage message);

    public void close();

    public void onException();
}
