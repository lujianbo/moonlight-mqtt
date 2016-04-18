package io.github.lujianbo.mqtt.manager.impl;

import io.github.lujianbo.mqtt.common.MQTTTopic;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

/**
 * Topic的默认实现，使用内存来管理可以订阅的频道，和管理频道上的订阅者
 * 默认使用树来实现
 * */
public class DefaultMQTTTopicManager {

    private final TreeNode root;

    public DefaultMQTTTopicManager() {
        root = new TreeNode();
    }

    /**
     * 订阅
     * */
    public void subscribe(String clientId,String topicFilter){
        findMatchTopic(topicFilter).addListener(clientId);
    }

    /**
     * 反订阅
     * */
    public void unSubscribe(String clientId,String topicFilter){
        findMatchTopic(topicFilter).removeListener(clientId);
    }


    /**
     * 查找匹配的 topic
     */
    public MQTTTopic findMatchTopic(String topicFilter) {
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

    public TreeNode find(String nodePath){
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


}
