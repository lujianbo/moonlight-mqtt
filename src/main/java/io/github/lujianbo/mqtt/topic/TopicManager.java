package io.github.lujianbo.mqtt.topic;

import io.github.lujianbo.mqtt.util.Tree;
import org.apache.commons.lang3.StringUtils;

import java.util.List;


/**
 * 订阅树的管理
 * */
public class TopicManager {

    private Tree<Topic<String>> tree;

    /**
     * 订阅
     * */
    public void subscribe(String clientId,String topicFilter){
        List<Topic<String>> topics = findMatchTopic(topicFilter);
        for (Topic<String> topic:topics){
            topic.addListener(clientId);
        }
    }

    /**
     * 反订阅
     * */
    public void unSubscribe(String clientId,String topicFilter){
        List<Topic<String>> topics = findMatchTopic(topicFilter);
        for (Topic<String> topic:topics){
            topic.removeListener(clientId);
        }
    }



    /**
     * 查找匹配的 topic
     */
    public List<Topic<String>> findMatchTopic(String topicFilter) {
        String[] tokens=parserToken(topicFilter);
        //根据tokens,来在树里进行遍历和查找
        



        List<Topic<String>> topics = null;
        return topics;
    }


    private String[] parserToken(String topicFilter) {
        String[] tokens = StringUtils.split(topicFilter, "/");
        return tokens;
    }
}
