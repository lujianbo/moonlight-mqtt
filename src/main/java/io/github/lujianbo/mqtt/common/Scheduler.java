package io.github.lujianbo.mqtt.common;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 用来执行任务
 */
public class Scheduler {

    /**
     * 执行任务的线程池
     *
     * 此处应当有更多的和更合适的抽象
     *
     * */
    private ExecutorService pool= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), runnable -> {
        Thread thread=new Thread();
        thread.setDaemon(true);
        return thread;
    });

    public void submit(Runnable runnable){
        this.pool.submit(runnable);
    }
}
