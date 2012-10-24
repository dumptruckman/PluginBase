package com.dumptruckman.minecraft.pluginbase.util;

import com.dumptruckman.minecraft.pluginbase.plugin.AbstractBukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.plugin.command.HelpCommand;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class MockPlugin extends AbstractBukkitPlugin<MockConfig> {

    public void preEnable() {
        MockMessages.init();
        HelpCommand.addStaticPrefixedKey("");
        initDatabase();
    }

    public void postEnable() {
        //getCommandHandler().registerCommand(new MockQueuedCommand(this));
    }
    
    @Override
    public String getCommandPrefix() {
        return "pb";
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
