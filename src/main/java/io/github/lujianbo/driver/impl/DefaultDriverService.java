package io.github.lujianbo.driver.impl;

import io.github.lujianbo.driver.service.DriverService;

/**
 * 默认的 DriverService 实现
 */
public class DefaultDriverService implements DriverService{




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
