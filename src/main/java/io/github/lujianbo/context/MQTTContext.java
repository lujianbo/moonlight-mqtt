package io.github.lujianbo.context;


/**
 * MQTT 相关的上下文
 */
public class MQTTContext {

    private final MQTTSessionManager sessionManager;

    private final MQTTTopicManager topicManager;


    public MQTTContext(MQTTSessionManager sessionManager, MQTTTopicManager topicManager) {
        this.sessionManager = sessionManager;
        this.topicManager = topicManager;
    }

    public MQTTSessionManager getSessionManager() {
        return sessionManager;
    }

    public MQTTTopicManager getTopicManager() {
        return topicManager;
    }
}
