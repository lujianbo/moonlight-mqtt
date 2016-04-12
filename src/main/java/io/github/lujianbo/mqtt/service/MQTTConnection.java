package io.github.lujianbo.mqtt.service;

import io.github.lujianbo.mqtt.domain.MQTTMessage;


/**
 *  代表的是一个MQTT的链接
 * */
public interface MQTTConnection {

    /**
     * write操作
     */
    public void write(MQTTMessage message);

    public void close();

    public void onException();
}
