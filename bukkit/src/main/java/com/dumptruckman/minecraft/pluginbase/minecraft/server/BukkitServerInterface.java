package com.dumptruckman.minecraft.pluginbase.minecraft.server;

import com.dumptruckman.minecraft.pluginbase.plugin.BukkitPlugin;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class BukkitServerInterface implements ServerInterface<BukkitPlugin> {

    private final Server server;

    public BukkitServerInterface(final Server server) {
        this.server = server;
    }

    @NotNull
    @Override
    public String getName() {
        return server.getName();
    }

    @NotNull
    @Override
    public String getVersion() {
        return server.getVersion();
    }

    @Override
    public File getWorldContainer() {
        return server.getWorldContainer();
    }

    @Override
    public int runTask(@NotNull final BukkitPlugin p, @NotNull final Runnable runnable) {
        return server.getScheduler().runTask(p, runnable).getTaskId();
    }

    @Override
    public int runTaskAsynchronously(@NotNull final BukkitPlugin p, @NotNull final Runnable runnable) {
        return server.getScheduler().runTaskAsynchronously(p, runnable).getTaskId();
    }

    @Override
    public int runTaskLater(@NotNull final BukkitPlugin p, @NotNull final Runnable runnable, final long delay) {
        return server.getScheduler().runTaskLater(p, runnable, delay).getTaskId();
    }

    @Override
    public int runTaskLaterAsynchronously(@NotNull final BukkitPlugin p, @NotNull final Runnable runnable, final long delay) {
        return server.getScheduler().runTaskLaterAsynchronously(p, runnable, delay).getTaskId();
    }

    @Override
    public int runTaskTimer(@NotNull final BukkitPlugin p, @NotNull final Runnable runnable, final long delay, final long period) {
        return server.getScheduler().runTaskTimer(p, runnable, delay, period).getTaskId();
    }

    @Override
    public int runTaskTimerAsynchronously(@NotNull final BukkitPlugin p, @NotNull final Runnable runnable, final long delay, final long period) {
        return server.getScheduler().runTaskTimerAsynchronously(p, runnable, delay, period).getTaskId();
    }
}
