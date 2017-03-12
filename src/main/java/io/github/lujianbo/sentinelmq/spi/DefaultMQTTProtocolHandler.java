package io.github.lujianbo.sentinelmq.spi;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.lujianbo.sentinelmq.common.handler.MQTTConnection;
import io.github.lujianbo.sentinelmq.common.handler.MQTTProtocolHandler;
import io.github.lujianbo.sentinelmq.common.protocol.*;
import io.github.lujianbo.sentinelmq.util.ObjectMapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Mqtt协议处理的默认实现
 * 通过实现topic的管理来完成
 */
public class DefaultMQTTProtocolHandler implements MQTTProtocolHandler {


    private Logger logger = LoggerFactory.getLogger(DefaultMQTTProtocolHandler.class);

    /**
     * 描述了clientId 和 MQTTConnection的关系,
     */
    private ConcurrentHashMap<String, MQTTConnection> maps = new ConcurrentHashMap<>();

    /**
     * topic的结点管理,用于向topic的结点添加和查找其中的订阅者
     */
    private DefaultMQTTTopicManager topicManager;

    /**
     * 用于执行耗时的异步任务
     */
    private ExecutorService pool = Executors.newCachedThreadPool(runnable -> {
        Thread thread = new Thread();
        thread.setDaemon(true);
        return thread;
    });


    public DefaultMQTTProtocolHandler() {
        this.topicManager = new DefaultMQTTTopicManager("");
    }

    public DefaultMQTTProtocolHandler(DefaultMQTTTopicManager topicManager) {
        this.topicManager = topicManager;
    }

    /**
     * 处理MQTT的连接和登陆部分的代码
     */
    public void onRead(MQTTConnection connection, ConnectProtocol message) {

        maps.put(message.getClientId(), connection);
        connection.setClientId(message.getClientId());//clientId 和 connection绑定

        byte returnCode = ConnackProtocol.CONNECTION_ACCEPTED;
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
        onClose(connection);
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
        /**
         * 根据消息类型分别处理
         * */
        switch (message.getQosLevel()) {
            case MQTTProtocol.mostOnce:
                handlePublishQS0Message(message);
                break;
            case MQTTProtocol.leastOnce:
                connection.write(handlePublishQS1Message(message));
                break;
            case MQTTProtocol.exactlyOnce:
                connection.write(handlePublishQS2Message(message));
                break;
        }
    }


    /**
     * 收到qos1的返回
     */
    public void onRead(MQTTConnection connection, PubackProtocol message) {
        //可以删除信息
    }

    /**
     * 已经完成
     */
    public void onRead(MQTTConnection connection, PubcompProtocol message) {
        //可以删除信息
    }

    /**
     * 已经释放
     */
    public void onRead(MQTTConnection connection, PubrelProtocol message) {
        PubcompProtocol pubcompProtocol = new PubcompProtocol();
        pubcompProtocol.setPacketIdentifier(message.getPacketIdentifier());
        connection.write(pubcompProtocol);
    }

    /**
     * 已经收到
     */
    public void onRead(MQTTConnection connection, PubrecProtocol message) {
        //发送已经释放的消息
        PubrelProtocol pubrelProtocol = new PubrelProtocol();
        pubrelProtocol.setPacketIdentifier(message.getPacketIdentifier());
        connection.write(pubrelProtocol);
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
        //构建返回值
        SubackProtocol subackMessage = new SubackProtocol();
        subackMessage.setPacketIdentifier(message.getPacketIdentifier());
        String clientId = connection.getClientId();
        for (SubscribeProtocol.TopicFilterQoSPair pair : message.getPairs()) {
            //订阅topic
            int count = this.topicManager.subscribe(clientId, pair.getTopicFilter());
            if (count > 0) {
                if (pair.getQos() == MQTTProtocol.mostOnce) {
                    subackMessage.addReturnCode(SubackProtocol.SuccessMaximumQoS0);
                }
                if (pair.getQos() == MQTTProtocol.leastOnce) {
                    subackMessage.addReturnCode(SubackProtocol.SuccessMaximumQoS1);
                }
                if (pair.getQos() == MQTTProtocol.exactlyOnce) {
                    subackMessage.addReturnCode(SubackProtocol.SuccessMaximumQoS2);
                }
            } else {
                subackMessage.addReturnCode(SubackProtocol.Failure);
            }
        }
        connection.write(subackMessage);
    }

    /**
     * 取消订阅的操作
     */
    public void onRead(MQTTConnection connection, UnsubscribeProtocol message) {
        UnsubackProtocol unsubackMessage = new UnsubackProtocol();
        for (String topicName : message.getTopicNames()) {
            this.topicManager.unSubscribe(connection.getClientId(), topicName);
        }
        unsubackMessage.setPacketIdentifier(message.getPacketIdentifier());
        connection.write(unsubackMessage);
    }


    public void onRead(MQTTConnection connection, UnsubackProtocol message) {
        connection.close();
    }


    public void onClose(MQTTConnection connection) {
        maps.remove(connection.getClientId());
        this.topicManager.clear(connection.getClientId());
    }


    public void onException(MQTTConnection connection) {
        maps.remove(connection.getClientId());
    }

    /**
     * 处理publish
     */
    private void handlePublishQS0Message(PublishProtocol message) {
        //找到所有订阅者进行push
        Iterator<String> iterator = this.topicManager.findSubscriber(message.getTopicName());
        if (iterator != null) {
            iterator.forEachRemaining(clientId -> maps.get(clientId).write(message));
        }
    }

    /**
     * 处理qs1的publish
     */
    private PubackProtocol handlePublishQS1Message(PublishProtocol message) {
        this.topicManager.findSubscriber(message.getTopicName()).forEachRemaining(clientId -> {
            maps.get(clientId).write(message);
        });
        PubackProtocol pubackMessage = new PubackProtocol();
        pubackMessage.setPacketIdentifier(message.getPacketIdentifier());
        return pubackMessage;
    }

    /**
     * 处理qs2的publish
     */
    private PubrecProtocol handlePublishQS2Message(PublishProtocol message) {
        this.topicManager.findSubscriber(message.getTopicName()).forEachRemaining(clientId -> {
            maps.get(clientId).write(message);
        });
        PubrecProtocol pubrecMessage = new PubrecProtocol();
        pubrecMessage.setPacketIdentifier(message.getPacketIdentifier());
        return pubrecMessage;
    }

}
