package pluginbase.bukkit.command;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import pluginbase.command.Command;
import pluginbase.command.CommandHandler;
import pluginbase.command.CommandProvider;
import pluginbase.command.QueuedCommand;
import pluginbase.logging.LoggablePlugin;
import pluginbase.messages.messaging.Messaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides a basic implementation of CommandProvider for use with Bukkit plugin's that are not utilizing the
 * Plugin-Bukkit module.
 *
 * @param <P> The plugin's type.
 */
public final class BukkitCommandProvider<P extends CommandProvider & Messaging & LoggablePlugin> implements CommandProvider {

    private final P plugin;
    private final Plugin executor;
    private final String commandPrefix;
    private boolean useQueuedCommands;

    private final BukkitCommandHandler<P> commandHandler;
    private final Map<Class<? extends Command>, List<String>> additionalAliases;

    /**
     * Creates a new instance of the basic Bukkit implementation of CommandProvider which can be used for delegating
     * CommandProvider methods.
     * <p/>
     * Queued commands are enabled by default.
     *
     * @param plugin the plugin that needs a CommandProvider implementation.
     * @param commandPrefix the base part of each command which is used by default for each command.
     * @param <P> the plugin's type.
     * @return a new instance of the basic Bukkit implementation of CommandProvider.
     */
    public static <P extends CommandProvider & Messaging & LoggablePlugin & Plugin> BukkitCommandProvider<P> getBukkitCommandProvider(P plugin, String commandPrefix) {
        return new BukkitCommandProvider<P>(plugin, plugin, commandPrefix, true);
    }

    /**
     * Creates a new instance of the basic Bukkit implementation of CommandProvider which can be used for delegating
     * CommandProvider methods.
     * <p/>
     * Queued commands will not be used.
     *
     * @param plugin the plugin that needs a CommandProvider implementation.
     * @param commandPrefix the base part of each command which is used by default for each command.
     * @param <P> the plugin's type.
     * @return a new instance of the basic Bukkit implementation of CommandProvider.
     */
    public static <P extends CommandProvider & Messaging & LoggablePlugin & Plugin> BukkitCommandProvider<P> getBukkitCommandProviderNoQueuedCommands(P plugin, String commandPrefix) {
        return new BukkitCommandProvider<P>(plugin, plugin, commandPrefix, false);
    }

    /**
     * Creates a new instance of the basic Bukkit implementation of CommandProvider which can be used for delegating
     * CommandProvider methods.
     * <p/>
     * Queued commands are enabled by default.
     *
     * @param pluginBase a proxy object that will implement everything required for providing commands.
     * @param plugin the Bukkit plugin that wants to use the Command module but not implement the required interfaces.
     * @param commandPrefix the base part of each command which is used by default for each command.
     * @param <P> the plugin's type.
     * @return a new instance of the basic Bukkit implementation of CommandProvider.
     */
    public static <P extends CommandProvider & Messaging & LoggablePlugin> BukkitCommandProvider<P> getBukkitCommandProvider(P pluginBase, Plugin plugin, String commandPrefix) {
        return new BukkitCommandProvider<P>(pluginBase, plugin, commandPrefix, true);
    }

    /**
     * Creates a new instance of the basic Bukkit implementation of CommandProvider which can be used for delegating
     * CommandProvider methods.
     * <p/>
     * Queued commands will not be used.
     *
     * @param pluginBase a proxy object that will implement everything required for providing commands.
     * @param plugin the Bukkit plugin that wants to use the Command module but not implement the required interfaces.
     * @param commandPrefix the base part of each command which is used by default for each command.
     * @param <P> the plugin's type.
     * @return a new instance of the basic Bukkit implementation of CommandProvider.
     */
    public static <P extends CommandProvider & Messaging & LoggablePlugin> BukkitCommandProvider<P> getBukkitCommandProviderNoQueuedCommands(P pluginBase, Plugin plugin, String commandPrefix) {
        return new BukkitCommandProvider<P>(pluginBase, plugin, commandPrefix, false);
    }

    private BukkitCommandProvider(P plugin, Plugin executor, String commandPrefix, boolean useQueuedCommands) {
        this.plugin = plugin;
        this.executor = executor;
        this.commandPrefix = commandPrefix;
        this.useQueuedCommands = useQueuedCommands;
        this.commandHandler = new BukkitCommandHandler<P>(plugin, executor);
        this.additionalAliases = new HashMap<Class<? extends Command>, List<String>>();
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public String getCommandPrefix() {
        return commandPrefix;
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
        executor.getServer().getScheduler().runTaskLater(executor, queuedCommand, queuedCommand.getExpirationDuration() * 20L);
    }

    /** {@inheritDoc} */
    @Override
    public boolean useQueuedCommands() {
        return useQueuedCommands;
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public String[] getAdditionalCommandAliases(@NotNull Class<? extends Command> commandClass) {
        List<String> aliases = additionalAliases.get(commandClass);
        return aliases != null ? aliases.toArray(new String[aliases.size()]) : new String[0];
    }

    /**
     * Adds an additional alias to a command.  Any amount may be added by calling this method multiple times.
     * <p/>
     * This must be called before command registration occurs!
     *
     * @param commandClass the command class to add aliases for.
     * @param alias the alias to add.
     */
    @Override
    public void addCommandAlias(@NotNull Class<? extends Command> commandClass, @NotNull String alias) {
        List<String> aliases = additionalAliases.get(commandClass);
        if (aliases == null) {
            aliases = new ArrayList<String>();
            additionalAliases.put(commandClass, aliases);
        }
        aliases.add(alias);
    }
}
