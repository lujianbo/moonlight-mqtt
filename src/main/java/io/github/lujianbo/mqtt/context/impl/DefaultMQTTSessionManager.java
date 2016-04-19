package io.github.lujianbo.mqtt.context.impl;

import io.github.lujianbo.mqtt.driver.common.MQTTSession;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的session管理
 */
public class DefaultMQTTSessionManager {

    /**
     *  biMap 作为 client 和 mqtt Session的对应
     * */
    private ConcurrentHashMap<String,MQTTSession> clientIdMaps=new ConcurrentHashMap<>();


    public DefaultMQTTSessionManager(){

    }
}
