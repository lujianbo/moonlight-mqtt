package io.github.lujianbo.driver.service;

import io.github.lujianbo.driver.MQTTSentinel;

/**
 * 内存内链接的实现
 */
public class DefaultDriverService implements DriverService{

    /**
     * 此接口对应的sentinel
     * */
    private MQTTSentinel sentinel;


    @Override
    public void auth(String clientId, String userName, String password) {

    }

    @Override
    public void subscribe(String clientId, String topicName) {

    }

    @Override
    public void unSubscribe(String clientId, String topicName) {

    }

    @Override
    public void publish() {

    }

    @Override
    public void close(String client) {

    }
}
