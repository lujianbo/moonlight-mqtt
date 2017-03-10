package io.github.lujianbo.sentinelmq.netty.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class WebSocketTransportDecoder extends MessageToMessageDecoder<WebSocketFrame> {

    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {
        if (msg instanceof CloseWebSocketFrame) {
            Channel channel=ctx.channel();
            channel.writeAndFlush((CloseWebSocketFrame) msg.retain(), channel.newPromise()).addListener(ChannelFutureListener.CLOSE);
            return;
        }
        if (msg instanceof TextWebSocketFrame) {
            //TextWebSocketFrame 已经被禁止
            Channel channel=ctx.channel();
            channel.writeAndFlush(new CloseWebSocketFrame(), channel.newPromise()).addListener(ChannelFutureListener.CLOSE);
            return;
        }

        //Mqtt只能通过BinaryWebSocket来进行传输
        if (msg instanceof BinaryWebSocketFrame) {
            //将数据传输到下一个handler
            out.add(msg.content().retain());

        }
    }
}
