package io.github.lujianbo.driver.core;


import io.github.lujianbo.context.service.ContextService;
import io.github.lujianbo.driver.common.PublishMessage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MQTTEngine {

    private final ContextService contextService;

    private ExecutorService pool= Executors.newCachedThreadPool(runnable -> {
        Thread thread=new Thread();
        thread.setDaemon(true);
        return thread;
    });

    public MQTTEngine(ContextService contextService) {
        this.contextService = contextService;
    }


    public void publish(PublishMessage message){
        /**
         * 从ContextService中获取需要广播的clientId的集合
         * */
       pool.submit(() -> contextService.findSubscriber(message.getTopic()).forEach(clientId -> {

       }));

       /**
        * 从clientId找到其对应的 sentinel, 构建一个sentinel任务
        *
        *  向sentinel下发任务
        * */


        /**
         * 找到订阅者，发送订阅信息
         * */

    }
}
