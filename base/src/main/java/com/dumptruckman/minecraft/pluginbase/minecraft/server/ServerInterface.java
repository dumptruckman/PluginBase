package com.dumptruckman.minecraft.pluginbase.minecraft.server;

import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public interface ServerInterface<P extends PluginBase> {

    @NotNull
    String getName();

    @NotNull
    String getVersion();

    File getWorldContainer();

    int runTask(@NotNull final P p, @NotNull final Runnable runnable);

    int runTaskAsynchronously(@NotNull final P p, @NotNull final Runnable runnable);

    int runTaskLater(@NotNull final P p, @NotNull final Runnable runnable, long delay);

    int runTaskLaterAsynchronously(@NotNull final P p, @NotNull final Runnable runnable, long delay);

    int runTaskTimer(@NotNull final P p, @NotNull final Runnable runnable, long delay, long period);

    int runTaskTimerAsynchronously(@NotNull final P p, @NotNull final Runnable runnable, long delay, long period);
}
