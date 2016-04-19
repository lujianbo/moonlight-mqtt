package io.github.lujianbo.mqtt.common.protocol;

/**
 * Created by jianbo on 2016/3/24.
 */
public class PublishProtocol extends MQTTProtocol {


    //qos 级别
    protected byte qosLevel = mostOnce;

    protected boolean dupFlag = false;

    protected boolean retainFlag = false;

    protected String topicName;

    protected int packetIdentifier;

    protected byte[] payload;

    public byte getQosLevel() {
        return qosLevel;
    }

    public void setQosLevel(byte qosLevel) {
        this.qosLevel = qosLevel;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public int getPacketIdentifier() {
        return packetIdentifier;
    }

    public void setPacketIdentifier(int packetIdentifier) {
        this.packetIdentifier = packetIdentifier;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    public boolean isDupFlag() {
        return dupFlag;
    }

    public void setDupFlag(boolean dupFlag) {
        this.dupFlag = dupFlag;
    }

    public boolean isRetainFlag() {
        return retainFlag;
    }

    public void setRetainFlag(boolean retainFlag) {
        this.retainFlag = retainFlag;
    }
}
