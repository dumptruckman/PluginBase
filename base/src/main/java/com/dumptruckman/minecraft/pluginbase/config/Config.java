package com.dumptruckman.minecraft.pluginbase.config;

public interface Config<C> {

    /**
     * Convenience method for saving the config to disk.
     */
    void save();
    
    <T> T get(ConfigEntry<T> entry) throws IllegalArgumentException;
    
    <T> boolean set(ConfigEntry<T> entry, T value) throws IllegalArgumentException;
}
