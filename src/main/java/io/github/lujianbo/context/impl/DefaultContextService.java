package io.github.lujianbo.context.impl;

import io.github.lujianbo.context.manager.AuthManager;
import io.github.lujianbo.context.manager.TopicManager;
import io.github.lujianbo.context.service.ContextService;

import java.util.Iterator;

public class DefaultContextService implements ContextService {

    private final TopicManager topicManager;

    private final AuthManager authManager;

    public DefaultContextService() {
        this.topicManager = new DefaultMQTTTopicManager("testTopic");
        this.authManager = new DefaultAuthManager();
    }

    public TopicManager getTopicManager() {
        return topicManager;
    }


    @Override
    public boolean subscribe(String clientId, String topicFilter, byte qos) {
        return topicManager.subscribe(clientId, topicFilter);
    }

    @Override
    public boolean unSubscribe(String clientId, String topicFilter) {
        return topicManager.unSubscribe(clientId, topicFilter);
    }

    @Override
    public void clear(String clientId) {
        topicManager.clear(clientId);
    }

    @Override
    public Iterator<String> findSubscriber(String topicFilter) {
        return topicManager.findSubscriber(topicFilter);
    }
}
