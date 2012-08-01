package com.dumptruckman.minecraft.pluginbase.config;

/**
 * This represents the simplest of Config Entries and should represent a key : value pair where the value is not
 * a list or a map.
 *
 * @param <T> The type for the value of this entry.
 */
public interface SimpleConfigEntry<T> extends ConfigEntry<T> {
}
