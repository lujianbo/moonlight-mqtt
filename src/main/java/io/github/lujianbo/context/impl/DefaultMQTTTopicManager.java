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
 */
public class DefaultMQTTTopicManager implements TopicManager {

    /**
     * 根节点名字为空
     */
    private final TopicNode root = new TopicNode(null, "");

    /**
     * 记录clientId已经订阅的Topic
     */
    private Multimap<String, TopicNode> maps = ArrayListMultimap.create();


    public DefaultMQTTTopicManager() {

    }

    /**
     * 添加订阅
     */
    public boolean subscribe(String clientId, String topicFilter) {
        findMatchTopic(topicFilter).forEachRemaining(mqttTopic -> {
            recordSubscribe(clientId, mqttTopic);
        });
        return true;
    }

    /**
     * 取消订阅
     */
    public boolean unSubscribe(String clientId, String topicFilter) {
        findMatchTopic(topicFilter).forEachRemaining(mqttTopic -> {
            removeSubscribe(clientId, mqttTopic);
        });

        return true;
    }

    /**
     * 记录订阅信息
     */
    private void recordSubscribe(String clientId, TopicNode mqttTopic) {
        mqttTopic.addListener(clientId);
        maps.put(clientId, mqttTopic);
    }

    private void removeSubscribe(String clientId, TopicNode mqttTopic) {
        mqttTopic.removeListener(clientId);
        maps.remove(clientId, mqttTopic);
    }


    public Iterator<String> findSubscriber(String topicFilter) {
        TopicNode topic = find(topicFilter);
        if (topic == null) {
            return null;
        } else {
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
    private Iterator<TopicNode> findMatchTopic(String topicFilter) {

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

        String[] tokens = StringUtil.split(topicFilter, '/');
        int length = tokens.length;
        int last = length - 1;
        /**
         * 合法性检验
         * */
        for (int i = 0; i < length; i++) {
            //# 不在最后一个位置
            String token = tokens[i];
            if (token.contains("#")) {
                //# 和其他字符混在一起
                if (token.length() > 1) {
                    break;
                }
                //# 不在最后一个位置
                if (i != last) {
                    break;
                }

            }
            // + 在最后位置
            if (tokens[i].contains("+")) {

                // + 和其他符号混用
                if (token.length() > 1) {
                    break;
                }
                // + 在最后一个位置
                if (i == last) {
                    break;
                }
            }

        }
        return new Iterator<TopicNode>() {

            private int max = 1;

            @Override
            public boolean hasNext() {
                return max == 0;
            }

            @Override
            public TopicNode next() {
                max--;
                return find(topicFilter);
            }
        };
    }

    /**
     * 树结点
     */
    public class TopicNode {
        private TopicNode parent = null;

        private final String name;

        private final HashSet<String> listeners = new HashSet<>();


        private ConcurrentHashMap<String, TopicNode> children = new ConcurrentHashMap<>();

        private TopicNode(TopicNode parent, String name) {
            this.parent = parent;
            this.name = name;
        }

        public TopicNode(String name) {
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

        /**
         * 当子节点不存在的时候就创造
         * 返回一个必然存在的子节点
         */
        public TopicNode createChildIfNotExit(String name) {
            TopicNode child = children.get(name);
            if (child == null) {
                child = new TopicNode(this, name);
                TopicNode returnNode = children.putIfAbsent(name, child);
                if (returnNode != null) {
                    child = returnNode;
                }
            }
            return child;
        }

        public TopicNode getParent() {
            return parent;
        }

        public void setParent(TopicNode parent) {
            this.parent = parent;
        }

        public Map<String, TopicNode> getChildren() {
            return children;
        }


        /**
         * 对于任意child进行操作
         */
        public void foreachChild(BiConsumer<String, TopicNode> consumer) {
            children.forEach(consumer);
        }
    }

    private TopicNode find(String nodePath) {
        TopicNode node = root;
        String[] names = nodePath.split("/");
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            node = node.getChildren().get(name);
            if (node == null) {
                break;//找不到节点
            }
        }
        return node;
    }
}
