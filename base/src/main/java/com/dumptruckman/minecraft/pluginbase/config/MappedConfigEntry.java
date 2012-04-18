package com.dumptruckman.minecraft.pluginbase.config;

import java.util.Map;

public interface MappedConfigEntry<T> extends ConfigEntry<T> {

    Map<String, T> getNewTypeMap();

    MappedConfigEntry<T> specific(String additionalPath);
    
    String getSpecificPath();
}
