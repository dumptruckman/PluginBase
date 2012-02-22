package com.dumptruckman.tools.test.utils;

import com.dumptruckman.tools.plugin.AbstractPluginBase;

public class MockPlugin extends AbstractPluginBase<MockConfig> {

    @Override
    public String getCommandPrefix() {
        return "dt";
    }

    @Override
    protected MockConfig newConfigInstance() throws Exception {
        return new MockConfig(this);
    }
}
