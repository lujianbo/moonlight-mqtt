package io.github.lujianbo.mqtt.manager;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by lujianbo on 2016/4/18.
 */
public abstract class AbstractManager {

    /**
     * 当前manager是否可用
     *
     * */
    public final AtomicBoolean available=new AtomicBoolean();


    /**
     * open方法用来加载和初始化整个Manager
     * */
    abstract public void open();

    /**
     * close方法用来关闭manager，在实现中，用于保存当前的状态
     * */
    abstract public void close();

    public void initialization(){


    }

    public void destroy(){

    }
}
