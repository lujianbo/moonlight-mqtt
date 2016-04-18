package io.github.lujianbo.mqtt.manager.impl;

import io.github.lujianbo.mqtt.common.MQTTTopic;
import io.github.lujianbo.mqtt.util.Tree;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.List;

/**
 * Topic的默认实现，使用内存来管理可以订阅的频道，和管理频道上的订阅者
 * */
public class DefaultMQTTTopicManager {

    private Tree<MQTTTopic> tree;

    /**
     * 订阅
     * */
    public void subscribe(String clientId,String topicFilter){
        List<MQTTTopic> topics = findMatchTopic(topicFilter);
        for (MQTTTopic topic:topics){
            topic.addListener(clientId);
        }
    }

    /**
     * 反订阅
     * */
    public void unSubscribe(String clientId,String topicFilter){
        List<MQTTTopic> topics = findMatchTopic(topicFilter);
        for (MQTTTopic topic:topics){
            topic.removeListener(clientId);
        }
    }



    /**
     * 查找匹配的 topic
     */
    public List<MQTTTopic> findMatchTopic(String topicFilter) {
        String[] tokens=parserToken(topicFilter);
        //根据tokens,来在树里进行遍历和查找

        List<MQTTTopic> topics = null;
        return topics;
    }


    private String[] parserToken(String topicFilter) {
        String[] tokens = StringUtils.split(topicFilter, "/");
        return tokens;
    }


}
