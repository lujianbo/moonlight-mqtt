package io.github.lujianbo.engine.wapper;

import io.github.lujianbo.context.service.ContextService;
import io.github.lujianbo.engine.core.MQTTEngine;

/**
 * Created by lujianbo on 2016/4/20.
 */
public class SingleSentinelEngine extends MQTTEngine {


    public SingleSentinelEngine(ContextService contextService) {
        super(contextService);
    }
}
