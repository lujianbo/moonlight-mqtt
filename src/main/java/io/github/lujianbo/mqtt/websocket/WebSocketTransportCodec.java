package io.github.lujianbo.mqtt.websocket;

import io.netty.channel.CombinedChannelDuplexHandler;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;

/**
 * Created by jianbo on 2016/3/28.
 */
public final class WebSocketTransportCodec extends CombinedChannelDuplexHandler<WebSocketTransportDecoder,WebSocketTransportEncoder> {

    /**
     * 完成握手后添加的编解码
     * */
    private final WebSocketServerHandshaker handshaker;

    public WebSocketTransportCodec(WebSocketServerHandshaker handshaker){
        this.handshaker = handshaker;
    }
}
