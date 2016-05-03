package io.github.lujianbo.engine.wapper;

import io.github.lujianbo.context.service.ContextService;
import io.github.lujianbo.engine.core.MQTTEngine;

/**
 * 采用网络模式的Engine
 * */
public class MultipleSentinelEngine extends MQTTEngine {


    public MultipleSentinelEngine(ContextService contextService) {
        super(contextService);
    }
}
