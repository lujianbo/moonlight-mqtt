package io.github.lujianbo.sentinelmq.netty;

import io.github.lujianbo.sentinelmq.common.handler.MQTTProtocolHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

/**
 * 处理容器，用来装载完成整个对接过程中需要的信息
 */
public class HandlerContext {

    private MQTTProtocolHandler handler;

    private String path = "/mqtt";

    private String host = "localhost";

    private int port = 8080;

    private SslContext sslCtx;

    private boolean webSocket=true;

    public boolean isWebSocket() {
        return webSocket;
    }

    public void setWebSocket(boolean webSocket) {
        this.webSocket = webSocket;
    }


    public String getWebSocketLocation() {
        String location = host + path;
        if (sslCtx != null) {
            return "wss://" + location;
        } else {
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

    public SslContext getSslCtx() {
        return sslCtx;
    }

    public void setSslCtx(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    public MQTTProtocolHandler getHandler() {
        return handler;
    }

    public void setHandler(MQTTProtocolHandler handler) {
        this.handler = handler;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void createSSl(){
        try {
            SelfSignedCertificate ssc;
            ssc = new SelfSignedCertificate();
            this.sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey())
                    .build();
        } catch (CertificateException | SSLException e) {
            e.printStackTrace();
        }
    }
}
