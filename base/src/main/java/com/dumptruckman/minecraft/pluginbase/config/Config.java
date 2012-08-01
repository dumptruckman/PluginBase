package com.dumptruckman.minecraft.pluginbase.config;

import java.util.List;
import java.util.Map;

public interface Config {

    /**
     * Convenience method for saving the config to disk.
     */
    void save();
    
    <T> T get(SimpleConfigEntry<T> entry) throws IllegalArgumentException;

    <T> T get(ListConfigEntry<T> entry, int index) throws IllegalArgumentException;

    <T> List<T> get(ListConfigEntry<T> entry) throws IllegalArgumentException;

    <T> T get(MappedConfigEntry<T> entry, String key) throws IllegalArgumentException;

    <T> Map<String, T> get(MappedConfigEntry<T> entry) throws IllegalArgumentException;
    
    <T> boolean set(SimpleConfigEntry<T> entry, T value) throws IllegalArgumentException;

    //<T> boolean set(ListConfigEntry<T> entry, int index, T value) throws IllegalArgumentException;

    <T> boolean set(ListConfigEntry<T> entry, List<T> value) throws IllegalArgumentException;

    <T> boolean set(MappedConfigEntry<T> entry, String key, T value) throws IllegalArgumentException;

    //<T> boolean set(MappedConfigEntry<T> entry, Map<String, T> value) throws IllegalArgumentException;
}
