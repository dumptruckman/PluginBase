package com.dumptruckman.minecraft.pluginbase.properties;

import java.util.Collections;
import java.util.List;

class DefaultProperty<T> implements Property<T> {

    private final String path;
    private final List<String> comments;
    private final Class<T> type;

    public DefaultProperty(Class<T> type, String path, List<String> comments) {
        this.type = type;
        this.path = path;
        this.comments = Collections.unmodifiableList(comments);
    }

    @Override
    public String getName() {
        return path;
    }

    @Override
    public Class<T> getType() {
        return type;
    }

    @Override
    public List<String> getComments() {
        return comments;
    }
}
