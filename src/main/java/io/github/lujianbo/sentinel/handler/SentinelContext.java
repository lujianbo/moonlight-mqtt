package io.github.lujianbo.sentinel.handler;

import io.github.lujianbo.driver.service.DriverConnection;
import io.github.lujianbo.driver.common.BroadcastMessage;
import io.github.lujianbo.sentinel.protocol.PublishProtocol;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class SentinelContext extends DriverConnection {

    /**
     * 描述了clientId 和 MQTTConnection的关系
     * */
    private Map<String,MQTTConnection> maps=new ConcurrentHashMap<>();


    public void add(String clientId,MQTTConnection connection){
        maps.put(clientId,connection);
    }

    /**
     * 实现信息的广播
     * */
    @Override
    public void broadcast(BroadcastMessage message) {
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

    }

    @Override
    public void close(String clientId) {

    }
}
