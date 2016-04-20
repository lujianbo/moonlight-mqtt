package io.github.lujianbo.context.manager;

import java.util.Iterator;

/**
 * Topic管理，
 * 根据规范,Topic利用 /xx/xx  的方式进行描述
 */
public interface TopicManager {

    /**
     * 订阅频道
     * */
    public boolean subscribe(String clientId, String topicFilter);

    /**
     * 反订阅
     * */
    public boolean unSubscribe(String clientId, String topicFilter);

    /**
     * 返回某个频道的监听者
     * */
    public Iterator<String> findSubscriber(String topicFilter);

    /**
     * 清理用户的所有监听
     * */
    public void clear(String clientId);
}
