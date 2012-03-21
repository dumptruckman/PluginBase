package com.dumptruckman.minecraft.pluginbase.config;

import java.util.Map;

public interface Config {

    /**
     * Convenience method for saving the config to disk.
     */
    void save();
    
    <T> T get(ConfigEntry<T> entry) throws IllegalArgumentException;
    
    <T> Map<String, T> getMap(MappedConfigEntry<T> entry) throws IllegalArgumentException;
    
    <T> boolean set(ConfigEntry<T> entry, T value) throws IllegalArgumentException;
}
