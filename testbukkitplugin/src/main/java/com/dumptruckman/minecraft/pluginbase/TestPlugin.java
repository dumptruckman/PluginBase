package com.dumptruckman.minecraft.pluginbase;

import com.dumptruckman.minecraft.pluginbase.plugin.AbstractBukkitPlugin;

public class TestPlugin extends AbstractBukkitPlugin {

    @Override
    public String getCommandPrefix() {
        return "pb";
    }

    @Override
    protected Class[] getConfigClasses() {
        return new Class[0];
    }

    @Override
    protected boolean useDatabase() {
        return false;
    }
}
