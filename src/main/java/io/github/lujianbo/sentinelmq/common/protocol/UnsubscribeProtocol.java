package io.github.lujianbo.sentinelmq.common.protocol;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jianbo on 2016/3/24.
 */
public class UnsubscribeProtocol extends MQTTProtocol {

    protected int packetIdentifier;

    protected List<String> topicNames = new LinkedList<>();

    public int getPacketIdentifier() {
        return packetIdentifier;
    }

    public void setPacketIdentifier(int packetIdentifier) {
        this.packetIdentifier = packetIdentifier;
    }

    public List<String> getTopicNames() {
        return topicNames;
    }

    public void setTopicNames(List<String> topicNames) {
        this.topicNames = topicNames;
    }

    public void addTopicName(String topicName) {
        topicNames.add(topicName);
    }
}
