package io.github.lujianbo.mqtt.behavior;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * 根据协议规范对于每一个clientId我们应当保持其会话
 * */
public class StateStoring {

    /**
     * 用来存储clientId和UserName的关联
     * */
    BiMap<String,String> sessionMap= HashBiMap.create();




}
