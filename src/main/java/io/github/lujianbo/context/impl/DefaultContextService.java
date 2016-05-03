package io.github.lujianbo.context.impl;

import io.github.lujianbo.context.manager.AuthManager;
import io.github.lujianbo.context.manager.TopicManager;
import io.github.lujianbo.context.service.ContextService;

import java.util.Iterator;

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
    public boolean subscribe(String clientId, String topicFilter, byte qos) {
        topicManager.subscribe(clientId,topicFilter);
        return true;
    }

    @Override
    public boolean unSubscribe(String clientId, String topicFilter) {

        topicManager.unSubscribe(clientId,topicFilter);
        return true;
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
