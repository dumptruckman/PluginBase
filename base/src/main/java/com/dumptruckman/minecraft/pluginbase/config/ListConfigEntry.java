package com.dumptruckman.minecraft.pluginbase.config;

import java.util.List;

public interface ListConfigEntry<T> extends ConfigEntry<T> {

    List<T> getNewTypeList();

    List<T> getDefaultList();
}
