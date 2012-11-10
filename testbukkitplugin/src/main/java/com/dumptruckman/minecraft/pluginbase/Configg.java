package com.dumptruckman.minecraft.pluginbase;

import com.dumptruckman.minecraft.pluginbase.plugin.BukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.properties.YamlProperties;

import java.io.File;
import java.io.IOException;

public class Configg extends YamlProperties implements TestConfig {

    public Configg(BukkitPlugin plugin) throws IOException {
        super(true, true, new File(plugin.getDataFolder(), "config.yml"), TestConfig.class);
    }
}
