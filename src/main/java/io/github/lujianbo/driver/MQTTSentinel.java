package io.github.lujianbo.driver;

import io.github.lujianbo.driver.common.BroadcastMessage;

/**
 * Sentinel的接口,描述了一个sentinel必须实现的接口
 * */
public abstract class MQTTSentinel {

    /**
     * 哨兵的Id
     * */
    private String sentinelId;


    abstract public void broadcast(BroadcastMessage message);

    /**
     * 关闭clientId
     * */
    abstract public void close(String clientId);
}
