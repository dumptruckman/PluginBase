package pluginbase.command;

import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.logging.PluginLogger;
import pluginbase.messages.messaging.Messager;
import pluginbase.messages.messaging.MessagerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public abstract class AbstractCommandProvider<P> implements CommandProvider<P> {


    private final P plugin;
    private final String commandPrefix;
    private final boolean useQueuedCommands;
    private final Map<Class<? extends Command>, List<String>> additionalAliases;

    private PluginLogger pluginLogger;
    @Nullable
    private Messager messager;

    protected AbstractCommandProvider(P plugin, String commandPrefix, boolean useQueuedCommands) {
        this.plugin = plugin;
        this.commandPrefix = commandPrefix;
        this.useQueuedCommands = useQueuedCommands;
        this.additionalAliases = new HashMap<>();
    }

    /**
     * Gets the prefix for commands used by this CommandProvider.
     * <br>
     * This prefix will be use on all command primary aliases but is optional on alternate aliases.
     *
     * @return the prefix for commands used by this CommandProvider.
     */
    @NotNull
    public String getCommandPrefix() {
        return commandPrefix;
    }

    /**
     * Gets the handler object for commands provided by this CommandProvider.
     *
     * @return the handler object for commands provided by this CommandProvider.
     */
    @NotNull
    public abstract CommandHandler getCommandHandler();

    /**
     * Schedules a queued command to be run later in order to deal with it's expiration when left unconfirmed.
     * <br>
     * This method should simply run the QueuedCommand (which implements {@link Runnable}) an amount of seconds later.
     * The amount is specified with {@link pluginbase.command.QueuedCommand#getExpirationDuration()}.
     * <br>
     * This will automatically be called by the command handler when the queued command is used initially in order to
     * schedule its expiration.
     * </p>
     * If {@link #useQueuedCommands()} returns false this method can happily do nothing.
     *
     * @param queuedCommand the queued command to schedule expiration for.
     */
    public abstract void scheduleQueuedCommandExpiration(@NotNull final QueuedCommand queuedCommand);

    /**
     * Whether or not the command provider offers queued commands.
     * <br>
     * This is important to note as it determines whether or not the provider should have a built in confirm command.
     * <br>
     * Simply implement this method to return true to offer queued commands.
     *
     * @return true means this provider offers queued commands and has a built in confirm command.
     */
    public boolean useQueuedCommands() {
        return useQueuedCommands;
    }

    /**
     * Provides additional aliases that commands should use.
     * <br>
     * This is useful for when the CommandProvider does not have access to the Command class in order to add them normally.
     *
     * @return an array of additional command aliases.
     */
    @NotNull
    public String[] getAdditionalCommandAliases(@NotNull Class<? extends Command> commandClass) {
        List<String> aliases = additionalAliases.get(commandClass);
        return aliases != null ? aliases.toArray(new String[aliases.size()]) : new String[0];
    }

    /**
     * Adds an additional alias to a command.  Any amount may be added by calling this method multiple times.
     * <br>
     * This must be called before command registration occurs!
     *
     * @param commandClass the command class to add aliases for.
     * @param alias the alias to add.
     */
    public void addCommandAlias(@NotNull Class<? extends Command> commandClass, @NotNull String alias) {
        List<String> aliases = additionalAliases.get(commandClass);
        if (aliases == null) {
            aliases = new ArrayList<String>();
            additionalAliases.put(commandClass, aliases);
        }
        aliases.add(alias);
    }

    /**
     * Gets the plugin that this command provider belongs to.
     *
     * @return the plugin that this command provider belongs to.
     */
    public P getPlugin() {
        return plugin;
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public PluginLogger getLog() {
        if (pluginLogger == null) {
            pluginLogger = PluginLogger.getLogger(this);
        }
        return pluginLogger;
    }

    /** {@inheritDoc} */
    @Override
    public void loadMessages(@NotNull ConfigurationLoader loader, @NotNull Locale locale) {
        messager = MessagerFactory.createMessager(this, loader, locale);
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public Messager getMessager() {
        if (messager == null) {
            throw new IllegalStateException("No Messager has been loaded for this command provider!");
        }
        return messager;
    }
}
