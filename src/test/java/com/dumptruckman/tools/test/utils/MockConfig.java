package com.dumptruckman.tools.test.utils;

import com.dumptruckman.tools.config.AbstractYamlConfig;
import com.dumptruckman.tools.config.ConfigBase;
import com.dumptruckman.tools.config.ConfigEntry;
import com.dumptruckman.tools.plugin.PluginBase;

public class MockConfig extends AbstractYamlConfig implements ConfigBase {

    public MockConfig(PluginBase plugin) throws Exception {
        super(plugin);
    }
    @Override
    protected ConfigEntry getSettingsHeader() {
        return SETTINGS;
    }
}
