package com.dumptruckman.minecraft.pluginbase.plugin;

import com.dumptruckman.minecraft.pluginbase.permission.PermInfo;

public interface PluginInfo extends PermInfo {

    String getName();

    String getPermissionName();

    String getVersion();
}
