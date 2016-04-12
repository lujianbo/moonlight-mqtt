package io.github.lujianbo.mqtt.manager;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.lujianbo.mqtt.common.MQTTSession;

/**
 * session管理
 */
public class MQTTSessionManager {

    /**
     *  biMap 作为 client 和 mqtt Session的对应
     * */
    private BiMap<String,MQTTSession> clientIdMaps= HashBiMap.create();



}
