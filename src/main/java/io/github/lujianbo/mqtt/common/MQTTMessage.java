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

    public MQTTMessage(String sender, String topic, byte[] playLoad) {
        this.sender = sender;
        this.topic = topic;
        this.playLoad = playLoad;
    }

    public MQTTMessage() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public byte[] getPlayLoad() {
        return playLoad;
    }

    public void setPlayLoad(byte[] playLoad) {
        this.playLoad = playLoad;
    }
}
