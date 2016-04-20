package io.github.lujianbo.driver.service;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.lujianbo.driver.core.MQTTEngineListener;
import io.github.lujianbo.driver.common.AuthMessage;
import io.github.lujianbo.driver.common.PublishMessage;
import io.github.lujianbo.driver.common.SubscribeMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class DriverConnectionHandler implements MQTTEngineListener {

    /**
     * 存储clientId和 DriverConnection的对应关系，用于下发广播事件
     * */
    private BiMap<String,DriverConnection> maps= HashBiMap.create();


    public void onConnection(DriverConnection connection){

    }

    public void oauth(DriverConnection connection,AuthMessage authMessage){

        /**
         * 如果验证成功
         * */
        maps.put(authMessage.getClientId(),connection);

    }

    /**
     * 移除该clientId的相关信息
     * */
    public void disconnect(String clientId){

    }

    public void publish(DriverConnection connection, PublishMessage publishMessage){

    }

    public void subscribe(DriverConnection connection, SubscribeMessage subscribeMessage){

    }

    public void disConnection(DriverConnection connection){

    }

    @Override
    public void onPublish(PublishMessage message) {
        /**
         * 根据ClientId的所在位置进行分发
         * */


    }
}
