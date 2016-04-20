package io.github.lujianbo.context.impl;

import io.github.lujianbo.context.manager.AuthManager;
import io.github.lujianbo.context.manager.TopicManager;
import io.github.lujianbo.context.service.ContextService;

/**
 * Created by lujianbo on 2016/4/19.
 */
public class DefaultContextService implements ContextService{

    private final TopicManager topicManager;

    private final AuthManager authManager;

    public DefaultContextService() {
        this.topicManager = new DefaultMQTTTopicManager();
        this.authManager=new DefaultAuthManager();
    }

    public TopicManager getTopicManager() {
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
