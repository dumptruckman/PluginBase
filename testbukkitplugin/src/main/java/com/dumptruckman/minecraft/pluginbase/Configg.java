package com.dumptruckman.minecraft.pluginbase;

import com.dumptruckman.minecraft.pluginbase.plugin.BukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.properties.AbstractYamlProperties;

import java.io.File;
import java.io.IOException;

public class Configg extends AbstractYamlProperties implements TestConfig {

    public Configg(BukkitPlugin plugin) throws IOException {
        super(plugin, true, true, new File(plugin.getDataFolder(), "config.yml"), TestConfig.class);
    }
}
