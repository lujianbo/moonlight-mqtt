package io.github.lujianbo.mqtt.driver;


public interface MQTTEngine {

    /**
     * 用户上线
     * */
    public void connect(String clientId);

    /**
     * 用户离线
     * */
    public void disconnect(String clientId);

    /**
     * 推送信息
     * */
    public void publish(String topicName,int packetIdentifier,byte[] playLoad);

    /**
     * 订阅信息
     * */
    public void subscribe(String topicName,String clientId);

    /**
     * 取消订阅
     * */
    public void unSubscribe(String topicName,String clientId);
}
