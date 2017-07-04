package pluginbase.bukkit.command;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import pluginbase.command.AbstractCommandProvider;
import pluginbase.command.CommandHandler;
import pluginbase.command.QueuedCommand;

import java.io.File;

/**
 * Provides a basic implementation of CommandProvider for use with Bukkit plugin's that are not utilizing the
 * Plugin-Bukkit module.
 * <br>
 * This implementation will provide a {@link pluginbase.logging.PluginLogger} and a {@link pluginbase.messages.messaging.Messager}
 * for your plugin so you will not need to create these on your own.
 */
public final class BukkitCommandProvider<O> extends AbstractCommandProvider<O> {

    private final Plugin plugin;
    private final BukkitCommandHandler commandHandler;

    /**
     * Creates a new instance of the basic Bukkit implementation of CommandProvider which can be used for registering
     * and running commands.
     * <br>
     * Queued commands are enabled by default.
     *
     * @param plugin the plugin that needs a CommandProvider.
     * @param commandPrefix the base part of each command which is used by default for each command.
     * @return a new instance of the basic Bukkit implementation of CommandProvider.
     */
    public static BukkitCommandProvider<Plugin> getBukkitCommandProvider(Plugin plugin, String commandPrefix) {
        return new BukkitCommandProvider<Plugin>(plugin, plugin, commandPrefix, true);
    }

    /**
     * Creates a new instance of the basic Bukkit implementation of CommandProvider which can be used for registering
     * and running commands.
     * <br>
     * Queued commands will not be used.
     *
     * @param plugin the plugin that needs a CommandProvider.
     * @param commandPrefix the base part of each command which is used by default for each command.
     * @return a new instance of the basic Bukkit implementation of CommandProvider.
     */
    public static BukkitCommandProvider<Plugin> getBukkitCommandProviderNoQueuedCommands(Plugin plugin, String commandPrefix) {
        return new BukkitCommandProvider<Plugin>(plugin, plugin, commandPrefix, false);
    }

    /**
     * Creates a new instance of the basic Bukkit implementation of CommandProvider which can be used for registering
     * and running commands.
     * <br>
     * Queued commands are enabled by default.
     *
     * @param owner the object that is used for command instantiation.
     * @param plugin the plugin that needs a CommandProvider.
     * @param commandPrefix the base part of each command which is used by default for each command.
     * @return a new instance of the basic Bukkit implementation of CommandProvider.
     */
    public static <O> BukkitCommandProvider<O> getBukkitCommandProvider(O owner, Plugin plugin, String commandPrefix) {
        return new BukkitCommandProvider<O>(owner, plugin, commandPrefix, true);
    }

    /**
     * Creates a new instance of the basic Bukkit implementation of CommandProvider which can be used for registering
     * and running commands.
     * <br>
     * Queued commands will not be used.
     *
     * @param owner the object that is used for command instantiation.
     * @param plugin the plugin that needs a CommandProvider.
     * @param commandPrefix the base part of each command which is used by default for each command.
     * @return a new instance of the basic Bukkit implementation of CommandProvider.
     */
    public static <O> BukkitCommandProvider<O> getBukkitCommandProviderNoQueuedCommands(O owner, Plugin plugin, String commandPrefix) {
        return new BukkitCommandProvider<O>(owner, plugin, commandPrefix, false);
    }

    private BukkitCommandProvider(O owner, Plugin plugin, String commandPrefix, boolean useQueuedCommands) {
        super(owner, commandPrefix, useQueuedCommands);
        this.plugin = plugin;
        this.commandHandler = new BukkitCommandHandler(this, plugin);

    }

    @NotNull
    @Override
    public String getName() {
        return plugin.getName();
    }

    @NotNull
    @Override
    public File getDataFolder() {
        return plugin.getDataFolder();
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    /** {@inheritDoc} */
    @Override
    public void scheduleQueuedCommandExpiration(@NotNull QueuedCommand queuedCommand) {
        plugin.getServer().getScheduler().runTaskLater(plugin, queuedCommand, queuedCommand.getExpirationDuration() * 20L);
    }
}
