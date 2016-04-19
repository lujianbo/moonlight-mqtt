package io.github.lujianbo.mqtt.gateway.common.protocol;

/**
 * Created by jianbo on 2016/3/24.
 */
public class PubrecProtocol extends MQTTProtocol {

    protected int packetIdentifier;

    public int getPacketIdentifier() {
        return packetIdentifier;
    }

    public void setPacketIdentifier(int packetIdentifier) {
        this.packetIdentifier = packetIdentifier;
    }
}
