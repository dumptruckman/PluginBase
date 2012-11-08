package com.dumptruckman.minecraft.pluginbase.plugin;

public class BukkitPluginInfo implements PluginInfo {

    private final BukkitPlugin plugin;

    private String name = null;

    public BukkitPluginInfo(BukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return plugin.getDescription().getName();
    }

    @Override
    public String getPermissionName() {
        if (name == null) {
            return plugin.getDescription().getName();
        } else {
            return name;
        }
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    void setPermissionName(final String name) {
        this.name = name;
    }
}
