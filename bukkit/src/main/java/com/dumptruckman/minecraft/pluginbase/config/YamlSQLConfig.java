package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.properties.YamlProperties;

import java.io.File;
import java.io.IOException;

public class YamlSQLConfig extends YamlProperties implements SQLConfig {

    public YamlSQLConfig(File configFile) throws IOException {
        super(true, true, configFile, SQLConfig.class);
    }
}
