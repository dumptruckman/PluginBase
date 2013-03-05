package com.dumptruckman.minecraft.pluginbase.util;

import com.dumptruckman.minecraft.pluginbase.bukkit.AbstractBukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.bukkit.properties.YamlProperties;
import com.dumptruckman.minecraft.pluginbase.messages.PluginBaseException;
import com.dumptruckman.minecraft.pluginbase.properties.Properties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class MockPlugin extends AbstractBukkitPlugin {

    @Override
    public void onPluginLoad() {
        MockMessages.init();
        //HelpCommand.addStaticPrefixedKey("");
    }

    @Override
    public void onPluginEnable() {
        //getCommandHandler().registerCommand(new MockQueuedCommand(this));
    }

    @NotNull
    @Override
    public String getCommandPrefix() {
        return "pb";
    }

    @Nullable
    @Override
    public List<String> dumpVersionInfo() {
        List<String> versionInfo = new LinkedList<String>();
        versionInfo.add("Test");
        return versionInfo;
    }

    @NotNull
    @Override
    protected Properties getNewConfig() throws PluginBaseException {
        return new YamlProperties.Loader(new File(getDataFolder(), "config.yml"), MockConfig.class).load();
    }

    @Override
    protected boolean useDatabase() {
        return true;
    }
}
