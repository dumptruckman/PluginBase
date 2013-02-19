package com.dumptruckman.minecraft.pluginbase.bukkit;

import com.dumptruckman.minecraft.pluginbase.minecraft.server.ServerInterface;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;

import java.io.File;

class BukkitServerInterface implements ServerInterface<BukkitPlugin> {

    private final Server server;

    BukkitServerInterface(final Server server) {
        this.server = server;
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public String getName() {
        return server.getName();
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public String getVersion() {
        return server.getVersion();
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public File getWorldContainer() {
        return server.getWorldContainer();
    }

    /** {@inheritDoc} */
    @Override
    public int runTask(@NotNull final BukkitPlugin p, @NotNull final Runnable runnable) {
        return server.getScheduler().runTask(p, runnable).getTaskId();
    }

    /** {@inheritDoc} */
    @Override
    public int runTaskAsynchronously(@NotNull final BukkitPlugin p, @NotNull final Runnable runnable) {
        return server.getScheduler().runTaskAsynchronously(p, runnable).getTaskId();
    }

    /** {@inheritDoc} */
    @Override
    public int runTaskLater(@NotNull final BukkitPlugin p, @NotNull final Runnable runnable, final long delay) {
        return server.getScheduler().runTaskLater(p, runnable, delay).getTaskId();
    }

    /** {@inheritDoc} */
    @Override
    public int runTaskLaterAsynchronously(@NotNull final BukkitPlugin p, @NotNull final Runnable runnable, final long delay) {
        return server.getScheduler().runTaskLaterAsynchronously(p, runnable, delay).getTaskId();
    }

    /** {@inheritDoc} */
    @Override
    public int runTaskTimer(@NotNull final BukkitPlugin p, @NotNull final Runnable runnable, final long delay, final long period) {
        return server.getScheduler().runTaskTimer(p, runnable, delay, period).getTaskId();
    }

    /** {@inheritDoc} */
    @Override
    public int runTaskTimerAsynchronously(@NotNull final BukkitPlugin p, @NotNull final Runnable runnable, final long delay, final long period) {
        return server.getScheduler().runTaskTimerAsynchronously(p, runnable, delay, period).getTaskId();
    }
}
