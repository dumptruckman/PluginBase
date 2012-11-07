package com.dumptruckman.minecraft.pluginbase.properties;

import java.util.List;

class DefaultNestedProperty<T extends NestedProperties> extends DefaultProperty<T> implements NestedProperty<T> {

    DefaultNestedProperty(Class<T> type, String path, List<String> comments) {
        super(type, path, comments);
    }
}
