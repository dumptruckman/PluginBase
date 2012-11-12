package com.dumptruckman.minecraft.pluginbase.util;

import com.dumptruckman.minecraft.pluginbase.plugin.AbstractBukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.plugin.command.HelpCommand;

import java.util.LinkedList;
import java.util.List;

public class MockPlugin extends AbstractBukkitPlugin {

    public void onPluginLoad() {
        MockMessages.init();
        HelpCommand.addStaticPrefixedKey("");
    }

    public void onPluginEnable() {
        //getCommandHandler().registerCommand(new MockQueuedCommand(this));
    }
    
    @Override
    public String getCommandPrefix() {
        return "pb";
    }

    @Override
    protected Class[] getConfigClasses() {
        return new Class[] { MockConfig.class };
    }

    public List<String> dumpVersionInfo() {
        List<String> versionInfo = new LinkedList<String>();
        versionInfo.add("Test");
        return versionInfo;
    }

    @Override
    protected boolean useDatabase() {
        return true;
    }
}
