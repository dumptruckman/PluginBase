package com.dumptruckman.minecraft.test.utils;

import com.dumptruckman.minecraft.plugin.AbstractBukkitPlugin;

import java.io.IOException;

public class MockPlugin extends AbstractBukkitPlugin<MockConfig> {

    public void preEnable() {
        MockMessages.init();
    }
    
    @Override
    public String getCommandPrefix() {
        return "pb";
    }

    @Override
    protected MockConfig newConfigInstance() throws IOException {
        return new MockConfig(this);
    }
}
