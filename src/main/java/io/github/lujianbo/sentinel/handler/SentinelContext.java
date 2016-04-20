package io.github.lujianbo.sentinel.handler;

import java.util.Map;

/**
 * Created by lujianbo on 2016/4/19.
 */
public class SentinelContext {

    /**
     * 描述了clientId 和 MQTTConnection的关系
     * */
    private Map<String,MQTTConnection> maps;


    public void add(String clientId,MQTTConnection connection){

    }


}
