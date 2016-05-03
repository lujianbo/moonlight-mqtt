package io.github.lujianbo.context.service;


import java.util.Iterator;

/**
 * 资源服务接口
 * */
public interface ContextService {

    /**
     * 订阅
     * */
    public boolean subscribe(String clientId,String topicFilter,byte qos);

    /**
     * 反订阅
     * */
    public boolean unSubscribe(String clientId,String topicFilter);


    /**
     * 移除clientId的订阅
     * */
    public void clear(String clientId);

    /**
     *  返回topic下的订阅者的迭代器
     * */
    public Iterator<String> findSubscriber(String topicFilter);

}
