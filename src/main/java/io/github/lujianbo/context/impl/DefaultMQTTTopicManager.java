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

    public boolean subscribe(String clientId,String topicFilter){
        findMatchTopic(topicFilter).forEachRemaining(mqttTopic -> mqttTopic.addListener(clientId));
        maps.put(clientId,topicFilter);
        return true;
    }


    public boolean unSubscribe(String clientId,String topicFilter){
        findMatchTopic(topicFilter).forEachRemaining(mqttTopic -> mqttTopic.removeListener(clientId));
        maps.remove(clientId,topicFilter);
        return true;
    }

    public Iterator<String> findSubscriber(String topicFilter) {
        return new ClientIterator(findMatchTopic(topicFilter));
    }


    /**
     * 迭代器的默认实现
     * */
    private static final Iterator<String> initIterator=new Iterator<String>() {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public String next() {
            return null;
        }
    };
    /**
     * 双重迭代器
     * */
    private class ClientIterator implements Iterator<String>{
        private Iterator<MQTTTopic> topicIterator;
        /**
         * 初始化迭代器使其无法为null
         * */
        private Iterator<String> current=initIterator;

        public ClientIterator(Iterator<MQTTTopic> topicIterator){
            this.topicIterator=topicIterator;
        }

        /**
         * 检查和修改当前的设置
         * */
        private void validate(){
            if (!current.hasNext()) {
                if (topicIterator.hasNext()){
                    current=topicIterator.next().listeners.iterator();
                }
            }
        }

        @Override
        public boolean hasNext() {
            validate();
            return current.hasNext();
        }

        @Override
        public String next() {
            validate();
            return current.next();
        }
    }


    @Override
    public void clear(String clientId) {
        /**
         * 移除监听
         * */
        maps.removeAll(clientId).forEach(topicName -> findMatchTopic(topicName).forEachRemaining(mqttTopic -> mqttTopic.removeListener(clientId)));
    }


    /**
     * 查找匹配的 topic
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
            children.forEach(consumer::accept);
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
