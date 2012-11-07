package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.plugin.BukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.properties.YamlProperties;

import java.io.File;
import java.io.IOException;

public class YamlSQLConfig extends YamlProperties implements SQLConfig {

    public YamlSQLConfig(BukkitPlugin plugin, File configFile) throws IOException {
        super(plugin, true, true, configFile, SQLConfig.class);
    }
}
