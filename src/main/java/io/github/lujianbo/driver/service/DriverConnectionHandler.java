package io.github.lujianbo.driver.service;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.lujianbo.driver.core.MQTTEngine;
import io.github.lujianbo.driver.common.AuthMessage;
import io.github.lujianbo.driver.common.PublishMessage;
import io.github.lujianbo.driver.common.SubscribeMessage;

/**
 * 在分发广播事件的时候需要重新整理和派发新的事件
 */
public class DriverConnectionHandler {

    /**
     * 存储clientId和 DriverConnection的对应关系，用于下发广播事件
     * */
    private BiMap<String,DriverConnection> maps= HashBiMap.create();

    private MQTTEngine engine;

    public DriverConnectionHandler(MQTTEngine engine){
        this.engine=engine;

        /**
         * 注册publish的事件响应策略
         * */
        engine.addListener(message -> {

        });
    }

    public void oauth(DriverConnection connection,AuthMessage authMessage){
        if (engine.auth(authMessage)){
            /**
             * 添加相关的对应关系
             * */
            maps.put(authMessage.getClientId(),connection);
        }
    }


}
