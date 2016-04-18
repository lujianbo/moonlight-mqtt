package io.github.lujianbo.mqtt.common;

import java.util.HashSet;

/**
 *
 */
public class MQTTTopic {

    private final String name;

    private final HashSet<String> listeners = new HashSet<>();

    public MQTTTopic(String name) {
        this.name = name;
    }

    public void addListener(String listener) {
        this.listeners.add(listener);
    }

    public void removeListener(String listener) {
        this.listeners.remove(listener);
    }

    public HashSet<String> getListeners() {
        return listeners;
    }
}
