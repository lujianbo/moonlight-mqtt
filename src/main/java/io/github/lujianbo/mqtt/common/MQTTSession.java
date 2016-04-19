package io.github.lujianbo.mqtt.common;

import io.github.lujianbo.mqtt.handler.MQTTConnection;
import io.github.lujianbo.mqtt.manager.MQTTContext;

import java.util.LinkedList;
import java.util.List;

/**
 * 代表一个 MQTT 连接/用户的抽象 .
 */
public abstract class MQTTSession {

    /**
     * 该session的clientId
     * */
    private final String clientId;

    /**
     * 存储该session关注的topicName
     * */
    private List<String> topics=new LinkedList<>();

    /**
     * Context容器
     * */
    private MQTTContext context;

    private MQTTConnection connection;


    public MQTTSession(String clientId) {
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

}
