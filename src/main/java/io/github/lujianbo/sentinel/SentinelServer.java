package io.github.lujianbo.sentinel;

import io.github.lujianbo.engine.core.MQTTEngine;
import io.github.lujianbo.sentinel.handler.MQTTProtocolHandler;

/**
 * 网关服务启动器
 */
public abstract class SentinelServer {

    protected MQTTProtocolHandler mqttProtocolHandler;

    protected MQTTEngine mqttEngine;

    public SentinelServer(MQTTEngine engine){
        this.mqttEngine=engine;
        this.mqttProtocolHandler=new MQTTProtocolHandler(engine);
    }

    /**
     * 启动服务器
     * */
    abstract public void start();

    /**
     * 停止服务器
     * */
    abstract public void stop();

}
