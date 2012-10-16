package com.dumptruckman.minecraft.pluginbase.plugin;

public class BukkitPluginInfo implements PluginInfo {

    private final BukkitPlugin plugin;

    public BukkitPluginInfo(BukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return plugin.getDescription().getName();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }
}
