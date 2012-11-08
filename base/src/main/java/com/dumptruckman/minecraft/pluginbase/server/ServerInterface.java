package com.dumptruckman.minecraft.pluginbase.server;

import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;

import java.io.File;

public interface ServerInterface<P extends PluginBase> {

    String getName();

    String getVersion();

    File getWorldContainer();

    int scheduleAsyncDelayedTask(P p, Runnable runnable);

    int scheduleSyncDelayedTask(P p, Runnable runnable);
}
