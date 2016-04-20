package io.github.lujianbo.driver.core;


import io.github.lujianbo.context.service.ContextService;

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

}
