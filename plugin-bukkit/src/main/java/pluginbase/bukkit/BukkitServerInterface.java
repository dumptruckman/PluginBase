package pluginbase.bukkit;

import org.bukkit.plugin.Plugin;
import pluginbase.bukkit.minecraft.BukkitTools;
import pluginbase.minecraft.BasePlayer;
import pluginbase.plugin.ServerInterface;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

class BukkitServerInterface implements ServerInterface {

    private final Plugin plugin;
    private final File serverFolder;

    BukkitServerInterface(@NotNull final Plugin plugin) {
        this.plugin = plugin;
        this.serverFolder = new File(".");
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public String getName() {
        return plugin.getServer().getName();
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public String getVersion() {
        return plugin.getServer().getVersion();
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public File getWorldContainer() {
        return plugin.getServer().getWorldContainer();
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public File getServerFolder() {
        return serverFolder;
    }

    /** {@inheritDoc} */
    @Nullable
    @Override
    public BasePlayer getPlayer(final String name) {
        return BukkitTools.wrapPlayer(plugin.getServer().getPlayer(name));
    }

    /** {@inheritDoc} */
    @Override
    public int runTask(@NotNull final Runnable runnable) {
        return plugin.getServer().getScheduler().runTask(plugin, runnable).getTaskId();
    }

    /** {@inheritDoc} */
    @Override
    public int runTaskAsynchronously(@NotNull final Runnable runnable) {
        return plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable).getTaskId();
    }

    /** {@inheritDoc} */
    @Override
    public int runTaskLater(@NotNull final Runnable runnable, final long delay) {
        return plugin.getServer().getScheduler().runTaskLater(plugin, runnable, delay).getTaskId();
    }

    /** {@inheritDoc} */
    @Override
    public int runTaskLaterAsynchronously(@NotNull final Runnable runnable, final long delay) {
        return plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay).getTaskId();
    }

    /** {@inheritDoc} */
    @Override
    public int runTaskTimer(@NotNull final Runnable runnable, final long delay, final long period) {
        return plugin.getServer().getScheduler().runTaskTimer(plugin, runnable, delay, period).getTaskId();
    }

    /** {@inheritDoc} */
    @Override
    public int runTaskTimerAsynchronously(@NotNull final Runnable runnable, final long delay, final long period) {
        return plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, period).getTaskId();
    }
}
