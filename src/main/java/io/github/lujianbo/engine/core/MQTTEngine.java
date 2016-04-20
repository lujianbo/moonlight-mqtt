package io.github.lujianbo.engine.core;


import io.github.lujianbo.context.service.ContextService;
import io.github.lujianbo.engine.common.*;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class MQTTEngine {

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
        BroadcastMessage broadcastMessage=new BroadcastMessage();
        contextService.findSubscriber(publishMessage.getTopic()).forEach(s -> {
            broadcastMessage.getClientIds().add(s);
        });
        broadcastMessage.setPayload(publishMessage.getPlayLoad());
        this.listeners.forEach(mqttEngineListener -> mqttEngineListener.broadcast(broadcastMessage));
    }

    /**
     * 修改订阅
     * */
    public void subscribe(SubscribeMessage subscribeMessage){
        contextService.subscribe(subscribeMessage.clientId,subscribeMessage.getTopicName());
    }

    /**
     * 修改订阅
     * */
    public void unSubscribe(UnSubscribeMessage unSubscribeMessage){
        contextService.unSubscribe(unSubscribeMessage.clientId,unSubscribeMessage.getTopicName());
    }

    public boolean auth(AuthMessage authMessage){
        return true;
    }

    public void disconnect(String clientId){
        contextService.clear(clientId);
    }
}
