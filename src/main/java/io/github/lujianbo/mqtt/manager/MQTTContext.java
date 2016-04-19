package io.github.lujianbo.mqtt.manager;


import io.github.lujianbo.mqtt.common.domain.MQTTMessage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * MQTT 相关的上下文
 */
public class MQTTContext {

    /**
     * 会话管理
     * */
    private MQTTSessionManager sessionManager;


    /**
     * 频道订阅管理
     * */
    private MQTTTopicManager topicManager;

    /**
     * scheduler
     * */
    private ExecutorService pool= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), runnable -> {
        Thread thread=new Thread();
        thread.setDaemon(true);
        return thread;
    });


    /**
     * 推送消息
     * */
    public void publish(MQTTMessage message){
        /**
         * 提交任务
         * */
        pool.submit(() -> {
            topicManager.findSubscriber(message.getTopic()).forEach(clientId -> {
                sessionManager.getMQTTSession(clientId);
            });
        });
    }

    /**
     * 订阅频道
     * */



}
