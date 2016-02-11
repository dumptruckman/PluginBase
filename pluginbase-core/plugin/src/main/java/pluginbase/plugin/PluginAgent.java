package pluginbase.plugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.command.Command;
import pluginbase.command.CommandException;
import pluginbase.command.CommandInfo;
import pluginbase.command.CommandProvider;
import pluginbase.command.CommandUsageException;
import pluginbase.config.datasource.DataSource;
import pluginbase.config.datasource.gson.GsonDataSource;
import pluginbase.config.datasource.hocon.HoconDataSource;
import pluginbase.config.datasource.json.JsonDataSource;
import pluginbase.config.datasource.yaml.YamlDataSource;
import pluginbase.config.properties.Properties;
import pluginbase.debugsession.DebugSessionManager;
import pluginbase.jdbc.DatabaseSettings;
import pluginbase.jdbc.JdbcAgent;
import pluginbase.logging.PluginLogger;
import pluginbase.messages.Messages;
import pluginbase.messages.PluginBaseException;
import pluginbase.messages.messaging.SendablePluginBaseException;
import pluginbase.minecraft.BasePlayer;
import pluginbase.plugin.command.builtin.BuiltInCommand;
import pluginbase.plugin.command.builtin.ConfirmCommand;
import pluginbase.plugin.command.builtin.DebugCommand;
import pluginbase.plugin.command.builtin.DebugSessionCommand;
import pluginbase.plugin.command.builtin.ReloadCommand;
import pluginbase.plugin.command.builtin.VersionCommand;

