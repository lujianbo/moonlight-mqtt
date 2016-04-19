package io.github.lujianbo.driver.common;

import java.util.List;

/**
 * 广播信息
 */
public class BroadcastMessage {

    /**
     * 需要广播的clientId
     * */
    private List<String> clientIds;

    /**
     * 需要广播的内容
     * */
    private byte[] payload;

    public BroadcastMessage(List<String> clientIds, byte[] payload) {
        this.clientIds = clientIds;
        this.payload = payload;
    }

    public BroadcastMessage() {
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    public List<String> getClientIds() {
        return clientIds;
    }

    public void setClientIds(List<String> clientIds) {
        this.clientIds = clientIds;
    }
}
