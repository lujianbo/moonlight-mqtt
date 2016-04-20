package io.github.lujianbo.driver.service;

import io.github.lujianbo.driver.common.BroadcastMessage;

import java.util.UUID;

/**
 * Sentinel的接口,描述了一个sentinel必须实现的接口
 * */
public abstract class DriverConnection {

    /**
     * 哨兵的Id
     * */
    protected final String sentinelId;

    protected DriverConnection() {
        sentinelId = UUID.randomUUID().toString();
    }

    abstract public void broadcast(BroadcastMessage message);

    /**
     * 关闭clientId
     * */
    abstract public void close(String clientId);
}
