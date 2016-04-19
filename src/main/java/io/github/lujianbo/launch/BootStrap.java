package io.github.lujianbo.launch;

import io.github.lujianbo.context.service.ContextService;
import io.github.lujianbo.driver.DriverServer;
import io.github.lujianbo.driver.service.DriverService;
import io.github.lujianbo.sentinel.SentinelServer;

/**
 * 启动器
 */
public class BootStrap {

    /**
     * 初始化容器接口
     * */
    private ContextService contextService;

    /**
     * 初始化 驱动接口
     * */
    private DriverService driverService;

    /**
     * 初始化哨兵
     * */
    private SentinelServer sentinelServer;


    private void registerHook(){

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {


        }));
    }

}
