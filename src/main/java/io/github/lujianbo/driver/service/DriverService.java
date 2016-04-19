package io.github.lujianbo.driver.service;


public interface DriverService {

    /**
     * 登陆信息
     * */
    public void auth(String clientId,String userName,String password);


    public void subscribe(String clientId,String topicName);


    public void unSubscribe(String clientId,String topicName);


    public void publish();

    /**
     * 关闭信息
     * */
    public void close(String client);
}
