package io.github.lujianbo.context.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import io.github.lujianbo.context.manager.TopicManager;

import java.util.HashSet;
import java.util.Map;
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
        findMatchTopic(topicFilter).addListener(clientId);
        maps.put(clientId,topicFilter);
        return true;
    }


    public boolean unSubscribe(String clientId,String topicFilter){
        findMatchTopic(topicFilter).removeListener(clientId);
        maps.remove(clientId,topicFilter);
        return true;
    }

    public Iterable<String> findSubscriber(String topicFilter) {
        return findMatchTopic(topicFilter).listeners;
    }

    @Override
    public void clear(String clientId) {
        /**
         * 移除监听
         * */
        maps.removeAll(clientId).forEach(topicName ->{
            findMatchTopic(topicName).removeListener(clientId);
        } );
    }


    /**
     * 查找匹配的 topic
     */
    private MQTTTopic findMatchTopic(String topicFilter) {
        return find(topicFilter).data;
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
