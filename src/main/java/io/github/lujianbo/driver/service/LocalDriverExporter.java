package io.github.lujianbo.driver.service;

import io.github.lujianbo.driver.MQTTSentinel;

/**
 * Created by lujianbo on 2016/4/20.
 */
public class LocalDriverExporter implements DriverExporter{


    @Override
    public DriverService getDriverService(MQTTSentinel sentinel) {
        return null;
    }
}
