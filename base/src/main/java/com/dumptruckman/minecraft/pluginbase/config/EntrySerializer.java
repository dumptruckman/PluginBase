package com.dumptruckman.minecraft.pluginbase.config;

public interface EntrySerializer<T> {

    T deserialize(Object o);

    Object serialize(T t);
}
