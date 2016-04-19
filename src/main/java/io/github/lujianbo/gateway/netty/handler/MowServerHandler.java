package io.github.lujianbo.gateway.netty.handler;

import io.github.lujianbo.gateway.netty.mqtt.MQTTServerCodec;
import io.github.lujianbo.gateway.netty.websocket.WebSocketTransportCodec;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * 完成 websocket 的握手和后续处理器的配置
 */
public class MowServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private HandlerContext context;

    public MowServerHandler(HandlerContext context) {
        this.context = context;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        if (!req.getDecoderResult().isSuccess()) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
            return;
        }
        // Allow only GET methods.
        if (req.getMethod() != GET) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN));
            return;
        }

        /**
         * 当且仅当uri为指定path的时候,进行websocket通讯的升级
         * */
        if (context.getPath().equals(req.getUri())
                //CONNECTION 字段的值为 UPGRADE
                && HttpHeaders.Values.UPGRADE.equalsIgnoreCase(req.headers().get(HttpHeaders.Names.CONNECTION))
                //UPGRADE 字段的值为 WEBSOCKET
                && HttpHeaders.Values.WEBSOCKET.equalsIgnoreCase(req.headers().get(HttpHeaders.Names.UPGRADE))
                ) {
            WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                    context.getWebSocketLocation(), null, true, 5 * 1024 * 1024);
            WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(req);
            if (handshaker == null) {
                /**
                 * 不支持的协议
                 * */
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            } else {
                /**
                 * 握手结束后补充如下协议
                 * */
                handshaker.handshake(ctx.channel(), req).addListener(future -> {
                    if (future.isSuccess()) {
                        /**
                         * 添加websocket Frame 的支持
                         * */
                        ctx.pipeline().addLast(new WebSocketTransportCodec(handshaker));

                        /**
                         * 添加Mqtt的协议支持
                         * */
                        ctx.pipeline().addLast(new MQTTServerCodec());

                        /**
                         * 添加协议的处理桥接部分
                         * */
                        ctx.pipeline().addLast(new MQTTServerHandler(context.getFactory()));
                    }
                });
            }
            return;
        }
        //错误的情况
        sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND));
    }


    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
        // Generate an error page if response getStatus code is not OK (200).
        if (res.getStatus().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            HttpHeaders.setContentLength(res, res.content().readableBytes());
        }

        // Send the response and close the connection if necessary.
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpHeaders.isKeepAlive(req) || res.getStatus().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }


}
