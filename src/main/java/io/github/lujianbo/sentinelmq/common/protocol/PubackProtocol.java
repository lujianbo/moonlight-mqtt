package io.github.lujianbo.sentinelmq.common.protocol;

/**
 * Created by jianbo on 2016/3/24.
 */
public class PubackProtocol extends MQTTProtocol {

    protected int packetIdentifier;


    public int getPacketIdentifier() {
        return packetIdentifier;
    }

    public void setPacketIdentifier(int packetIdentifier) {
        this.packetIdentifier = packetIdentifier;
    }
}
