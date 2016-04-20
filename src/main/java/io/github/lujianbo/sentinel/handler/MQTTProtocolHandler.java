package io.github.lujianbo.sentinel.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.lujianbo.sentinel.protocol.*;
import io.github.lujianbo.util.ObjectMapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MQTTProtocolHandler  {

    private Logger logger = LoggerFactory.getLogger(MQTTProtocolHandler.class);

    private SentinelContext sentinelContext;

    public MQTTProtocolHandler(SentinelContext sentinelContext){
        this.sentinelContext=sentinelContext;
    }


    public void onRead(MQTTConnection connection, ConnectProtocol message) {

    }

    
    public void onRead(MQTTConnection connection, ConnackProtocol message) {
        //不会出现该数据包
    }

    
    public void onRead(MQTTConnection connection, DisconnectProtocol message) {

    }

    
    public void onRead(MQTTConnection connection, PingreqProtocol message) {

    }

    
    public void onRead(MQTTConnection connection, PingrespProtocol message) {

    }

    
    public void onRead(MQTTConnection connection, PubcompProtocol message) {

    }

    
    public void onRead(MQTTConnection connection, PublishProtocol message) {

    }

    
    public void onRead(MQTTConnection connection, PubackProtocol message) {

    }

    
    public void onRead(MQTTConnection connection, PubrelProtocol message) {

    }

    
    public void onRead(MQTTConnection connection, PubrecProtocol message) {

    }

    
    public void onRead(MQTTConnection connection, SubackProtocol message) {

    }

    
    public void onRead(MQTTConnection connection, SubscribeProtocol message) {

    }

    
    public void onRead(MQTTConnection connection, UnsubscribeProtocol message) {

    }

    
    public void onRead(MQTTConnection connection, UnsubackProtocol message) {

    }

    
    public void onClose(MQTTConnection connection) {

    }

    
    public void onException(MQTTConnection connection) {

    }


    /**
     * 处理连接
     * */
    private ConnackProtocol handleConnectMessage(ConnectProtocol message) {

        try {
            logger.info(ObjectMapperUtil.objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        ConnackProtocol connackMessage = new ConnackProtocol();

        if ((null == message.getClientId() || message.getClientId().equals("")) && (!message.isCleanSession())) {
            connackMessage.setReturnCode(ConnackProtocol.IDENTIFIER_REJECTED);
            return connackMessage;
        }

        //默认测试
        connackMessage.setReturnCode(ConnackProtocol.CONNECTION_ACCEPTED);
        connackMessage.setSessionPresentFlag(false);

        return connackMessage;
    }

    /**
     * 处理publish
     * */
    private void handlePublishQS0Message(PublishProtocol message) {
        try {
            logger.info(ObjectMapperUtil.objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理qs1的publish
     * */
    private PubackProtocol handlePublishQS1Message(PublishProtocol message) {
        try {
            logger.info(ObjectMapperUtil.objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        PubackProtocol pubackMessage = new PubackProtocol();
        pubackMessage.setPacketIdentifier(message.getPacketIdentifier());
        return pubackMessage;
    }

    /**
     * 处理qs2的publish
     * */
    private PubrecProtocol handlePublishQS2Message(PublishProtocol message) {
        try {
            logger.info(ObjectMapperUtil.objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        PubrecProtocol pubrecMessage = new PubrecProtocol();
        pubrecMessage.setPacketIdentifier(message.getPacketIdentifier());
        return pubrecMessage;
    }

    /**
     * qs2相关的处理
     * */
    private PubcompProtocol handlePubrelMessage(PubrelProtocol message) {
        try {
            logger.info(ObjectMapperUtil.objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        PubcompProtocol pubcompMessage = new PubcompProtocol();
        pubcompMessage.setPacketIdentifier(message.getPacketIdentifier());
        return pubcompMessage;
    }

    /**
     * 订阅相关的处理
     * */
    private SubackProtocol handleSubscribeMessage(SubscribeProtocol message) {
        try {
            logger.info(ObjectMapperUtil.objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        SubackProtocol subackMessage = new SubackProtocol();
        subackMessage.setPacketIdentifier(message.getPacketIdentifier());
        for (SubscribeProtocol.TopicFilterQoSPair pair : message.getPairs()) {
            if (pair.getQos() == MQTTProtocol.mostOnce) {
                subackMessage.addReturnCode(SubackProtocol.SuccessMaximumQoS0);
            }
            if (pair.getQos() == MQTTProtocol.leastOnce) {
                subackMessage.addReturnCode(SubackProtocol.SuccessMaximumQoS1);
            }
            if (pair.getQos() == MQTTProtocol.exactlyOnce) {
                subackMessage.addReturnCode(SubackProtocol.SuccessMaximumQoS2);
            }
        }
        return subackMessage;
    }

    /**
     * 取消订阅相关的处理
     * */
    private UnsubackProtocol handleUnsubscribeMessage(UnsubscribeProtocol message) {

        try {
            logger.info(ObjectMapperUtil.objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        UnsubackProtocol unsubackMessage = new UnsubackProtocol();
        unsubackMessage.setPacketIdentifier(message.getPacketIdentifier());
        return unsubackMessage;
    }




}
