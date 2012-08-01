package com.dumptruckman.minecraft.pluginbase.config;

import java.util.List;

public interface ListConfigEntry<T> extends BaseConfigEntry<T> {

    List<T> getNewTypeList();

    List<T> getDefaultList();
}
