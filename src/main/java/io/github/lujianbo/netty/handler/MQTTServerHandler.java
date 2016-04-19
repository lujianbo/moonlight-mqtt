package io.github.lujianbo.netty.handler;


import io.github.lujianbo.mqtt.protocol.*;
import io.github.lujianbo.mqtt.handler.MQTTMessageHandler;
import io.github.lujianbo.mqtt.handler.MQTTMessageHandlerFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


/**
 * 完成MQTT和Netty部分的对接，实现数据的上下行
 */
public class MQTTServerHandler extends SimpleChannelInboundHandler<MQTTProtocol> {

    private MQTTMessageHandlerFactory factory;

    private MQTTMessageHandler handler;

    public MQTTServerHandler(MQTTMessageHandlerFactory factory) {
        this.factory = factory;
    }

    /**
     * 将session注册到factory中，并且从中获得对应的handler
     * */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        handler = factory.register(new MQTTNettyConnection(ctx.channel()));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MQTTProtocol msg) throws Exception {

        if (msg instanceof PingreqProtocol) {
            handler.onRead((PingreqProtocol)msg);
            return;
        }
        if (msg instanceof PingrespProtocol) {
            handler.onRead((PingrespProtocol)msg);
            return;
        }

        if (msg instanceof ConnectProtocol) {
            handler.onRead((ConnectProtocol)msg);
            return;
        }

        if (msg instanceof ConnackProtocol) {
            handler.onRead((ConnackProtocol)msg);
            return;
        }

        if (msg instanceof DisconnectProtocol) {
            handler.onRead((DisconnectProtocol)msg);
            return;
        }

        //处理subscribe
        if (msg instanceof SubscribeProtocol) {
            handler.onRead((SubscribeProtocol)msg);
            return;
        }

        if (msg instanceof SubackProtocol) {
            handler.onRead((SubackProtocol)msg);
            return;
        }

        //处理unSubscribe
        if (msg instanceof UnsubscribeProtocol) {
            handler.onRead((UnsubscribeProtocol)msg);
            return;
        }

        if (msg instanceof UnsubackProtocol) {
            handler.onRead((UnsubackProtocol)msg);
            return;
        }

        //处理 publish
        if (msg instanceof PublishProtocol) {
            handler.onRead((PublishProtocol)msg);
            return;
        }

        if (msg instanceof PubackProtocol) {
            handler.onRead((PubackProtocol)msg);
            return;
        }

        if (msg instanceof PubrecProtocol) {
            handler.onRead((PubrecProtocol)msg);
            return;
        }

        if (msg instanceof PubrelProtocol) {
            handler.onRead((PubrelProtocol)msg);
            return;
        }

        if (msg instanceof PubcompProtocol) {
            handler.onRead((PubcompProtocol)msg);
            return;
        }
    }

}
