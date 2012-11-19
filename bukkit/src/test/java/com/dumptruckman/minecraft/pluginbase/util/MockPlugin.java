package com.dumptruckman.minecraft.pluginbase.util;

import com.dumptruckman.minecraft.pluginbase.plugin.AbstractBukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.plugin.command.HelpCommand;
import com.dumptruckman.minecraft.pluginbase.properties.Properties;
import com.dumptruckman.minecraft.pluginbase.properties.YamlProperties;

import java.io.File;
import java.io.IOException;
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

    public List<String> dumpVersionInfo() {
        List<String> versionInfo = new LinkedList<String>();
        versionInfo.add("Test");
        return versionInfo;
    }

    protected Properties getNewConfig() throws IOException {
        return new YamlProperties(true, true, new File(getDataFolder(), "config.yml"), MockConfig.class);
    }

    @Override
    protected boolean useDatabase() {
        return true;
    }
}
