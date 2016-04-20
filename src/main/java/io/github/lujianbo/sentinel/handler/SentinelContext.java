package io.github.lujianbo.sentinel.handler;

import io.github.lujianbo.driver.MQTTSentinel;
import io.github.lujianbo.driver.common.BroadcastMessage;
import io.github.lujianbo.driver.common.PublishMessage;
import io.github.lujianbo.sentinel.protocol.PublishProtocol;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class SentinelContext extends MQTTSentinel {

    /**
     * 描述了clientId 和 MQTTConnection的关系
     * */
    private Map<String,MQTTConnection> maps=new ConcurrentHashMap<>();
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
