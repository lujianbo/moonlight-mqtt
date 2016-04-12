package io.github.lujianbo.mqtt.topic;

import java.util.HashSet;

/**
 *
 */
public class Topic<T> {

    private final String name;

    private final HashSet<T> listeners = new HashSet<>();

    public Topic(String name) {
        this.name = name;
    }

    public void addListener(T listener) {
        this.listeners.add(listener);
    }

    public void removeListener(T listener) {
        this.listeners.remove(listener);
    }

    public HashSet<T> getListeners() {
        return listeners;
    }
}
