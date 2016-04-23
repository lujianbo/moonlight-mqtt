package io.github.lujianbo.sentinel.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.HashBiMap;
import io.github.lujianbo.engine.core.MQTTEngine;
import io.github.lujianbo.sentinel.protocol.*;
import io.github.lujianbo.util.ObjectMapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MQTTProtocolHandler  {

    private Logger logger = LoggerFactory.getLogger(MQTTProtocolHandler.class);

    /**
     * 描述了clientId 和 MQTTConnection的关系
     * */
    private HashBiMap<String,MQTTConnection> maps= HashBiMap.create();

    private MQTTEngine engine;


    public MQTTProtocolHandler(MQTTEngine engine){
        this.engine=engine;
        engine.addListener(message -> {
            /**
             * 构建协议
             * */
            PublishProtocol publishProtocol=new PublishProtocol();
            /**
             * 循环发送
             * */
            for (String clientId:message.getClientIds()){
                if (maps.get(clientId)!=null){
                    maps.get(clientId).write(publishProtocol);
                }
            }
        });
    }


    public void onRead(MQTTConnection connection, ConnectProtocol message) {

        /**
         * 进行登录验证后就可以了
         * */
        maps.put(message.getClientId(),connection);

        connection.write(handleConnectMessage(message));

    }

    
    public void onRead(MQTTConnection connection, ConnackProtocol message) {
        //不会出现该数据包
    }

    
    public void onRead(MQTTConnection connection, DisconnectProtocol message) {
        engine.disconnect(maps.inverse().get(connection));
    }

    
    public void onRead(MQTTConnection connection, PingreqProtocol message) {
        connection.write(new PingrespProtocol());
    }

    
    public void onRead(MQTTConnection connection, PingrespProtocol message) {
        connection.close();
    }

    


    
    public void onRead(MQTTConnection connection, PublishProtocol message) {
        switch (message.getQosLevel()){
            case MQTTProtocol.mostOnce:
                handlePublishQS0Message(message);
                break;
            case MQTTProtocol.leastOnce:
                connection.write(handlePublishQS1Message(message));
            case MQTTProtocol.exactlyOnce:
                connection.write(handlePublishQS2Message(message));
        }
    }

    
    public void onRead(MQTTConnection connection, PubackProtocol message) {

    }

    public void onRead(MQTTConnection connection, PubcompProtocol message) {

    }

    public void onRead(MQTTConnection connection, PubrelProtocol message) {

    }

    public void onRead(MQTTConnection connection, PubrecProtocol message) {

    }

    public void onRead(MQTTConnection connection, SubackProtocol message) {
        connection.close();
    }

    
    public void onRead(MQTTConnection connection, SubscribeProtocol message) {
        debug(message);
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
        connection.write(subackMessage);
    }

    
    public void onRead(MQTTConnection connection, UnsubscribeProtocol message) {
        debug(message);
        UnsubackProtocol unsubackMessage = new UnsubackProtocol();
        unsubackMessage.setPacketIdentifier(message.getPacketIdentifier());
        connection.write(unsubackMessage);
    }

    
    public void onRead(MQTTConnection connection, UnsubackProtocol message) {
        connection.close();
    }

    
    public void onClose(MQTTConnection connection) {

    }

    
    public void onException(MQTTConnection connection) {

    }


    /**
     * 处理连接
     * */
    private ConnackProtocol handleConnectMessage(ConnectProtocol message) {

        ConnackProtocol connackProtocol = new ConnackProtocol();

        //默认测试
        connackProtocol.setReturnCode(ConnackProtocol.CONNECTION_ACCEPTED);
        connackProtocol.setSessionPresentFlag(false);

        return connackProtocol;
    }

    /**
     * 处理publish
     * */
    private void handlePublishQS0Message(PublishProtocol message) {
        debug(message);
    }

    /**
     * 处理qs1的publish
     * */
    private PubackProtocol handlePublishQS1Message(PublishProtocol message) {
        debug(message);
        PubackProtocol pubackMessage = new PubackProtocol();
        pubackMessage.setPacketIdentifier(message.getPacketIdentifier());
        return pubackMessage;
    }

    /**
     * 处理qs2的publish
     * */
    private PubrecProtocol handlePublishQS2Message(PublishProtocol message) {
        debug(message);
        PubrecProtocol pubrecMessage = new PubrecProtocol();
        pubrecMessage.setPacketIdentifier(message.getPacketIdentifier());
        return pubrecMessage;
    }

    /**
     * qs2相关的处理
     * */
    private PubcompProtocol handlePubrelMessage(PubrelProtocol message) {
        debug(message);

        PubcompProtocol pubcompMessage = new PubcompProtocol();
        pubcompMessage.setPacketIdentifier(message.getPacketIdentifier());
        return pubcompMessage;
    }

    private void debug(Object message){
        try {
            logger.debug(ObjectMapperUtil.objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
