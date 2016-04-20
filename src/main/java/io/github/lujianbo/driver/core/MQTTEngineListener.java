package io.github.lujianbo.driver.core;

import io.github.lujianbo.driver.common.BroadcastMessage;

/**
 * MQTTEngine 上的监听者
 */
public interface MQTTEngineListener {

    /**
     * 推送信息
     * */
    public void onPublish(BroadcastMessage message);
}
