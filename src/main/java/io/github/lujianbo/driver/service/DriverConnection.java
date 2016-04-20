package io.github.lujianbo.driver.service;

import io.github.lujianbo.driver.common.BroadcastMessage;

/**
 * 连接到Driver的Connection 的抽象接口
 * */
public interface DriverConnection {


    /**
     * 广播接口
     * */
    public void broadcast(BroadcastMessage message);

    /**
     * 关闭clientId
     * */
    public void close(String clientId);
}
