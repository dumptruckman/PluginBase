package com.dumptruckman.minecraft.pluginbase;

import com.dumptruckman.minecraft.pluginbase.plugin.AbstractBukkitPlugin;

import java.io.IOException;

public class TestPlugin extends AbstractBukkitPlugin<TestConfig> {

    @Override
    public String getCommandPrefix() {
        return "pb";
    }

    @Override
    protected TestConfig newConfigInstance() throws IOException {
        return new Configg(this);
    }

    @Override
    protected boolean useDatabase() {
        return false;
    }
}
