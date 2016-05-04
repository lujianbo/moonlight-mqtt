package io.github.lujianbo.context.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.github.lujianbo.context.manager.TopicManager;
import io.netty.util.internal.StringUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

/**
 * Topic的默认实现，使用内存来管理可以订阅的频道，和管理频道上的订阅者
 * 默认使用树来实现
 * */
public class DefaultMQTTTopicManager implements TopicManager {

    /**
     * 根节点名字为空
     * */
    private final TreeNode root=new TreeNode(null,new MQTTTopic(""));

    /**
     * 记录clientId已经订阅的Topic
     * */
    private Multimap<String,MQTTTopic> maps= ArrayListMultimap.create();


    public DefaultMQTTTopicManager() {

    }

    /**
     * 添加订阅
     * */
    public boolean subscribe(String clientId,String topicFilter){
        findMatchTopic(topicFilter).forEachRemaining(mqttTopic -> {
            recordSubscribe(clientId,mqttTopic);
        });
        return true;
    }

    /**
     * 取消订阅
     * */
    public boolean unSubscribe(String clientId,String topicFilter){
        findMatchTopic(topicFilter).forEachRemaining(mqttTopic -> {
            removeSubscribe(clientId,mqttTopic);
        });

        return true;
    }


    private void recordSubscribe(String clientId,MQTTTopic mqttTopic){
        mqttTopic.addListener(clientId);
        maps.put(clientId,mqttTopic);
    }

    private void removeSubscribe(String clientId,MQTTTopic mqttTopic){
        mqttTopic.removeListener(clientId);
        maps.remove(clientId,mqttTopic);
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
        maps.removeAll(clientId).forEach(topic -> topic.removeListener(clientId));
    }


    /**
     * 查找正则匹配的 topic
     */
    private Iterator<MQTTTopic> findMatchTopic(String topicFilter) {

        /**
         * # 只能出现在最后的部分
         * + 只能出现在中间部分
         *
         * # 代表订阅以该节点为根节点的所有子节点
         *
         * + 代表的是某个层级上的通配符 即任意的意思
         *
         * $ 只能在第一个位置出现,代表特殊的信息
         * */

        String[] tokens= StringUtil.split(topicFilter,'/');
        int length=tokens.length;
        int last=length-1;
        /**
         * 合法性检验
         * */
        for (int i=0;i<length;i++){
            //# 不在最后一个位置
            String token=tokens[i];
            if (token.contains("#")){
                //# 和其他字符混在一起
                if (token.length()>1){
                    break;
                }
                //# 不在最后一个位置
                if (i!=last){
                    break;
                }

            }
            // + 在最后位置
            if (tokens[i].contains("+")){

                // + 和其他符号混用
                if (token.length()>1){
                    break;
                }
                // + 在最后一个位置
                if (i==last){
                    break;
                }
            }

        }




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
    public class TreeNode {
        private TreeNode parent = null;
        private MQTTTopic data;
        private ConcurrentHashMap<String, TreeNode> children = new ConcurrentHashMap<>();

        private TreeNode(TreeNode parent, MQTTTopic data) {
            this.parent = parent;
            this.data = data;
        }

        public TreeNode() {

        }

        /**
         * 当子节点不存在的时候就创造
         * 返回一个必然存在的子节点
         * */
        public TreeNode createChildIfNotExit(String name){
            TreeNode child=children.get(name);
            if (child==null){
                child=new TreeNode(this,new MQTTTopic(name));
                TreeNode returnNode=children.putIfAbsent(name,child);
                if (returnNode!=null){
                    child=returnNode;
                }
            }
            return child;
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


    public class MQTTTopic {

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
