package io.github.lujianbo.mqtt.common.protocol;

/**
 * Created by jianbo on 2016/3/24.
 */
public class PubcompProtocol extends MQTTProtocol {

    protected int packetIdentifier;

    public int getPacketIdentifier() {
        return packetIdentifier;
    }

    public void setPacketIdentifier(int packetIdentifier) {
        this.packetIdentifier = packetIdentifier;
    }
}
