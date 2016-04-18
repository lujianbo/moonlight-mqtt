package io.github.lujianbo.mqtt.manager;

/**
 * MQTT 相关的上下文
 */
public class MQTTContext {

    /**
     * 会话管理
     * */
    private MQTTSessionManager sessionManager;

    /**
     * 信息管理
     * */
    private  MQTTMessageManager messageManager;

    /**
     * 频道订阅管理
     * */
    private MQTTTopicManager topicManager;



}
