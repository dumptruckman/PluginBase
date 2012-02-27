package com.dumptruckman.tools.test.utils;

import com.dumptruckman.tools.plugin.AbstractPluginBase;

import java.io.IOException;

public class MockPlugin extends AbstractPluginBase<MockConfig> {

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
