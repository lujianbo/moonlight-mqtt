package io.github.lujianbo.driver;


import io.github.lujianbo.common.MQTTMessage;
import io.github.lujianbo.context.MQTTContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MQTTEngine {

    private final MQTTContext context;

    public MQTTEngine(MQTTContext context) {
        this.context = context;
    }

    private ExecutorService pool= Executors.newCachedThreadPool(runnable -> {
        Thread thread=new Thread();
        thread.setDaemon(true);
        return thread;
    });


    public void publish(MQTTMessage message){
        /**
         * 从context中获取订阅频道的订阅者
         * */
       pool.submit(() -> context.getTopicManager().findSubscriber(message.getTopic()).forEach(clientId -> {

       }));


        /**
         * 找到订阅者，发送订阅信息
         * */

    }
}
