package pluginbase.plugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.command.Command;
import pluginbase.command.CommandException;
import pluginbase.command.CommandHandler;
import pluginbase.command.CommandInfo;
import pluginbase.command.CommandUsageException;
import pluginbase.command.QueuedCommand;
import pluginbase.config.properties.Properties;
import pluginbase.jdbc.DatabaseSettings;
import pluginbase.jdbc.JdbcAgent;
import pluginbase.logging.PluginLogger;
import pluginbase.messages.Messages;
import pluginbase.messages.messaging.Messager;
import pluginbase.messages.messaging.SendablePluginBaseException;
import pluginbase.minecraft.BasePlayer;
import pluginbase.plugin.command.PluginCommand;
import pluginbase.plugin.command.QueuedPluginCommand;
import pluginbase.plugin.command.builtin.DebugCommand;
import pluginbase.plugin.command.builtin.ReloadCommand;
import pluginbase.plugin.command.builtin.VersionCommand;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

public abstract class PluginAgent<P> {

    private final P plugin;
    private final Class<P> pluginClass;
    private final PluginBase<P> pluginBase;

    private boolean loaded = false;

    private File configFile;
    private File sqlConfigFile;

    @NotNull
    private final String commandPrefix;
    @Nullable
    private Callable<? extends Settings> settingsCallable;

    private Set<Class> messageClassesToRegister = new HashSet<Class>();
    private List<Class<? extends Command>> commandClassesToRegister = new LinkedList<Class<? extends Command>>();

    private boolean queuedCommandsEnabled = true;

    @Nullable
    private Runnable firstRunRunnable = null;

    @Nullable
    private Callable<JdbcAgent> jdbcAgentCallable = null;

    @Nullable
    private Callable<List<String>> additionalVersionInfo = null;

    private Map<Class<? extends Command>, List<String>> additionalCommandAliases = new HashMap<Class<? extends Command>, List<String>>();

    private String permissionPrefix;

    protected PluginAgent(@NotNull Class<P> pluginClass, @NotNull P plugin, @NotNull String commandPrefix) {
        this.pluginClass = pluginClass;
        this.plugin = plugin;
        this.commandPrefix = commandPrefix;
        this.pluginBase = new PluginBase<P>(this);

        // Add initial commands for registration
        //registerCommand(InfoCommand.class); // TODO Unused currently... may implement later.
        _registerCommand(DebugCommand.class);
        _registerCommand(ReloadCommand.class);
        _registerCommand(VersionCommand.class);
    }

    protected P getPlugin() {
        return plugin;
    }

    public PluginBase<P> getPluginBase() {
        return pluginBase;
    }

    protected PluginLogger getLog() {
        return getPluginBase().getLog();
    }

    protected File getConfigFile() {
        return configFile;
    }

    protected File getSqlConfigFile() {
        return sqlConfigFile;
    }

    public void loadPluginBase() {
        // Setup config file.
        configFile = new File(getDataFolder(), "config.yml");
        sqlConfigFile = new File(getDataFolder(), "db_config.yml");

        getPluginBase().onLoad();

        // Setup messages
        Messages.registerMessages(getPluginBase(), Properties.class);
        registerMessages();

        loaded = true;
    }

    public void enablePluginBase() {
        if (!loaded) {
            throw new IllegalStateException("PluginBase must be loaded before enabling!");
        }
        getPluginBase().onEnable();
    }

    public void disablePluginBase() {
        getPluginBase().onDisable();
    }

    public void setDefaultSettingsCallable(@NotNull Callable<? extends Settings> settingsCallable) {
        this.settingsCallable = settingsCallable;
    }

