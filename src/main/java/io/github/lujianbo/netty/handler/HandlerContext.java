package io.github.lujianbo.netty.handler;

import io.github.lujianbo.mqtt.service.MQTTContext;
import io.netty.handler.ssl.SslContext;

import static io.netty.handler.codec.http.HttpHeaders.Names.HOST;

/**
 * 处理容器，用来装载完成整个对接过程中需要的信息
 */
public class HandlerContext {

    private MQTTContext mqttContext;

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

    public MQTTContext getMqttContext() {
        return mqttContext;
    }

    public void setMqttContext(MQTTContext mqttContext) {
        this.mqttContext = mqttContext;
    }

    public SslContext getSslCtx() {
        return sslCtx;
    }

    public void setSslCtx(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }
}
