package io.github.lujianbo.mqtt.domain;

/**
 * Created by jianbo on 2016/3/24.
 */
public class PubrelMessage extends MQTTMessage {

    protected int packetIdentifier;

    public int getPacketIdentifier() {
        return packetIdentifier;
    }

    public void setPacketIdentifier(int packetIdentifier) {
        this.packetIdentifier = packetIdentifier;
    }

}
