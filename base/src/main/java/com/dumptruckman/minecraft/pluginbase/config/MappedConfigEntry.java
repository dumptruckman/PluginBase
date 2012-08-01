package com.dumptruckman.minecraft.pluginbase.config;

import java.util.Map;

public interface MappedConfigEntry<T> extends BaseConfigEntry<T> {

    Map<String, T> getNewTypeMap();

    MappedConfigEntry<T> specific(String additionalPath);
    
    String getSpecificPath();
}
