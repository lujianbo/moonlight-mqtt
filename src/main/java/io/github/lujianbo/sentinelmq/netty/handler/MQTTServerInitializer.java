package io.github.lujianbo.sentinelmq.netty.handler;

import io.github.lujianbo.sentinelmq.netty.mqtt.MQTTServerCodec;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * MQTT的初始化类
 */
public class MQTTServerInitializer extends ChannelInitializer<SocketChannel> {

    private HandlerContext handlerContext;

    public MQTTServerInitializer(HandlerContext handlerContext) {
        this.handlerContext = handlerContext;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new MQTTServerCodec());
        pipeline.addLast(new MQTTServerHandler(handlerContext.getHandler()));
    }
}
