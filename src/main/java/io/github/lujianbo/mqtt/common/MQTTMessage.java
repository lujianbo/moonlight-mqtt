package io.github.lujianbo.mqtt.common;

/**
 * 一条消息的抽象
 */
public class MQTTMessage {

    /**
     * 发送者
     * */
    public String sender;

    /**
     * 目标topic
     * */
    public String  topic;

    /**
     * 消息内容
     * */
    public byte[] playLoad;



}
