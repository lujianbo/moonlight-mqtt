package io.github.lujianbo.driver.service;


import io.github.lujianbo.driver.MQTTSentinel;

/**
 * Driver服务的提供者
 */
public interface DriverExporter {

    public DriverService getDriverService(MQTTSentinel sentinel);
}
