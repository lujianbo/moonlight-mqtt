package io.github.lujianbo.mqtt.topic;

/**
 * topic 的 订阅者
 */
public interface TopicListener {

    public void publish(String name,byte[] message);
}
