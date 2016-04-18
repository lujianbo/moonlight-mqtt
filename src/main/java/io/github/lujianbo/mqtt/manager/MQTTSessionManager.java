package io.github.lujianbo.mqtt.manager;

import io.github.lujianbo.mqtt.common.MQTTSession;

import java.util.function.BiConsumer;


/**
 * session管理
 */
public interface MQTTSessionManager {

    /**
     * 添加一个MQTTSession
     * */
    public void put(String clientId,MQTTSession session);

    /**
     * 关闭并移除一个session
     * */
    public void remove(String clientId);

    /**
     * 关闭所有的Session
     * */
    public void closeAll();

    /**
     * 根据clientId来查询session
     * */
    public MQTTSession getMQTTSession(String clientId);

    /**
     * 返回当前session的总数
     * */
    public long count();


    /**
     *  遍历所有的 clientId和MQTTSession
     * */
    public void forEach(BiConsumer<String,MQTTSession> consumer);
}
