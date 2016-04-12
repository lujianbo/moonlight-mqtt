package io.github.lujianbo.mqtt.topic;

import io.github.lujianbo.mqtt.common.Tree;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by jianbo on 2016/3/31.
 */
public class TopicManager<T> {

    private Tree<Topic<T>> tree;

    /**
     * 查找匹配的 topic
     */
    public List<Topic<T>> findMatchTopic(String topicFilter) {
        List<Topic<T>> topics = null;
        return topics;
    }

    /**
     * 查找指定名称的topic
     */
    public Topic findTopic(String topicName) {
        return null;
    }

    private String[] parserToken(String topicFilter) {
        String[] tokens = StringUtils.split(topicFilter, "/");
        return tokens;
    }
}
