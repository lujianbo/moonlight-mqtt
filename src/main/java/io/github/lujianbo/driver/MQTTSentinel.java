package io.github.lujianbo.driver;

import io.github.lujianbo.driver.common.BroadcastMessage;

/**
 * Sentinel的抽象描述
 * */
public interface MQTTSentinel {


    public void broadcast(BroadcastMessage message);

    /**
     * 关闭clientId
     * */
    public void close(String clientId);
}
