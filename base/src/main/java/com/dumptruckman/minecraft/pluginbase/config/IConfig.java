package com.dumptruckman.minecraft.pluginbase.config;

public interface IConfig {

    /**
     * Convenience method for saving the config to disk.
     */
    void save();
    
    <T> T get(ConfigEntry<T> entry);
    
    <T> boolean set(ConfigEntry<T> entry, T value);
}
