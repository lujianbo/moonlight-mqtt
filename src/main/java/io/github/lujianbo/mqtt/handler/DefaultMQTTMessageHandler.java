package io.github.lujianbo.mqtt.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.lujianbo.mqtt.common.MQTTSession;
import io.github.lujianbo.mqtt.protocol.*;
import io.github.lujianbo.mqtt.manager.MQTTContext;
import io.github.lujianbo.util.ObjectMapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultMQTTMessageHandler implements MQTTMessageHandler{

    private Logger logger = LoggerFactory.getLogger(DefaultMQTTMessageHandler.class);

    private MQTTConnection connection;

    private MQTTContext context;

    private MQTTSession session;//仅当登陆后才会获得该session

    public DefaultMQTTMessageHandler(MQTTContext context,MQTTConnection connection) {
        this.context=context;
        this.connection = connection;
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

    @Override
    public void onRead(ConnectProtocol message) {
        ConnackProtocol connackMessage = handleConnectMessage(message);
        connection.write(connackMessage);
        /**
         If the Client supplies a zero-byte ClientId with CleanSession set to 0, the Server MUST respond to the CONNECT Packet with a CONNACK return code 0x02 (Identifier rejected) and then close the Network Connection [MQTT-3.1.3-8].

         If the Server rejects the ClientId it MUST respond to the CONNECT Packet with a CONNACK return code 0x02 (Identifier rejected) and then close the Network Connection [MQTT-3.1.3-9].
         */
        if (connackMessage.getReturnCode() == ConnackProtocol.IDENTIFIER_REJECTED) {
            connection.close();
        }
    }



    @Override
    public void onRead(DisconnectProtocol message) {

    }

    @Override
    public void onRead(PingreqProtocol message) {
        //处理ping
        connection.write(new PingrespProtocol());
    }



    @Override
    public void onRead(PubcompProtocol message) {

    }

    @Override
    public void onRead(PublishProtocol message) {
        if (message.getQosLevel() == PublishProtocol.reserved) {
            connection.write(new DisconnectProtocol());
            return;
        }
        if (message.getQosLevel() == PublishProtocol.mostOnce) {
            handlePublishQS0Message(message);
            return;
        }

        if (message.getQosLevel() == PublishProtocol.leastOnce) {
            connection.write(handlePublishQS1Message(message));
            return;
        }

        if (message.getQosLevel() == PublishProtocol.exactlyOnce) {
            connection.write(handlePublishQS2Message(message));
            return;
        }
    }



    @Override
    public void onRead(PubrelProtocol message) {
        connection.write(handlePubrelMessage(message));
    }

    @Override
    public void onRead(PubrecProtocol message) {
        //doSomeThing

        PubrelProtocol pubrelMessage = new PubrelProtocol();
        pubrelMessage.setPacketIdentifier(message.getPacketIdentifier());
        connection.write(pubrelMessage);
    }

    @Override
    public void onRead(SubscribeProtocol message) {
        connection.write(handleSubscribeMessage(message));
    }

    @Override
    public void onRead(UnsubscribeProtocol message) {
        connection.write(handleUnsubscribeMessage(message));
    }

    @Override
    public void onRead(PingrespProtocol message) {
        //不应当接收到这个数据
    }

    @Override
    public void onRead(ConnackProtocol message) {
        //不应当接收到这个数据
    }

    @Override
    public void onRead(SubackProtocol message) {

    }

    @Override
    public void onRead(UnsubackProtocol message) {

    }

    @Override
    public void onRead(PubackProtocol message) {

    }
}
