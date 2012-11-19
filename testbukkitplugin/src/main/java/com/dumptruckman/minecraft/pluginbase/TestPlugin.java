package com.dumptruckman.minecraft.pluginbase;

import com.dumptruckman.minecraft.pluginbase.config.BaseConfig;
import com.dumptruckman.minecraft.pluginbase.plugin.AbstractBukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.properties.Properties;
import com.dumptruckman.minecraft.pluginbase.properties.YamlProperties;

import java.io.File;
import java.io.IOException;

public class TestPlugin extends AbstractBukkitPlugin {

    @Override
    public String getCommandPrefix() {
        return "pb";
    }

    @Override
    protected boolean useDatabase() {
        return false;
    }

    @Override
    protected Properties getNewConfig() throws IOException {
        return new YamlProperties(true, true, new File(getDataFolder(), "config.yml"), BaseConfig.class);
    }
}
