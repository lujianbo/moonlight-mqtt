package io.github.lujianbo.sentinelmq.netty.handler;

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
         * SSL支持
         * */
        if (handlerContext.getSslCtx() != null) {
            pipeline.addLast(handlerContext.getSslCtx().newHandler(ch.alloc()));
        }

        /**
         * 编解码Http协议
         * */
        pipeline.addLast(new HttpServerCodec());

        pipeline.addLast(new HttpObjectAggregator(65536));

        /**
         * 完成握手和后置的Handler配置
         * */
        pipeline.addLast(new MowServerHandler(handlerContext));
    }
}
