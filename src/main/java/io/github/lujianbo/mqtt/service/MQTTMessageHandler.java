package io.github.lujianbo.mqtt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.lujianbo.mqtt.domain.*;
import io.github.lujianbo.util.ObjectMapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lujianbo on 2016/4/12.
 */
public class MQTTMessageHandler {

    private Logger logger = LoggerFactory.getLogger(MQTTMessageHandler.class);

    private  MQTTSession session;

    public MQTTMessageHandler(MQTTSession session){
        this.session = session;
    }


    public void onRead(MQTTMessage msg){

        //处理心跳程序
        if (msg instanceof PingreqMessage) {
            session.write(new PingrespMessage());
            return;
        }

        //处理connect
        if (msg instanceof ConnectMessage) {
            ConnackMessage connackMessage = handleConnectMessage((ConnectMessage) msg);
            session.write(connackMessage);

            /**
             If the Client supplies a zero-byte ClientId with CleanSession set to 0, the Server MUST respond to the CONNECT Packet with a CONNACK return code 0x02 (Identifier rejected) and then close the Network Connection [MQTT-3.1.3-8].

             If the Server rejects the ClientId it MUST respond to the CONNECT Packet with a CONNACK return code 0x02 (Identifier rejected) and then close the Network Connection [MQTT-3.1.3-9].
             */
            if (connackMessage.getReturnCode() == ConnackMessage.IDENTIFIER_REJECTED) {
                session.close();
            }

            return;
        }
        //处理subscribe
        if (msg instanceof SubscribeMessage) {
            session.write(handleSubscribeMessage((SubscribeMessage) msg));
            return;
        }
        //处理unSubscribe
        if (msg instanceof UnsubscribeMessage) {
            session.write(handleUnsubscribeMessage((UnsubscribeMessage) msg));
            return;
        }


        //处理 publish
        if (msg instanceof PublishMessage) {
            PublishMessage message = (PublishMessage) msg;
            if (message.getQosLevel() == PublishMessage.reserved) {
                session.write(new DisconnectMessage());
                return;
            }
            if (message.getQosLevel() == PublishMessage.mostOnce) {
                handlePublishQS0Message(message);
                return;
            }

            if (message.getQosLevel() == PublishMessage.leastOnce) {
                session.write(handlePublishQS1Message(message));
                return;
            }

            if (message.getQosLevel() == PublishMessage.exactlyOnce) {
                session.write(handlePublishQS2Message(message));
                return;
            }
        }


        if (msg instanceof PubackMessage){
            //doNothing
            return;
        }

        if (msg instanceof PubrecMessage){
            PubrecMessage message=(PubrecMessage)msg;

            //doSomeThing

            PubrelMessage pubrelMessage=new PubrelMessage();
            pubrelMessage.setPacketIdentifier(message.getPacketIdentifier());
            session.write(pubrelMessage);
            return;
        }

        if (msg instanceof PubrelMessage) {
            session.write(handlePubrelMessage((PubrelMessage) msg));
            return;
        }

        if (msg instanceof PubcompMessage){
            //doNothing
        }
        
    }

    private ConnackMessage handleConnectMessage(ConnectMessage message) {

        try {
            logger.info(ObjectMapperUtil.objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        ConnackMessage connackMessage = new ConnackMessage();

        if ((null == message.getClientId() || message.getClientId().equals("")) && (!message.isCleanSession())) {
            connackMessage.setReturnCode(ConnackMessage.IDENTIFIER_REJECTED);
            return connackMessage;
        }

        //默认测试
        connackMessage.setReturnCode(ConnackMessage.CONNECTION_ACCEPTED);
        connackMessage.setSessionPresentFlag(false);

        return connackMessage;
    }

    private void handlePublishQS0Message(PublishMessage message) {
        try {
            logger.info(ObjectMapperUtil.objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private PubackMessage handlePublishQS1Message(PublishMessage message) {
        try {
            logger.info(ObjectMapperUtil.objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        PubackMessage pubackMessage = new PubackMessage();
        pubackMessage.setPacketIdentifier(message.getPacketIdentifier());
        return pubackMessage;
    }

    private PubrecMessage handlePublishQS2Message(PublishMessage message) {
        try {
            logger.info(ObjectMapperUtil.objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        PubrecMessage pubrecMessage = new PubrecMessage();
        pubrecMessage.setPacketIdentifier(message.getPacketIdentifier());
        return pubrecMessage;
    }

    private PubcompMessage handlePubrelMessage(PubrelMessage message) {
        try {
            logger.info(ObjectMapperUtil.objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        PubcompMessage pubcompMessage = new PubcompMessage();
        pubcompMessage.setPacketIdentifier(message.getPacketIdentifier());
        return pubcompMessage;
    }

    private SubackMessage handleSubscribeMessage(SubscribeMessage message) {
        try {
            logger.info(ObjectMapperUtil.objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        SubackMessage subackMessage = new SubackMessage();
        subackMessage.setPacketIdentifier(message.getPacketIdentifier());
        for (SubscribeMessage.TopicFilterQoSPair pair : message.getPairs()) {
            if (pair.getQos() == MQTTMessage.mostOnce) {
                subackMessage.addReturnCode(SubackMessage.SuccessMaximumQoS0);
            }
            if (pair.getQos() == MQTTMessage.leastOnce) {
                subackMessage.addReturnCode(SubackMessage.SuccessMaximumQoS1);
            }
            if (pair.getQos() == MQTTMessage.exactlyOnce) {
                subackMessage.addReturnCode(SubackMessage.SuccessMaximumQoS2);
            }
        }
        return subackMessage;
    }


    private UnsubackMessage handleUnsubscribeMessage(UnsubscribeMessage message) {
        try {
            logger.info(ObjectMapperUtil.objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        UnsubackMessage unsubackMessage = new UnsubackMessage();
        unsubackMessage.setPacketIdentifier(message.getPacketIdentifier());
        return unsubackMessage;
    }

}
