package io.github.lujianbo.launch;

import io.github.lujianbo.mqtt.context.MQTTContext;
import io.github.lujianbo.mqtt.driver.MQTTMessageHandlerFactory;
import io.github.lujianbo.mqtt.gateway.netty.MowServer;
import io.github.lujianbo.mqtt.gateway.netty.handler.HandlerContext;

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
