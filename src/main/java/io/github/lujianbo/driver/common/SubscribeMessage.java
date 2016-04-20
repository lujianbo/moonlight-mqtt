package io.github.lujianbo.driver.common;

/**
 * 订阅消息
 */
public class SubscribeMessage {

    public String clientId;

    public String topicName;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}
