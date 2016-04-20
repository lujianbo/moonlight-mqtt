package io.github.lujianbo.driver.core;

import io.github.lujianbo.driver.service.DriverConnection;
import io.github.lujianbo.driver.common.AuthMessage;
import io.github.lujianbo.driver.common.PublishMessage;
import io.github.lujianbo.driver.common.SubscribeMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lujianbo on 2016/4/20.
 */
public class DriverConnectionHandler {

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

}
