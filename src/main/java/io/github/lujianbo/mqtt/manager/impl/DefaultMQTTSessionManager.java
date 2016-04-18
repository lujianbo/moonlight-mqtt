package io.github.lujianbo.mqtt.manager.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.lujianbo.mqtt.common.MQTTSession;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lujianbo on 2016/4/18.
 */
public class DefaultMQTTSessionManager {

    /**
     *  biMap 作为 client 和 mqtt Session的对应
     * */
    private ConcurrentHashMap<String,MQTTSession> clientIdMaps=new ConcurrentHashMap<>();


    public DefaultMQTTSessionManager(){

    }
}
