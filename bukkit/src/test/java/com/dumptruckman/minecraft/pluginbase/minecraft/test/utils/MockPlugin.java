package com.dumptruckman.minecraft.pluginbase.minecraft.test.utils;

import com.dumptruckman.minecraft.pluginbase.plugin.AbstractBukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.plugin.command.HelpCommand;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MockPlugin extends AbstractBukkitPlugin<MockConfig> {

    public void preEnable() {
        MockMessages.init();
        HelpCommand.addStaticPrefixedKey("");
    }
    
    @Override
    public List<String> getCommandPrefixes() {
        return Arrays.asList("pb");
    }

    @Override
    protected MockConfig newConfigInstance() throws IOException {
        return new MockConfig(this, true, new File(getDataFolder(), "config.yml"), MockConfig.class);
    }

    public List<String> dumpVersionInfo() {
        List<String> versionInfo = new LinkedList<String>();
        versionInfo.add("Test");
        return versionInfo;
    }
}
