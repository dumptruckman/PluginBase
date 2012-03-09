package com.dumptruckman.minecraft.pluginbase.minecraft.test.utils;

import com.dumptruckman.minecraft.pluginbase.config.BaseConfig;
import com.dumptruckman.minecraft.pluginbase.plugin.AbstractBukkitPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MockPlugin extends AbstractBukkitPlugin<MockConfig> {

    public void preEnable() {
        MockMessages.init();
    }
    
    @Override
    public List<String> getCommandPrefixes() {
        return Arrays.asList("pb");
    }

    @Override
    protected MockConfig newConfigInstance() throws IOException {
        return new MockConfig(this, true, new File(getDataFolder(), "config.yml"), MockConfig.class);
    }
}
