package io.github.lujianbo.context.manager;

/**
 * Topic管理，
 * 根据规范,Topic利用 /xx/xx  的方式进行描述
 */
public interface TopicManager {


    public boolean subscribe(String clientId, String topicFilter);


    public boolean unSubscribe(String clientId, String topicFilter);

    public Iterable<String> findSubscriber(String topicFilter);

    public void clear(String clientId);
}
