package io.github.lujianbo.sentinelmq.netty.websocket;

import io.netty.channel.CombinedChannelDuplexHandler;

/**
 * Created by jianbo on 2016/3/28.
 */
public final class WebSocketTransportCodec extends CombinedChannelDuplexHandler<WebSocketTransportDecoder, WebSocketTransportEncoder> {

    public WebSocketTransportCodec() {
        super(new WebSocketTransportDecoder(), new WebSocketTransportEncoder());
    }
}
