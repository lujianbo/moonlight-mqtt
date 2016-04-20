package io.github.lujianbo.launch;

import io.github.lujianbo.context.impl.DefaultContextService;
import io.github.lujianbo.context.service.ContextService;
import io.github.lujianbo.driver.DriverServer;
import io.github.lujianbo.driver.core.MQTTEngine;
import io.github.lujianbo.driver.service.DefaultDriverService;
import io.github.lujianbo.driver.service.DriverExporter;
import io.github.lujianbo.driver.service.DriverService;
import io.github.lujianbo.driver.service.LocalDriverExporter;
import io.github.lujianbo.sentinel.SentinelServer;

/**
 * 启动器
 */
public class BootStrap {

    /**
     * 初始化容器资源
     * */
    private ContextService contextService;

    /**
     * MQTT的核心引擎
     * */
    private MQTTEngine engine;

    private DriverExporter exporter;

    /**
     * 初始化哨兵
     * */
    private SentinelServer sentinelServer;


    public BootStrap(){
        /**
         * 默认使用内存来实现context数据存储功能
         * */
        this.contextService=new DefaultContextService();

        /**
         * 初始化引擎
         * */
        this.engine= new MQTTEngine(contextService);

        this.exporter=new LocalDriverExporter();


        /**
         * 从exporter中获取service
         * */


        /**
         * 启动sentinel
         * */
        



    }


    private void registerHook(){

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {


        }));
    }

}
