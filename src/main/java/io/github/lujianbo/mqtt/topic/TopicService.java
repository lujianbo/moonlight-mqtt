package io.github.lujianbo.mqtt.topic;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * topic 订阅相关的接口
 */
public interface TopicService<T> {

    /**
     * 查找并且返回符合topicFilter的 topic
     * */
    public List<Topic<T>> findMatchTopic(String topicFilter);

    /**
     * 查找指定名称的Topic
     * */
    public Topic<T> findTopic(String topicName);

    /**
     * 添加topic
     * */
    public void addTopic(String topicName);
}
