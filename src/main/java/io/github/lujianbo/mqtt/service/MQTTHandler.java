package io.github.lujianbo.mqtt.service;

import io.github.lujianbo.mqtt.session.MQTTSession;

/**
 * MQTT协议的事件处理,留给通信底层来实现
 */
public interface MQTTHandler {

    /**
     * 超时
     *  */
    public void onTimeout(MQTTSession session);

    /**
     *
     * */
    public void onConnect();

    /**
     * 订阅
     * */
    public void subscribe();

    /**
     * 取消订阅
     * */
    public void unSubscribe();

    /**
     * publish message 处理部分
     * */

    public void onPublish();
}
