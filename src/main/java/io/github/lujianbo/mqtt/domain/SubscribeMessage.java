package io.github.lujianbo.mqtt.domain;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jianbo on 2016/3/24.
 */
public class SubscribeMessage extends MQTTMessage {

    private int packetIdentifier;
    private List<TopicFilterQoSPair> pairs=new LinkedList<>();

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

    public void addTopicFilterQoSPair(String topicName, byte qos){
        this.pairs.add(new TopicFilterQoSPair(topicName, qos));
    }

    public class TopicFilterQoSPair{
        private String topicName;
        private byte qos;

        public TopicFilterQoSPair(String topicName, byte qos) {
            this.topicName = topicName;
            this.qos = qos;
        }

        public String getTopicName() {
            return topicName;
        }

        public void setTopicName(String topicName) {
            this.topicName = topicName;
        }

        public byte getQos() {
            return qos;
        }

        public void setQos(byte qos) {
            this.qos = qos;
        }
    }
}
