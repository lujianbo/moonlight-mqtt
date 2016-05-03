package io.github.lujianbo.launch;

import io.github.lujianbo.context.impl.DefaultContextService;
import io.github.lujianbo.context.service.ContextService;
import io.github.lujianbo.engine.core.MQTTEngine;
import io.github.lujianbo.engine.wapper.SingleSentinelEngine;
import io.github.lujianbo.netty.NettyServer;
import io.github.lujianbo.sentinel.SentinelServer;

/**
 * 启动器
 */
public class BootStrap {

    /**
     * 初始化容器资源
     */
    private ContextService contextService;

    /**
     * MQTT的核心引擎
     */
    private MQTTEngine engine;


    private SentinelServer sentinelServer;

    public BootStrap() {
        /**
         * 默认使用内存来实现context数据存储功能
         * */
        this.contextService = new DefaultContextService();

        /**
         * 初始化引擎
         * */
        this.engine = new SingleSentinelEngine(contextService);


        this.sentinelServer = new NettyServer(engine);
        sentinelServer.start();
    }

    public static void main(String[] args) {
        new BootStrap();
    }


    private void registerHook() {

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            this.sentinelServer.stop();

        }));
    }

}
