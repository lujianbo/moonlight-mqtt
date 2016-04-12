package io.github.lujianbo.netty.handler;


import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.lujianbo.mqtt.domain.*;
import io.github.lujianbo.mqtt.service.MQTTContext;
import io.github.lujianbo.mqtt.service.MQTTMessageHandler;
import io.github.lujianbo.util.ObjectMapperUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 完成MQTT和Netty部分的对接，实现数据的上下行
 * */
public class MQTTServerHandler extends SimpleChannelInboundHandler<MQTTMessage> {
    private MQTTContext context;

    private MQTTMessageHandler handler;

    public MQTTServerHandler(MQTTContext context){
        this.context=context;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        handler=context.register(new MQTTNettySession(ctx.channel()));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MQTTMessage msg) throws Exception {
        handler.onRead(msg);
    }

}
