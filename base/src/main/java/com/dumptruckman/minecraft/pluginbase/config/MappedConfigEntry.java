package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.locale.Message;

import java.util.List;
import java.util.Map;

public interface MappedConfigEntry<T> extends ConfigEntry<T> {

    Map<String, T> getNewTypeMap();

    MappedConfigEntry<T> specific(String additionalPath);
    
    String getSpecificPath();
}
