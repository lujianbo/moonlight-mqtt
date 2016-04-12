package io.github.lujianbo.mqtt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.lujianbo.mqtt.domain.*;
import io.github.lujianbo.mqtt.manager.MQTTContext;
import io.github.lujianbo.util.ObjectMapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler处理器
 */
public interface MQTTMessageHandler {

    public void onRead(MQTTMessage message);
}
