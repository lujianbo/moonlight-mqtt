package io.github.lujianbo.driver.core;

import io.github.lujianbo.driver.common.PublishMessage;

/**
 * Created by lujianbo on 2016/4/20.
 */
public interface MQTTEngineListener {

    /**
     * 推送信息
     * */
    public void onPublish(PublishMessage message);
}
