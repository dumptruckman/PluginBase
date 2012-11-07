package com.dumptruckman.minecraft.pluginbase.properties;

public interface FileProperties extends Properties {

    void save();

    void reload() throws Exception;
}
