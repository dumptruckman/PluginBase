package com.dumptruckman.minecraft.pluginbase;

import com.dumptruckman.minecraft.pluginbase.bukkit.AbstractBukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.bukkit.properties.YamlProperties;
import com.dumptruckman.minecraft.pluginbase.messages.PluginBaseException;
import com.dumptruckman.minecraft.pluginbase.plugin.BaseConfig;
import com.dumptruckman.minecraft.pluginbase.properties.Properties;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class TestPlugin extends AbstractBukkitPlugin {

    @NotNull
    @Override
    public String getCommandPrefix() {
        return "pb";
    }

    @Override
    protected boolean useDatabase() {
        return false;
    }

    @NotNull
    @Override
    protected Properties getNewConfig() throws PluginBaseException {
        return new YamlProperties(true, true, new File(getDataFolder(), "config.yml"), BaseConfig.class);
    }
}