    @NotNull
    protected Settings getDefaultSettings() {
        if (settingsCallable == null) {
            return new Settings(getPluginBase());
        } else {
            try {
                return settingsCallable.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setAdditionalVersionInfoCallable(@NotNull Callable<List<String>> additionalVersionInfo) {
        this.additionalVersionInfo = additionalVersionInfo;
    }

    @Nullable
    List<String> getAdditionalVersionInfo() {
        try {
            return additionalVersionInfo != null ? additionalVersionInfo.call() : new ArrayList<String>(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    protected String getCommandPrefix() {
        return commandPrefix;
    }

    public void registerMessage(Class messageContainerClass) {
        if (messageClassesToRegister == null) {
            throw new IllegalStateException("Message registration must be done before loadPlugin is called.");
        }
        messageClassesToRegister.add(messageContainerClass);
    }

    void registerMessages() {
        for (Class messageContainerClass : messageClassesToRegister) {
            Messages.registerMessages(getPluginBase(), messageContainerClass);
        }
        messageClassesToRegister = null;
    }

    public void setQueuedCommandsEnabled(boolean queuedCommandsEnabled) {
        if (commandClassesToRegister == null) {
            throw new IllegalStateException("Queued commands must be set prior to plugin enable");
        }
        if (queuedCommandsEnabled) {
            commandClassesToRegister.add(QueuedCommand.class);
        } else {
            commandClassesToRegister.remove(QueuedCommand.class);
        }
        this.queuedCommandsEnabled = queuedCommandsEnabled;
    }

    public boolean isQueuedCommandsEnabled() {
        return queuedCommandsEnabled;
    }

    /**
     * Register the given command class as a command for this plugin.
     *
     * @param commandClass the command class to register as a command for this plugin.
     */
    public final void registerCommand(Class<? extends PluginCommand<P>> commandClass) {
        _registerCommand(commandClass);
    }

    /**
     * Register the given command class as a command for this plugin.
     *
     * @param commandClass the command class to register as a command for this plugin.
     */
    public final void registerQueuedCommand(Class<? extends QueuedPluginCommand<P>> commandClass) {
        _registerCommand(commandClass);
    }

    private void _registerCommand(Class<? extends Command> commandClass) {
        if (commandClass.getAnnotation(CommandInfo.class) == null) {
            throw new IllegalArgumentException("Command class must be annotated with " + CommandInfo.class);
        }
        if (commandClassesToRegister == null) {
            getPluginBase().getCommandHandler().registerCommand(commandClass);
        } else {
            commandClassesToRegister.add(commandClass);
        }
    }

    void registerCommands() {
        for (Class<? extends Command> clazz : commandClassesToRegister) {
            getPluginBase().getCommandHandler().registerCommand(clazz);
        }
        commandClassesToRegister = null;
    }

    /**
     * Retrieves the list of commands that will be registered after {@link #registerCommands()} has been called.
     * <p/>
     * This will contain the default commands.  You may remove commands from this list if you do not wish them to be registered.
     * <p/>
     * After {@link #registerCommands()} has been called this method will always return null.
     * <p/>
     * Ordering of commands can matter.  If you have a parent command, "test", and you want a child command, "test help",
     * you must ensure that "test" is in this list ahead of "test help" or "test" will not work 100%.
     *
     * @return The commands to be registered or null if initial command registration has already occurred.
     */
    @Nullable
    public final List<Class<? extends Command>> getCommandClassesToRegister() {
        return commandClassesToRegister;
    }

    public final void setFirstRunTask(@NotNull Runnable runnable) {
        firstRunRunnable = runnable;
    }

    void firstRun() {
        if (firstRunRunnable != null) {
            firstRunRunnable.run();
        }
    }

    public void setJdbcAgentCallable(@Nullable Callable<JdbcAgent> jdbcAgentCallable) {
        this.jdbcAgentCallable = jdbcAgentCallable;
    }

    JdbcAgent getJdbcAgent() {
        if (jdbcAgentCallable != null) {
            try {
                return jdbcAgentCallable.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    /**
     * Adds an additional alias to a command.  Any amount may be added by calling this method multiple times.
     * <p/>
     * This must be called before the command is registered!
     *
     * @param commandClass the class to add aliases for.
     * @param alias the alias to add.
     */
    public final void addAdditionalCommandAlias(@NotNull Class<? extends Command> commandClass, @NotNull String alias) {
        List<String> aliases = additionalCommandAliases.get(commandClass);
        if (aliases == null) {
            aliases = new ArrayList<String>();
            additionalCommandAliases.put(commandClass, aliases);
        }
        aliases.add(alias);
    }

    @NotNull
    String[] getAdditionalCommandAliases(Class<? extends Command> commandClass) {
        List<String> aliases = additionalCommandAliases.get(commandClass);
        return aliases != null ? aliases.toArray(new String[aliases.size()]) : new String[0];
    }

    public final boolean callCommand(@NotNull BasePlayer sender, @NotNull String commandName, String[] args) {
        String[] allArgs = new String[args.length + 1];
        allArgs[0] = commandName;
        System.arraycopy(args, 0, allArgs, 1, args.length);
        try {
            return getPluginBase().getCommandHandler().locateAndRunCommand(sender, allArgs);
        } catch (CommandException e) {
            e.sendException(getPluginBase().getMessager(), sender);
            if (e instanceof CommandUsageException) {
                for (final String usageString : ((CommandUsageException) e).getUsage()) {
                    sender.sendMessage(usageString);
                }
            }
        }
        return true;
    }

    @NotNull
    String getPermissionPrefix() {
        return permissionPrefix == null ? getPluginInfo().getName().toLowerCase() : permissionPrefix;
    }

    public void setPermissionPrefix(@Nullable String permissionPrefix) {
        this.permissionPrefix = permissionPrefix;
    }

    @NotNull
    Class<P> getPluginClass() {
        return pluginClass;
    }

    @NotNull
    protected abstract PluginInfo getPluginInfo();

    @NotNull
    protected abstract File getDataFolder();

    @NotNull
    protected abstract Settings loadSettings();

    @NotNull
    public abstract DatabaseSettings loadDatabaseSettings(@NotNull DatabaseSettings defaults);

    protected abstract CommandHandler<PluginBase> getNewCommandHandler();

    protected abstract Messager getNewMessager(Settings.Language languageSettings);

    protected abstract void disablePlugin();

    protected abstract ServerInterface getServerInterface();

    protected abstract void saveSettings() throws SendablePluginBaseException;
}
