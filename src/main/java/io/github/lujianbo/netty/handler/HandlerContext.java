package io.github.lujianbo.netty.handler;

import io.github.lujianbo.mqtt.service.MQTTMessageHandlerFactory;
import io.netty.handler.ssl.SslContext;

/**
 * 处理容器，用来装载完成整个对接过程中需要的信息
 */
public class HandlerContext {

    private MQTTMessageHandlerFactory factory;

    private String path;

    private String host;

    private SslContext sslCtx;

    public String getWebSocketLocation() {
        String location =  host + path;
        if (sslCtx!=null){
            return "wss://" + location;
        }else {
            return "ws://" + location;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public MQTTMessageHandlerFactory getFactory() {
        return factory;
    }

    public void setFactory(MQTTMessageHandlerFactory factory) {
        this.factory = factory;
    }

    public SslContext getSslCtx() {
        return sslCtx;
    }

    public void setSslCtx(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }
}
