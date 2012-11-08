package com.dumptruckman.minecraft.pluginbase.server;

import com.dumptruckman.minecraft.pluginbase.plugin.BukkitPlugin;
import org.bukkit.Server;

import java.io.File;

public class BukkitServerInterface implements ServerInterface<BukkitPlugin> {

    private final Server server;

    public BukkitServerInterface(final Server server) {
        this.server = server;
    }

    @Override
    public String getName() {
        return server.getName();
    }

    @Override
    public String getVersion() {
        return server.getVersion();
    }

    @Override
    public File getWorldContainer() {
        return server.getWorldContainer();
    }

    @Override
    public int scheduleAsyncDelayedTask(BukkitPlugin p, Runnable runnable) {
        return server.getScheduler().scheduleAsyncDelayedTask(p, runnable);
    }

    @Override
    public int scheduleSyncDelayedTask(BukkitPlugin p, Runnable runnable) {
        return server.getScheduler().scheduleSyncDelayedTask(p, runnable);
    }
}
