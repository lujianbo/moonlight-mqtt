package io.github.lujianbo.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class WebSocketTransportDecoder extends MessageToMessageDecoder<WebSocketFrame> {

    private final WebSocketServerHandshaker handshaker;

    private static final Logger logger = LoggerFactory.getLogger(WebSocketTransportDecoder.class);

    public WebSocketTransportDecoder(WebSocketServerHandshaker handshaker) {
        this.handshaker = handshaker;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {
        if (msg instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) msg.retain());
            return;
        }
        if (msg instanceof TextWebSocketFrame) {
            //TextWebSocketFrame 已经被禁止
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) msg.retain());
            return;
        }

        //Mqtt只能通过BinaryWebSocket来进行传输
        if (msg instanceof BinaryWebSocketFrame) {
            //将数据传输到下一个handler
            out.add(msg.content().retain());
            return;
        }
    }
}
