package io.github.lujianbo.mqtt.manager;

import java.util.List;

/**
 * Topic管理，
 * 根据规范,Topic利用 /xx/xx  的方式进行描述
 */
public interface MQTTTopicManager {

    /**
     * 查找并且返回符合topicFilter的 topic
     */
    public List<String> findMatchTopic(String topicFilter);

    /**
     * 查找指定名称的Topic
     */
    public String findTopic(String topicName);

    /**
     * 添加topic
     */
    public void addTopic(String topicName);


    /**
     * 返回当前存在的topic
     * */
    List<String> list();


    /**
     * 向目标topic添加订阅者
     * */
    public void subscribe(String clientId,String topicFilter);

    /**
     * 向目标Topic移除订阅者
     * */
    public void unSubscribe(String clientId,String topicFilter);



    /**
     * 返回某个Topic对应的clientId
     * */


}
