package pluginbase.plugin;

import pluginbase.minecraft.BasePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * An interface for interacting with required aspects the Minecraft server.
 * <br>
 * This must be implemented by the specific server implementation of PluginBase.
 */
public interface ServerInterface {

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

    /**
     * Gets a player by his name.
     * @param name The player's name.
     * @return The player object or {@code null} if the player was not found.
     */
    @Nullable
    BasePlayer getPlayer(String name);

    int runTask(@NotNull final Runnable runnable);

    int runTaskAsynchronously(@NotNull final Runnable runnable);

    int runTaskLater(@NotNull final Runnable runnable, long delay);

    int runTaskLaterAsynchronously(@NotNull final Runnable runnable, long delay);

    int runTaskTimer(@NotNull final Runnable runnable, long delay, long period);

    int runTaskTimerAsynchronously(@NotNull final Runnable runnable, long delay, long period);

    void cancelTask(int taskId);
}
