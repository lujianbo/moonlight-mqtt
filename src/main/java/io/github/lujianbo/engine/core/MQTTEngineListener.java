package io.github.lujianbo.engine.core;

import io.github.lujianbo.engine.common.BroadcastMessage;

/**
 * MQTTEngine 上的监听者
 */
public interface MQTTEngineListener {

    /**
     * 推送信息
     */
    public void broadcast(BroadcastMessage message);
}
