package io.github.lujianbo.mqtt.protocol;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jianbo on 2016/3/24.
 */
public class SubackProtocol extends MQTTProtocol {

    public static byte SuccessMaximumQoS0 = 0x00;
    public static byte SuccessMaximumQoS1 = 0x01;
    public static byte SuccessMaximumQoS2 = 0x02;
    public static byte Failure = 0x08;

    protected int packetIdentifier;

    private List<Byte> returnCodes = new LinkedList<>();

    public int getPacketIdentifier() {
        return packetIdentifier;
    }

    public void setPacketIdentifier(int packetIdentifier) {
        this.packetIdentifier = packetIdentifier;
    }

    public List<Byte> getReturnCodes() {
        return returnCodes;
    }

    public void setReturnCodes(List<Byte> returnCodes) {
        this.returnCodes = returnCodes;
    }

    public void addReturnCode(byte b) {
        this.returnCodes.add(b);
    }
}
