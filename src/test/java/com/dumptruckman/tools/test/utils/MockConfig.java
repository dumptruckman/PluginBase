package com.dumptruckman.tools.test.utils;

import com.dumptruckman.tools.config.AbstractYamlConfig;
import com.dumptruckman.tools.config.BaseConfig;
import com.dumptruckman.tools.config.ConfigEntry;
import com.dumptruckman.tools.config.Null;
import com.dumptruckman.tools.config.SimpleConfigEntry;
import com.dumptruckman.tools.plugin.PluginBase;

import java.io.IOException;

public class MockConfig extends AbstractYamlConfig implements BaseConfig {
    
    public static final ConfigEntry<Boolean> TEST = new SimpleConfigEntry<Boolean>("test", true, "# ===[ PluginBase Test ]===");
    public static final ConfigEntry<Null> SETTINGS = new SimpleConfigEntry<Null>("settings", null, "# ===[ PluginBase Settings ]===");
    
    public MockConfig(PluginBase plugin) throws IOException {
        super(plugin);
    }

    @Override
    protected ConfigEntry getSettingsEntry() {
       return SETTINGS;
    }

    @Override
    protected String getHeader() {
        return "# ===[ PluginBase Config ]===";
    }
}
