package io.github.lujianbo.server;

import io.github.lujianbo.mqtt.manager.MQTTContext;
import io.github.lujianbo.mqtt.handler.MQTTMessageHandlerFactory;
import io.github.lujianbo.netty.MowServer;
import io.github.lujianbo.netty.handler.HandlerContext;

/**
 * 启动器
 */
public class BootStrap {

    /**
     * 启动MqttContext
     * */
    private MQTTContext context;

    /**
     *  获取通向MqttContext的Proxy
     * */
    private MQTTMessageHandlerFactory factory;

    /**
     * 配置 Netty网络部分
     * */
    private HandlerContext handlerContext;

    /**
     * 启动网络服务
     * */
    private MowServer mowServer;



    public static void main(String[] args) {


    }

}
