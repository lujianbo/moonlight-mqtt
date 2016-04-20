package io.github.lujianbo.driver.core;


import io.github.lujianbo.context.service.ContextService;
import io.github.lujianbo.driver.common.PublishMessage;
import io.github.lujianbo.driver.common.SubscribeMessage;
import io.github.lujianbo.driver.common.UnSubscribeMessage;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MQTTEngine {

    private final ContextService contextService;

    /**
     * 回调接口集合
     * */
    private Set<MQTTEngineListener> listeners= new HashSet<>();


    private ExecutorService pool= Executors.newCachedThreadPool(runnable -> {
        Thread thread=new Thread();
        thread.setDaemon(true);
        return thread;
    });

    public MQTTEngine(ContextService contextService) {
        this.contextService = contextService;
    }

    public void addListener(MQTTEngineListener listener){
        this.listeners.add(listener);
    }

    public void removeListener(MQTTEngineListener listener){
        this.listeners.remove(listener);
    }

    /**
     * 推送信息
     * */
    public void publish(PublishMessage publishMessage){

    }

    /**
     * 修改订阅
     * */
    public void subscribe(SubscribeMessage subscribeMessage){

    }

    /**
     * 修改订阅
     * */
    public void unSubscribe(UnSubscribeMessage unSubscribeMessage){

    }
}
