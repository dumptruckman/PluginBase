package pluginbase.bukkit.command;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.bukkit.messaging.BukkitMessager;
import pluginbase.command.Command;
import pluginbase.command.CommandHandler;
import pluginbase.command.CommandProvider;
import pluginbase.command.QueuedCommand;
import pluginbase.logging.PluginLogger;
import pluginbase.messages.LocalizablePlugin;
import pluginbase.messages.messaging.Messager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Provides a basic implementation of CommandProvider for use with Bukkit plugin's that are not utilizing the
 * Plugin-Bukkit module.
 * <p/>
 * This implementation will provide a {@link pluginbase.logging.PluginLogger} and a {@link pluginbase.messages.messaging.Messager}
 * for your plugin so you will not need to create these on your own.
 */
public final class BukkitCommandProvider<O> implements CommandProvider<O> {

    private final O owner;
    private final Plugin plugin;
    private final String commandPrefix;
    private final boolean useQueuedCommands;

    private PluginLogger pluginLogger;
    @Nullable
    private BukkitMessager messager;
    private final BukkitCommandHandler commandHandler;
    private final Map<Class<? extends Command>, List<String>> additionalAliases;

    /**
     * Creates a new instance of the basic Bukkit implementation of CommandProvider which can be used for registering
     * and running commands.
     * <p/>
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
     * <p/>
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
     * <p/>
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
     * <p/>
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
        this.owner = owner;
        this.plugin = plugin;
        this.commandPrefix = commandPrefix;
        this.useQueuedCommands = useQueuedCommands;
        this.commandHandler = new BukkitCommandHandler(this, plugin);
        this.additionalAliases = new HashMap<Class<? extends Command>, List<String>>();
    }

    @NotNull
    @Override
    public PluginLogger getLog() {
        if (pluginLogger == null) {
            pluginLogger = PluginLogger.getLogger(this);
        }
        return pluginLogger;
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

    @NotNull
    @Override
    public Messager getMessager() {
        if (messager == null) {
            throw new IllegalStateException("No Messager has been loaded for this command provider!");
        }
        return messager;
    }

    /** {@inheritDoc} */
    @Override
    public void loadMessages(@NotNull File languageFile, @NotNull Locale locale) {
        messager = BukkitMessager.loadMessagerWithMessages(this, languageFile, locale);
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
        plugin.getServer().getScheduler().runTaskLater(plugin, queuedCommand, queuedCommand.getExpirationDuration() * 20L);
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

    /** {@inheritDoc} */
    @Override
    public void addCommandAlias(@NotNull Class<? extends Command> commandClass, @NotNull String alias) {
        List<String> aliases = additionalAliases.get(commandClass);
        if (aliases == null) {
            aliases = new ArrayList<String>();
            additionalAliases.put(commandClass, aliases);
        }
        aliases.add(alias);
    }

    /** {@inheritDoc} */
    @Override
    public O getPlugin() {
        return owner;
    }
}
