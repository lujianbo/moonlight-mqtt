package io.github.lujianbo.context;

import io.github.lujianbo.context.impl.DefaultContextService;
import io.github.lujianbo.context.service.ContextService;

/**
 * 容器服务,启动器
 */
public class ContextServer {

    public ContextServer(){

    }

    public ContextService getDefaultService(){
        return new DefaultContextService();
    }

}
