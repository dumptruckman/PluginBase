package com.dumptruckman.minecraft.pluginbase.properties;

public interface PropertyChangeListener<T> {

    void propertyChange(PropertyChangeEvent<T> event);
}
