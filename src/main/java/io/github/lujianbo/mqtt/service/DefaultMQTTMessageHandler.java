package io.github.lujianbo.mqtt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.lujianbo.mqtt.domain.*;
import io.github.lujianbo.mqtt.manager.MQTTContext;
import io.github.lujianbo.util.ObjectMapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultMQTTMessageHandler implements MQTTMessageHandler{

    private Logger logger = LoggerFactory.getLogger(DefaultMQTTMessageHandler.class);

    private MQTTConnection session;

    private MQTTContext context;

    public DefaultMQTTMessageHandler(MQTTConnection session) {
        this.session = session;
    }


    public void onRead(MQTTProtocol msg) {

        //处理心跳程序
        if (msg instanceof PingreqProtocol) {
            session.write(new PingrespProtocol());
            return;
        }

        //处理connect
        if (msg instanceof ConnectProtocol) {
            ConnackProtocol connackMessage = handleConnectMessage((ConnectProtocol) msg);
            session.write(connackMessage);

            /**
             If the Client supplies a zero-byte ClientId with CleanSession set to 0, the Server MUST respond to the CONNECT Packet with a CONNACK return code 0x02 (Identifier rejected) and then close the Network Connection [MQTT-3.1.3-8].

             If the Server rejects the ClientId it MUST respond to the CONNECT Packet with a CONNACK return code 0x02 (Identifier rejected) and then close the Network Connection [MQTT-3.1.3-9].
             */
            if (connackMessage.getReturnCode() == ConnackProtocol.IDENTIFIER_REJECTED) {
                session.close();
            }

            return;
        }
        //处理subscribe
        if (msg instanceof SubscribeProtocol) {
            session.write(handleSubscribeMessage((SubscribeProtocol) msg));
            return;
        }
        //处理unSubscribe
        if (msg instanceof UnsubscribeProtocol) {
            session.write(handleUnsubscribeMessage((UnsubscribeProtocol) msg));
            return;
        }


        //处理 publish
        if (msg instanceof PublishProtocol) {
            PublishProtocol message = (PublishProtocol) msg;
            if (message.getQosLevel() == PublishProtocol.reserved) {
                session.write(new DisconnectProtocol());
                return;
            }
            if (message.getQosLevel() == PublishProtocol.mostOnce) {
                handlePublishQS0Message(message);
                return;
            }

            if (message.getQosLevel() == PublishProtocol.leastOnce) {
                session.write(handlePublishQS1Message(message));
                return;
            }

            if (message.getQosLevel() == PublishProtocol.exactlyOnce) {
                session.write(handlePublishQS2Message(message));
                return;
            }
        }


        if (msg instanceof PubackProtocol) {
            //doNothing
            return;
        }

        if (msg instanceof PubrecProtocol) {
            PubrecProtocol message = (PubrecProtocol) msg;

            //doSomeThing

            PubrelProtocol pubrelMessage = new PubrelProtocol();
            pubrelMessage.setPacketIdentifier(message.getPacketIdentifier());
            session.write(pubrelMessage);
            return;
        }

        if (msg instanceof PubrelProtocol) {
            session.write(handlePubrelMessage((PubrelProtocol) msg));
            return;
        }

        if (msg instanceof PubcompProtocol) {
            //doNothing
        }

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
