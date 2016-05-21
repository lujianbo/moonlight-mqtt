package io.github.lujianbo.sentinelmq.common.protocol;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jianbo on 2016/3/24.
 */
public class SubscribeProtocol extends MQTTProtocol {

    private int packetIdentifier;
    private List<TopicFilterQoSPair> pairs = new LinkedList<>();

    public int getPacketIdentifier() {
        return packetIdentifier;
    }

    public void setPacketIdentifier(int packetIdentifier) {
        this.packetIdentifier = packetIdentifier;
    }

    public List<TopicFilterQoSPair> getPairs() {
        return pairs;
    }

    public void setPairs(List<TopicFilterQoSPair> pairs) {
        this.pairs = pairs;
    }

    public void addTopicFilterQoSPair(String topicName, byte qos) {
        this.pairs.add(new TopicFilterQoSPair(topicName, qos));
    }

    public class TopicFilterQoSPair {
        private String topicFilter;
        private byte qos;

        public TopicFilterQoSPair(String topicFilter, byte qos) {
            this.topicFilter = topicFilter;
            this.qos = qos;
        }

        public String getTopicFilter() {
            return topicFilter;
        }

        public void setTopicFilter(String topicFilter) {
            this.topicFilter = topicFilter;
        }

        public byte getQos() {
            return qos;
        }

        public void setQos(byte qos) {
            this.qos = qos;
        }
    }
}
