package io.github.lujianbo.sentinel.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.HashBiMap;
import io.github.lujianbo.engine.common.PublishMessage;
import io.github.lujianbo.engine.core.MQTTEngine;
import io.github.lujianbo.sentinel.protocol.*;
import io.github.lujianbo.util.ObjectMapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 负责处理MQTT协议相关的内容,作为Mqtt的协议前置处理，其主要目的是将MQTT协议抽象到
 * 一个通用的发布订阅服务上
 */
public class MQTTProtocolHandler {

    private Logger logger = LoggerFactory.getLogger(MQTTProtocolHandler.class);

    /**
     * 描述了clientId 和 MQTTConnection的关系,
     */
    private HashBiMap<String, MQTTConnection> maps = HashBiMap.create();


    /**
     * 提供通用 发布订阅服务的引擎
     */
    private MQTTEngine engine;


    public MQTTProtocolHandler(MQTTEngine engine) {
        this.engine = engine;

        /**
         * 向引擎中注册相关的处理方式
         * */
        engine.addListener(message -> {
            /**
             * 构建协议
             * */
            PublishProtocol publishProtocol = new PublishProtocol();

            /**
             * 循环发送
             * */
            for (String clientId : message.getClientIds()) {
                MQTTConnection connection = maps.get(clientId);
                if (connection != null) {
                    connection.write(publishProtocol);
                }
            }
        });
    }


    /**
     * 处理MQTT的连接和登陆部分的代码
     */
    public void onRead(MQTTConnection connection, ConnectProtocol message) {
        /**
         * 进行登录验证后就可以了
         * */
        maps.put(message.getClientId(), connection);
        byte returnCode = ConnackProtocol.CONNECTION_ACCEPTED;

        /**
         * 构建返回数据包
         * */
        ConnackProtocol connackProtocol = new ConnackProtocol();
        connackProtocol.setReturnCode(returnCode);
        connackProtocol.setSessionPresentFlag(false);
        connection.write(connackProtocol);

    }

    /**
     * 登陆信息的返回，在服务端不会出现该数据包
     */
    public void onRead(MQTTConnection connection, ConnackProtocol message) {
        //不会出现该数据包
        connection.close();
    }


    public void onRead(MQTTConnection connection, DisconnectProtocol message) {
        engine.disconnect(maps.inverse().get(connection));
    }

    /**
     * 心跳协议的实现
     */
    public void onRead(MQTTConnection connection, PingreqProtocol message) {
        connection.write(new PingrespProtocol());
    }


    public void onRead(MQTTConnection connection, PingrespProtocol message) {
        connection.close();
    }

    public void onRead(MQTTConnection connection, PublishProtocol message) {
        debug(message);

        /**
         * 获取信息
         * */
        String clientId = maps.inverse().get(connection);

        /**
         * 根据消息类型分别处理
         * */
        switch (message.getQosLevel()) {
            case MQTTProtocol.mostOnce:
                handlePublishQS0Message(clientId, message.getTopicName(), message.getPayload());
                break;
            case MQTTProtocol.leastOnce:
                connection.write(handlePublishQS1Message(message));
                break;
            case MQTTProtocol.exactlyOnce:
                connection.write(handlePublishQS2Message(message));
                break;
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

    /**
     * 在服务器端不会接收到 SubackProtocol
     */
    public void onRead(MQTTConnection connection, SubackProtocol message) {
        connection.close();
    }

    /**
     * 订阅相关的消息服务
     */
    public void onRead(MQTTConnection connection, SubscribeProtocol message) {
        debug(message);

        //构建返回值
        SubackProtocol subackMessage = new SubackProtocol();
        subackMessage.setPacketIdentifier(message.getPacketIdentifier());
        String clientId = maps.inverse().get(connection);
        for (SubscribeProtocol.TopicFilterQoSPair pair : message.getPairs()) {
            int returnCode = engine.subscribe(clientId, pair.getTopicFilter(), pair.getQos());
            //根据returnCode 来构建返回值

            /**
             * 这里的演示是假的
             * */
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

    /**
     * 取消订阅的操作
     */
    public void onRead(MQTTConnection connection, UnsubscribeProtocol message) {
        debug(message);

        UnsubackProtocol unsubackMessage = new UnsubackProtocol();
        unsubackMessage.setPacketIdentifier(message.getPacketIdentifier());
        for (String topicName : message.getTopicNames()) {
            engine.unSubscribe(maps.inverse().get(connection), topicName);
        }
        connection.write(unsubackMessage);
    }


    public void onRead(MQTTConnection connection, UnsubackProtocol message) {
        connection.close();
    }


    public void onClose(MQTTConnection connection) {
        /**
         * 清理连接
         * */
        maps.inverse().remove(connection);
    }


    public void onException(MQTTConnection connection) {
        maps.inverse().remove(connection);
    }

    /**
     * 处理publish
     */
    private void handlePublishQS0Message(String clientId, String topicName, byte[] payLoad) {
        engine.publish(new PublishMessage(clientId, topicName, payLoad));
    }

    /**
     * 处理qs1的publish
     */
    private PubackProtocol handlePublishQS1Message(PublishProtocol message) {
        PubackProtocol pubackMessage = new PubackProtocol();
        pubackMessage.setPacketIdentifier(message.getPacketIdentifier());

        return pubackMessage;
    }

    /**
     * 处理qs2的publish
     */
    private PubrecProtocol handlePublishQS2Message(PublishProtocol message) {
        PubrecProtocol pubrecMessage = new PubrecProtocol();
        pubrecMessage.setPacketIdentifier(message.getPacketIdentifier());
        return pubrecMessage;
    }

    /**
     * qs2相关的处理
     */
    private PubcompProtocol handlePubrelMessage(PubrelProtocol message) {
        PubcompProtocol pubcompMessage = new PubcompProtocol();
        pubcompMessage.setPacketIdentifier(message.getPacketIdentifier());
        return pubcompMessage;
    }

    private void debug(Object message) {
        try {
            logger.debug(ObjectMapperUtil.objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
