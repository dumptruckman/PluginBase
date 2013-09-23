package com.dumptruckman.minecraft.pluginbase.config.bukkit;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.MemoryConfiguration;

public class MemoryConfigurationTest extends ConfigurationTest {
    @Override
    public Configuration getConfig() {
        return new MemoryConfiguration();
    }
}
