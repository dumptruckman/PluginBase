package com.dumptruckman.minecraft.pluginbase.config;

import java.util.List;
import java.util.Locale;

public interface IConfig {

    /**
     * Convenience method for saving the config to disk.
     */
    void save();

    Locale get(ConfigEntry<Locale> entry);

    boolean set(ConfigEntry entry, Object newValue);

    Integer get(ConfigEntry<Integer> entry);
    Boolean get(ConfigEntry<Boolean> entry);
    String get(ConfigEntry<String> entry);
    //List get(ConfigEntry<List> entry);
    List get(ConfigEntry<List<String>> entry);


    //Object get(ConfigEntry entry);

    // void set(ConfigEntry entry, Object newValue);
}
