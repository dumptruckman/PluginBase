package com.dumptruckman.tools.test.utils;

import com.dumptruckman.tools.config.AbstractYamlConfig;
import com.dumptruckman.tools.config.ConfigBase;
import com.dumptruckman.tools.config.ConfigEntry;
import com.dumptruckman.tools.config.Entries;
import com.dumptruckman.tools.plugin.PluginBase;

import java.io.IOException;

public class MockConfig extends AbstractYamlConfig implements ConfigBase {
    
    public static final ConfigEntry TEST = new ConfigEntry("test", true, "# ===[ PluginBase Test ]===");
    
    public MockConfig(PluginBase plugin) throws IOException {
        super(plugin);
    }

    @Override
    protected ConfigEntry getSettingsHeader() {
        return SETTINGS;
    }
}
