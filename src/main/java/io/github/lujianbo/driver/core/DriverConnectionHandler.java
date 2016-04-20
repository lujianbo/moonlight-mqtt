package io.github.lujianbo.driver.core;

import io.github.lujianbo.driver.service.DriverConnection;
import io.github.lujianbo.driver.common.AuthMessage;
import io.github.lujianbo.driver.common.PublishMessage;
import io.github.lujianbo.driver.common.SubscribeMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class DriverConnectionHandler implements MQTTEngineListener{

    /**
     * 存储clientId和 DriverConnection的对应关系，用于下发广播事件
     * */
    private Map<String,DriverConnection> maps=new ConcurrentHashMap<>();


    public void onConnection(DriverConnection connection){

    }

    public void oauth(AuthMessage authMessage){

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
