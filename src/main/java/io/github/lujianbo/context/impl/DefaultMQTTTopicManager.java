package io.github.lujianbo.context.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import io.github.lujianbo.context.manager.TopicManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

/**
 * Topic的默认实现，使用内存来管理可以订阅的频道，和管理频道上的订阅者
 * 默认使用树来实现
 * */
public class DefaultMQTTTopicManager implements TopicManager {

    private final TreeNode root;

    private Multimap<String,String> maps= ArrayListMultimap.create();

    public DefaultMQTTTopicManager() {
        root = new TreeNode();
    }

    /**
     * 添加订阅
     * */
    public boolean subscribe(String clientId,String topicFilter){
        findMatchTopic(topicFilter).forEachRemaining(mqttTopic -> {
            mqttTopic.addListener(clientId);
            maps.put(clientId,mqttTopic.name);
        });
        return true;
    }

    /**
     * 取消订阅
     * */
    public boolean unSubscribe(String clientId,String topicFilter){
        findMatchTopic(topicFilter).forEachRemaining(mqttTopic -> {
            mqttTopic.removeListener(clientId);
            maps.remove(clientId,mqttTopic.name);
        });

        return true;
    }


    public Iterator<String> findSubscriber(String topicFilter) {
        MQTTTopic topic=findTopic(topicFilter);
        if (topic==null){
            return null;
        }else {
            return topic.listeners.iterator();
        }
    }


    @Override
    public void clear(String clientId) {
        /**
         * 移除监听
         * */
        maps.removeAll(clientId).forEach(topicName -> findTopic(topicName).removeListener(clientId));
    }


    /**
     * 查找正则匹配的 topic
     */
    private Iterator<MQTTTopic> findMatchTopic(String topicFilter) {
        return new Iterator<MQTTTopic>() {

            private int max=1;

            @Override
            public boolean hasNext() {
                return max==0;
            }

            @Override
            public MQTTTopic next() {
                max--;
                return find(topicFilter).data;
            }
        };
    }

    /**
     * 查找指定的Topic
     * */
    private MQTTTopic findTopic(String topicName){
        TreeNode node=find(topicName);
        if (node==null){
            return null;
        }else {
            return node.data;
        }
    }


    /**
     * 树结点
     */
    public static class TreeNode {
        private TreeNode parent = null;
        private MQTTTopic data;
        private Map<String, TreeNode> children = new ConcurrentHashMap<>();

        public TreeNode(TreeNode parent, MQTTTopic data) {
            this.parent = parent;
            this.data = data;
        }

        public TreeNode() {

        }

        public TreeNode getParent() {
            return parent;
        }

        public void setParent(TreeNode parent) {
            this.parent = parent;
        }

        public Map<String, TreeNode> getChildren() {
            return children;
        }

        public MQTTTopic getData() {
            return data;
        }

        public void setData(MQTTTopic data) {
            this.data = data;
        }

        /**
         * 对于任意child进行操作
         */
        public void foreachChild(BiConsumer<String,TreeNode> consumer) {
            children.forEach(consumer);
        }
    }

    private TreeNode find(String nodePath){
        TreeNode node=root;
        String[] names=nodePath.split("/");
        for (int i=0;i<names.length;i++){
            String name=names[i];
            node=(TreeNode) node.getChildren().get(name);
            if (node==null){
                break;//找不到节点
            }
        }
        return node;
    }


    class MQTTTopic {

        private final String name;

        private final HashSet<String> listeners = new HashSet<>();

        public MQTTTopic(String name) {
            this.name = name;
        }

        public void addListener(String listener) {
            this.listeners.add(listener);
        }

        public void removeListener(String listener) {
            this.listeners.remove(listener);
        }

        public HashSet<String> getListeners() {
            return listeners;
        }
    }


}
