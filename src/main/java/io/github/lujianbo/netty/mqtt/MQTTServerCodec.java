package io.github.lujianbo.netty.mqtt;

import io.netty.channel.CombinedChannelDuplexHandler;

/**
 * Created by jianbo on 2016/3/30.
 */
public class MQTTServerCodec extends CombinedChannelDuplexHandler<MQTTDecoder,MQTTEncoder> {

    public MQTTServerCodec(){
        super(new MQTTDecoder(),new MQTTEncoder());
    }
}
