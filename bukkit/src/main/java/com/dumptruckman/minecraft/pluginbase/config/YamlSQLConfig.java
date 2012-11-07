package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.plugin.BukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.properties.AbstractYamlProperties;

import java.io.File;
import java.io.IOException;

public class YamlSQLConfig extends AbstractYamlProperties implements SQLConfig {

    public YamlSQLConfig(BukkitPlugin plugin, File configFile) throws IOException {
        super(plugin, true, true, configFile, SQLConfig.class);
    }
}
