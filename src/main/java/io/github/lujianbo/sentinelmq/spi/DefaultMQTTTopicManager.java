package io.github.lujianbo.sentinelmq.spi;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Topic的默认实现，使用内存来管理可以订阅的频道，和管理频道上的订阅者
 * 默认使用树来实现
 */
public class DefaultMQTTTopicManager {

    /**
     * 根节点名字为空
     */
    private final TopicNode root;

    /**
     * 记录clientId已经订阅的Topic
     */
    private Multimap<String, TopicNode> maps = ArrayListMultimap.create();


    public DefaultMQTTTopicManager(String topicName) {
        this.root = new TopicNode(null, topicName);
    }

    //创建topic
    public void createTopic(String topicFilter) {
        TopicNode current = root;
        String[] tokens = StringUtils.split(topicFilter, '/');
        for (int i = 0; i < tokens.length; i++) {
            current = current.createChildIfNotExit(tokens[i]);
        }
    }

    /**
     * 为clientId 添加 topicFilter的订阅
     * @return int 返回订阅的topic个数
     * @param clientId 需要订阅的clientId
     * @param topicFilter 需要订阅的topic
     */
    public int subscribe(String clientId, String topicFilter) {
        final int[] count = {0};
        findMatchTopic(topicFilter, topicNode -> {
            recordSubscribe(clientId, topicNode);
            count[0]++;
        });
        return count[0];
    }

    /**
     * 取消订阅
     */
    public int unSubscribe(String clientId, String topicFilter) {
        final int[] count = {0};
        findMatchTopic(topicFilter, topicNode ->{
            removeSubscribe(clientId, topicNode);
            count[0]++;
        });
        return count[0];
    }

    /**
     * 记录订阅信息
     */
    private void recordSubscribe(String clientId, TopicNode mqttTopic) {
        mqttTopic.addListener(clientId);
        maps.put(clientId, mqttTopic);
    }

    /**
     * 移除订阅信息
     */
    private void removeSubscribe(String clientId, TopicNode mqttTopic) {
        mqttTopic.removeListener(clientId);
        maps.remove(clientId, mqttTopic);
    }

    /**
     * 返回某个topic上的订阅者
     */
    public Iterator<String> findSubscriber(String topicFilter) {
        TopicNode topic = findOne(topicFilter);
        if (topic == null) {
            return null;
        } else {
            return topic.listeners.iterator();
        }
    }

    /**
     * 移除监听
     */
    public void clear(String clientId) {
        maps.removeAll(clientId).forEach(topic -> topic.removeListener(clientId));
    }


    /**
     * 查找正则匹配的 topic,并且进行action操作
     */
    private void findMatchTopic(String topicFilter, Consumer<TopicNode> action) {
        String[] tokens = StringUtils.split(topicFilter, '/');
        int count = 0;
        findMatch(root, tokens, 0, action);
    }


    private void findMatch(TopicNode parent, String[] tokens, int i, Consumer<TopicNode> action) {
        //到token的结尾则进行处理
        if (i == tokens.length) {
            action.accept(parent);
            return;
        }
        String token = tokens[i];
        if ("#".equals(token)) {
            //所有的子结点都匹配
            action.accept(parent);
            parent.getChildren().values().forEach(topicNode -> findMatch(topicNode, tokens, i, action));
        } else if ("+".equals(token)) {
            parent.getChildren().values().forEach(topicNode -> findMatch(topicNode, tokens, i + 1, action));
        } else {
            findMatch(parent.getChildren().get(token), tokens, i + 1, action);
        }
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

        public String getName() {
            return name;
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

        /**
         * 返回该结点的全称名字
         */
        public String getAbsoluteName() {
            StringBuilder sb = new StringBuilder(this.getName());
            //从该结点进行递归到父节点在进行输出
            TopicNode myParent = this.parent;
            while (myParent != null) {
                sb.insert(0, "/");
                sb.insert(0, myParent.getName());
                myParent = myParent.parent;
            }
            return sb.toString();
        }
    }

    /**
     * 查找指定路径的TopicNode
     */
    private TopicNode findOne(String nodePath) {
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
