package io.github.lujianbo.engine.wapper;

import io.github.lujianbo.context.service.ContextService;
import io.github.lujianbo.engine.core.MQTTEngine;

/**
 * Local模式的Sentinel
 */
public class SingleSentinelEngine extends MQTTEngine {


    public SingleSentinelEngine(ContextService contextService) {
        super(contextService);
    }
}
