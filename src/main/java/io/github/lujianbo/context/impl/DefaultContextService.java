package io.github.lujianbo.context.impl;

import io.github.lujianbo.context.service.ContextService;

/**
 * Created by lujianbo on 2016/4/19.
 */
public class DefaultContextService implements ContextService{

    private final MQTTSessionManager sessionManager;

    private final MQTTTopicManager topicManager;

    public DefaultContextService() {
        this.sessionManager = new DefaultMQTTSessionManager();
        this.topicManager = new DefaultMQTTTopicManager();
    }

    public MQTTSessionManager getSessionManager() {
        return sessionManager;
    }

    public MQTTTopicManager getTopicManager() {
        return topicManager;
    }

    @Override
    public boolean subscribe(String clientId, String topicFilter) {
        return false;
    }

    @Override
    public boolean unSubscribe(String clientId, String topicFilter) {
        return false;
    }

    @Override
    public Iterable<String> findSubscriber(String topicFilter) {
        return null;
    }
}
