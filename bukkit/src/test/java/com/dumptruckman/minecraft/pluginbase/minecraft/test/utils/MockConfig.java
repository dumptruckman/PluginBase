package com.dumptruckman.minecraft.pluginbase.minecraft.test.utils;

import com.dumptruckman.minecraft.pluginbase.config.AbstractYamlConfig;
import com.dumptruckman.minecraft.pluginbase.config.BaseConfig;
import com.dumptruckman.minecraft.pluginbase.config.ConfigEntry;
import com.dumptruckman.minecraft.pluginbase.config.Config;
import com.dumptruckman.minecraft.pluginbase.config.Null;
import com.dumptruckman.minecraft.pluginbase.config.SimpleConfigEntry;
import com.dumptruckman.minecraft.pluginbase.plugin.BukkitPlugin;

import java.io.File;
import java.io.IOException;

public class MockConfig extends AbstractYamlConfig<MockConfig> implements BaseConfig {
    
    public static final ConfigEntry<Boolean> TEST = new SimpleConfigEntry<Boolean>(Boolean.class, "test", true, "# ===[ PluginBase Test ]===");
    public static final ConfigEntry<Null> SETTINGS = new SimpleConfigEntry<Null>(Null.class, "settings", null, "# ===[ PluginBase Settings ]===");
    
    public MockConfig(BukkitPlugin plugin, boolean doComments, File configFile, Class<? extends MockConfig> configClass) throws IOException {
        super(plugin, doComments, configFile, configClass);
    }

    @Override
    protected String getHeader() {
        return "# ===[ PluginBase Config ]===";
    }
}
