package io.github.lujianbo.context;

import io.github.lujianbo.common.MQTTSession;

import java.util.function.BiConsumer;

/**
 * session管理
 */
public interface MQTTSessionManager {


    /**
     * 获取或初始化一个session
     * */
    public MQTTSession getMQTTSession(String clientId);


    /**
     * 关闭并移除一个session
     * */
    public void remove(String clientId);

    /**
     * 关闭所有的Session
     * */
    public void closeAll();


    /**
     * 返回当前session的总数
     * */
    public long count();


    /**
     *  遍历所有的 clientId和MQTTSession
     * */
    public void forEach(BiConsumer<String,MQTTSession> consumer);
}
