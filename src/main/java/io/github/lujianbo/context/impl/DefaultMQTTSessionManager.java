package io.github.lujianbo.context.impl;

import io.github.lujianbo.sentinel.proxy.MQTTSession;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

/**
 * 默认的session管理
 */
public class DefaultMQTTSessionManager implements MQTTSessionManager{

    /**
     *  biMap 作为 client 和 mqtt Session的对应
     * */
    private ConcurrentHashMap<String,MQTTSession> clientIdMaps=new ConcurrentHashMap<>();


    public DefaultMQTTSessionManager(){

    }

    @Override
    public MQTTSession getMQTTSession(String clientId) {
        return null;
    }

    @Override
    public void remove(String clientId) {

    }

    @Override
    public void closeAll() {

    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void forEach(BiConsumer<String, MQTTSession> consumer) {

    }
}