import java.io.File;
import java.io.IOException;
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
    private final CommandProvider commandProvider;
    private final PluginBase<P> pluginBase;

    private boolean loaded = false;

    private ConfigType configType = ConfigType.HOCON;
    private File configFile;
    private DataSource configDataSource = null;
    private File sqlConfigFile;
    private DataSource sqlConfigDataSource = null;

    @Nullable
    private Callable<? extends Settings> settingsCallable;

    private Set<Class> messageClassesToRegister = new HashSet<Class>();
    private List<Class<? extends Command>> commandClassesToRegister = new LinkedList<Class<? extends Command>>();

    @Nullable
    private Runnable firstRunRunnable = null;

    @Nullable
    private Callable<JdbcAgent> jdbcAgentCallable = null;

    @Nullable
    private VersionInfoModifier versionInfoModifier = null;

    private Map<Class<? extends Command>, List<String>> additionalCommandAliases = new HashMap<Class<? extends Command>, List<String>>();

    private String permissionPrefix;
    String languageFilePrefix = "messages";

    protected PluginAgent(@NotNull Class<P> pluginClass, @NotNull P plugin, @NotNull CommandProvider commandProvider) {
        this.pluginClass = pluginClass;
        this.plugin = plugin;
        this.commandProvider = commandProvider;
        this.pluginBase = new PluginBase<P>(this);

        // Add initial commands for registration
        //registerCommand(InfoCommand.class); // TODO Unused currently... may implement later.
        _registerCommand(DebugCommand.class);
        _registerCommand(ReloadCommand.class);
        _registerCommand(VersionCommand.class);
        _registerCommand(DebugSessionCommand.class);
        if (commandProvider.useQueuedCommands()) {
            _registerCommand(ConfirmCommand.class);
        }
    }

    /**
     * Sets the language filename prefix. That is, the text that prepends the locale tag. The default is "language".
     * This must be called before enabling PluginBase or before reloading.
     *
     * @param languageFilePrefix The new language file prefix
     */
    public void setLanguageFilePrefix(@NotNull String languageFilePrefix) {
        this.languageFilePrefix = languageFilePrefix;
    }

    public String getLanguageFileName() {
        return this.languageFilePrefix + "_" + getPluginBase().getSettings().getLocale().toString()
                + getConfigType().getFileExtension();
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

    protected ConfigType getConfigType() {
        return configType;
    }

    public void setConfigType(@NotNull ConfigType configType) {
        this.configType = configType;
    }

    protected File getConfigFile() throws PluginBaseException {
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                throw new SendablePluginBaseException(Messages.EXCEPTION.bundle(e), e);
            }
        }
        return configFile;
    }

    protected File getSqlConfigFile() throws PluginBaseException {
        if (!sqlConfigFile.exists()) {
            sqlConfigFile.getParentFile().mkdirs();
            try {
                sqlConfigFile.createNewFile();
            } catch (IOException e) {
                throw new SendablePluginBaseException(Messages.EXCEPTION.bundle(e), e);
            }
        }
        return sqlConfigFile;
    }

    public void loadPluginBase() {
        // Setup config file.
        if (getConfigType() == ConfigType.HOCON) {
            configFile = new File(getDataFolder(), "plugin.conf");
        } else {
            configFile = new File(getDataFolder(), "config" + getConfigType().getFileExtension());
        }
        if (getConfigType() == ConfigType.HOCON) {
            sqlConfigFile = new File(getDataFolder(), "database.conf");
        } else {
            sqlConfigFile = new File(getDataFolder(), "database_config" + getConfigType().getFileExtension());
        }

        getPluginBase().onLoad();

        // Setup messages
        Messages.registerMessages(getCommandProvider(), Properties.class);
        Messages.registerMessages(getCommandProvider(), DebugSessionManager.class);
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

    public void setVersionInfoModifier(@Nullable VersionInfoModifier versionInfoModifier) {
        this.versionInfoModifier = versionInfoModifier;
    }

    @NotNull
    List<String> getModifiedVersionInfo(List<String> versionInfo) {
        try {
            return versionInfoModifier != null ? versionInfoModifier.modifyVersionInfo(versionInfo) : versionInfo;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Registers the {@link pluginbase.messages.Message} objects in the given class and any subclasses for usage
     * in your plugin.
     *
     * The {@link pluginbase.messages.Message} objects in the given class <b>must</b> be static and final.
     *
     * @param messageContainerClass The class that contains your {@link pluginbase.messages.Message} objects.
     */
    public void registerMessages(@NotNull Class messageContainerClass) {
        if (messageClassesToRegister == null) {
            throw new IllegalStateException("Message registration must be done before loadPlugin is called.");
        }
        messageClassesToRegister.add(messageContainerClass);
    }

    void registerMessages() {
        for (Class messageContainerClass : messageClassesToRegister) {
            Messages.registerMessages(getCommandProvider(), messageContainerClass);
        }
        messageClassesToRegister = null;
    }

    /**
     * Register the given command class as a command for this plugin.
     *
     * @param commandClass the command class to register as a command for this plugin.
     */
    public final void registerCommand(Class<? extends Command<P>> commandClass) {
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
            if (BuiltInCommand.class.isAssignableFrom(clazz)) {
                getPluginBase().getCommandHandler().registerCommand(getPluginBase(), clazz);
            } else {
                getPluginBase().getCommandHandler().registerCommand(clazz);
            }
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
     * This must be called before command registration occurs!
     *
     * @param commandClass the command class to add aliases for.
     * @param alias the alias to add.
     */
    public final void addCommandAlias(@NotNull Class<? extends Command> commandClass, @NotNull String alias) {
        getCommandProvider().addCommandAlias(commandClass, alias);
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
    CommandProvider getCommandProvider() {
        return commandProvider;
    }

    @NotNull
    protected abstract PluginInfo getPluginInfo();

    @NotNull
    protected abstract File getDataFolder();

    @NotNull
    protected Settings loadSettings() {
        Settings defaults = getDefaultSettings();
        Settings settings = defaults;
        try {
            if (configDataSource == null) {
                switch (getConfigType()) {
                    case HOCON:
                        configDataSource = HoconDataSource.builder().setCommentsEnabled(true).setFile(getConfigFile()).build();
                        break;
                    case YAML:
                        configDataSource = YamlDataSource.builder().setCommentsEnabled(false).setFile(getConfigFile()).build();
                        break;
                    case JSON:
                        configDataSource = JsonDataSource.builder().setFile(getConfigFile()).build();
                        break;
                    case GSON:
                        configDataSource = GsonDataSource.builder().setFile(getConfigFile()).build();
                        break;
                    default:
                        configDataSource = HoconDataSource.builder().setCommentsEnabled(true).setFile(getConfigFile()).build();
                        break;
                }
            }
            settings = configDataSource.loadToObject(defaults);
            if (settings == null) {
                settings = defaults;
            }
            configDataSource.save(settings);
            getLog().fine("Loaded config file!");
        } catch (PluginBaseException e) {  // Catch errors loading the config file and exit out if found.
            getLog().severe("Error loading config file!");
            e.printStackTrace();
            disablePlugin();
        }
        getLog().setDebugLevel(getLog().getDebugLevel());
        return settings;
    }

    @NotNull
    public DatabaseSettings loadDatabaseSettings(@NotNull DatabaseSettings defaults) {
        DatabaseSettings settings = defaults;
        try {
            if (sqlConfigDataSource == null) {
                switch (getConfigType()) {
                    case HOCON:
                        sqlConfigDataSource = HoconDataSource.builder().setCommentsEnabled(true).setFile(getSqlConfigFile()).build();
                        break;
                    case YAML:
                        sqlConfigDataSource = YamlDataSource.builder().setCommentsEnabled(false).setFile(getSqlConfigFile()).build();
                        break;
                    case JSON:
                        sqlConfigDataSource = JsonDataSource.builder().setFile(getSqlConfigFile()).build();
                        break;
                    case GSON:
                        sqlConfigDataSource = GsonDataSource.builder().setFile(getSqlConfigFile()).build();
                        break;
                    default:
                        sqlConfigDataSource = HoconDataSource.builder().setCommentsEnabled(true).setFile(getSqlConfigFile()).build();
                        break;
                }
            }
            settings = sqlConfigDataSource.loadToObject(defaults);
            if (settings == null) {
                settings = defaults;
            }
            sqlConfigDataSource.save(settings);
            getLog().fine("Loaded db config file!");
        } catch (PluginBaseException e) {
            getLog().severe("Could not create db_config.yml!");
            e.printStackTrace();
        }
        return settings;
    }

    protected abstract void disablePlugin();

    protected abstract ServerInterface getServerInterface();

    protected void saveSettings() throws SendablePluginBaseException {
        configDataSource.save(getPluginBase().getSettings());
    }

    @Override
    public String toString() {
        return "PluginAgent{" +
                "plugin=" + plugin +
                ", pluginClass=" + pluginClass +
                ", loaded=" + loaded +
                ", permissionPrefix='" + permissionPrefix + '\'' +
                '}';
    }
}
