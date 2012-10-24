package com.dumptruckman.minecraft.pluginbase;

import com.dumptruckman.minecraft.pluginbase.config.AbstractYamlConfig;
import com.dumptruckman.minecraft.pluginbase.plugin.BukkitPlugin;

import java.io.File;
import java.io.IOException;

public class Configg extends AbstractYamlConfig implements TestConfig {

    public Configg(BukkitPlugin plugin) throws IOException {
        super(plugin, true, true, new File(plugin.getDataFolder(), "config.yml"), TestConfig.class);
    }
}
