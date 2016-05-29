package io.github.lujianbo.sentinelmq.netty.handler;

import io.github.lujianbo.sentinelmq.common.handler.MQTTProtocolHandler;
import io.github.lujianbo.sentinelmq.netty.mqtt.MQTTServerCodec;
import io.github.lujianbo.sentinelmq.netty.websocket.WebSocketHandshaker;
import io.github.lujianbo.sentinelmq.netty.websocket.WebSocketTransportCodec;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;

/**
 * 完成初始化的编解码配置
 */
public class MQTTServerInitializer extends ChannelInitializer<SocketChannel> {

    private MQTTProtocolHandler handler;

    private String path = "";

    private SslContext sslCtx;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        /**
         * 判断是否需要添加ssl支持
         * */
        if (this.sslCtx!=null){
            pipeline.addLast(sslCtx.newHandler(ch.alloc()));
        }
        /**
         * 判断是否需要添加websocket支持
         * */
        if (!path.equals("")){
            /**
             * 编解码Http协议
             * */
            pipeline.addLast(new HttpServerCodec());
            pipeline.addLast(new HttpObjectAggregator(65536));
            /**
             * 完成握手和后置的Handler配置
             * */
            pipeline.addLast(new WebSocketHandshaker());
            /**
             * 添加websocket Frame 的支持
             * */
            pipeline.addLast(new WebSocketTransportCodec());
        }
        /**
         * 添加Mqtt的协议支持
         * */
        pipeline.addLast(new MQTTServerCodec());
        /**
         * 添加协议的处理桥接部分
         * */
        pipeline.addLast(new MQTTServerHandler(handler));
        pipeline.fireChannelActive();
    }

    public MQTTProtocolHandler getHandler() {
        return handler;
    }

    public void setHandler(MQTTProtocolHandler handler) {
        this.handler = handler;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public SslContext getSslCtx() {
        return sslCtx;
    }

    public void setSslCtx(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }
}
