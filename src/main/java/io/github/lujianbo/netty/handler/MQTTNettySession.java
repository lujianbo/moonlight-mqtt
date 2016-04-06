package io.github.lujianbo.netty.handler;


import io.netty.channel.Channel;

/**
 *
 */
public class MQTTNettySession {

    private Channel channel;

    public MQTTNettySession(Channel channel){
        this.channel=channel;
    }


}
