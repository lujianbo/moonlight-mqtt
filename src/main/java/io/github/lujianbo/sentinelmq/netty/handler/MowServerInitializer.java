package io.github.lujianbo.sentinelmq.netty.handler;

import io.github.lujianbo.sentinelmq.netty.HandlerContext;
import io.github.lujianbo.sentinelmq.netty.mqtt.MQTTServerCodec;
import io.github.lujianbo.sentinelmq.netty.websocket.WebSocketTransportCodec;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 完成初始化的编解码配置
 */
public class MowServerInitializer extends ChannelInitializer<SocketChannel> {

    private HandlerContext handlerContext;

    public MowServerInitializer(HandlerContext handlerContext) {
        this.handlerContext = handlerContext;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        /**
         * 判断是否需要添加ssl支持
         * */
        if (handlerContext.getSslCtx()!=null){
            pipeline.addLast(handlerContext.getSslCtx().newHandler(ch.alloc()));
        }

        if (handlerContext.isWebSocket()){
            /**
             * 编解码Http协议
             * */
            pipeline.addLast(new HttpServerCodec());
            pipeline.addLast(new HttpObjectAggregator(65536));
            /**
             * 完成握手和后置的Handler配置
             * */
            pipeline.addLast(new MowServerHandshaker(handlerContext));

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
        pipeline.addLast(new MQTTServerHandler(handlerContext.getHandler()));
        pipeline.fireChannelActive();
    }
}
