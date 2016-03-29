package io.github.lujianbo.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketTransportDecoder extends SimpleChannelInboundHandler<WebSocketFrame> {

    private final WebSocketServerHandshaker handshaker;

    private static final Logger logger = LoggerFactory.getLogger(WebSocketTransportDecoder.class);

    public WebSocketTransportDecoder(WebSocketServerHandshaker handshaker) {
        this.handshaker = handshaker;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        if (frame instanceof TextWebSocketFrame) {
            //TextWebSocketFrame 已经被禁止
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }

        //Mqtt只能通过BinaryWebSocket来进行传输
        if (frame instanceof BinaryWebSocketFrame) {
            //将数据传输到下一个handler
            ctx.fireChannelRead(frame.content());
            return;
        }
    }
}
