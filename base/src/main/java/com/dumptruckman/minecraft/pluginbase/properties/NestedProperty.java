package com.dumptruckman.minecraft.pluginbase.properties;

public interface NestedProperty<T extends NestedProperties> extends Property<T> {

    Class<T> getType();
}
