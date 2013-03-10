package com.dumptruckman.minecraft.pluginbase.plugin;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * An interface for interacting with required aspects the Minecraft server.
 * <p/>
 * This must be implemented by the specific server implementation of PluginBase.
 *
 * @param <P> The server implementation of the PluginBase plugin.
 */
public interface ServerInterface<P extends PluginBase> {

    /**
     * Returns the name of the server implementation.
     *
     * @return the name of the server implementation.
     */
    @NotNull
    String getName();

    /**
     * Returns the server version.
     *
     * @return the server version.
     */
    @NotNull
    String getVersion();

    /**
     * Returns the directory in which worlds are stored.
     *
     * @return the directory in which worlds are stored.
     */
    @NotNull
    File getWorldContainer();

    /**
     * Returns the directory in which the minecraft server is hosted.
     *
     * @return the directory in which the minecraft server is hosted.
     */
    @NotNull
    File getServerFolder();

    int runTask(@NotNull final P p, @NotNull final Runnable runnable);

    int runTaskAsynchronously(@NotNull final P p, @NotNull final Runnable runnable);

    int runTaskLater(@NotNull final P p, @NotNull final Runnable runnable, long delay);

    int runTaskLaterAsynchronously(@NotNull final P p, @NotNull final Runnable runnable, long delay);

    int runTaskTimer(@NotNull final P p, @NotNull final Runnable runnable, long delay, long period);

    int runTaskTimerAsynchronously(@NotNull final P p, @NotNull final Runnable runnable, long delay, long period);
}
