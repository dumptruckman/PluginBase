/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.bukkit;

import pluginbase.bukkit.config.BukkitConfiguration;
import pluginbase.bukkit.config.YamlConfiguration;
import pluginbase.bukkit.permission.BukkitPermFactory;
import pluginbase.command.Command;
import pluginbase.command.CommandException;
import pluginbase.command.CommandHandler;
import pluginbase.command.CommandInfo;
import pluginbase.command.CommandUsageException;
import pluginbase.command.QueuedCommand;
import pluginbase.config.SerializationRegistrar;
import pluginbase.config.properties.PropertiesWrapper;
import pluginbase.database.MySQL;
import pluginbase.database.SQLDatabase;
import pluginbase.database.SQLSettings;
import pluginbase.database.SQLite;
import pluginbase.logging.PluginLogger;
import pluginbase.messages.PluginBaseException;
import pluginbase.messages.messaging.SendablePluginBaseException;
import pluginbase.minecraft.BasePlayer;
import pluginbase.permission.PermFactory;
import pluginbase.plugin.PluginBase;
import pluginbase.plugin.PluginInfo;
import pluginbase.plugin.ServerInterface;
import pluginbase.plugin.Settings;
import pluginbase.plugin.Settings.Language;
import pluginbase.plugin.command.builtin.ConfirmCommand;
import pluginbase.plugin.command.builtin.DebugCommand;
import pluginbase.plugin.command.builtin.InfoCommand;
import pluginbase.plugin.command.builtin.ReloadCommand;
import pluginbase.plugin.command.builtin.VersionCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mcstats.Metrics;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

/**
 * An implementation of PluginBase made for Bukkit Plugin that automatically takes care of many of the setup steps
 * required in a plugin.
 */
public abstract class AbstractBukkitPlugin extends JavaPlugin implements BukkitPlugin {

    static {
        PropertiesWrapper.initializePropertyMessages();
        SerializationRegistrar.registerClass(Settings.class);
        SerializationRegistrar.registerClass(Language.class);
    }

    private final BukkitPluginInfo pluginInfo = new BukkitPluginInfo(this);

    private ServerInterface<BukkitPlugin> serverInterface;
    private BukkitMessager messager = null;
    private CommandHandler commandHandler = null;
    private SQLDatabase db = null;
    private Metrics metrics = null;
    private PluginLogger logger;

    private File configFile;
    private BukkitConfiguration config = null;
    private Settings settings = null;

    private File sqlConfigFile;
    private BukkitConfiguration sqlConfig = null;
    private SQLSettings sqlSettings = null;

    /**
     * Override this method if you wish for your permissions to start with something other than the plugin name
     * when using the {@link pluginbase.permission.PermFactory#usePluginName()} method.
     *
     * @return The name to use as a base for permissions.
     */
    @Override
    @NotNull
    public String getPermissionName() {
        return getPluginInfo().getName().toLowerCase();
    }

    /** {@inheritDoc} */
    @Override
    public final void onLoad() {
        // Setup config files
        configFile = new File(getDataFolder(), "config.yml");
        sqlConfigFile = new File(getDataFolder(), "db_config.yml");
        // Setup the server interface.
        this.serverInterface = new BukkitServerInterface(getServer());
        // Initialize our logging.
        logger = PluginLogger.getLogger(this);
        // Setup a permission factory for Bukkit permissions.
        PermFactory.registerPermissionFactory(BukkitPermFactory.class);
        PermFactory.registerPermissionName(PluginBase.class, getPermissionName());
        PermFactory.registerPermissionName(AbstractBukkitPlugin.class, getPermissionName());
        PermFactory.registerPermissionName(getClass(), getPermissionName());
        // Loads the configuration.
        setupConfig();
        // Setup the command handler.
        this.commandHandler = new BukkitCommandHandler(this);
        // Setup messages.
        _registerMessages();

        // Call the method implementers should use in place of onLoad().
        onPluginLoad();
    }

    /**
     * Override this method to perform actions that should be performed on the normal bukkit {@link #onLoad()}.
     */
    protected void onPluginLoad() { }

    /** {@inheritDoc} */
    @Override
    public final void onDisable() {
        // Call the method implementers should use in place of onDisable().
        onPluginDisable();

        // Shut down our logging.
        getLog().shutdown();
    }

    /**
     * Override this method to perform actions that should be performed on the normal bukkit {@link #onDisable()}.
     */
    protected void onPluginDisable() { }

    /** {@inheritDoc} */
    @Override
    public final void onEnable() {
        // Setup plugin metrics.
        setupMetrics();
        // Sets up the plugin database if configured.
        setupDB();
        // Loads the configuration.
        Settings settings = setupConfig();
        // Setup our base commands.
        setupCommands(settings);
        // Setup the plugin messager.
        setupMessager(settings.getLanguageSettings());

        // Do any important first run stuff here.
        if (getSettings().isFirstRun()) {
            firstRun();
            getSettings().setFirstRun(false);
            try {
                saveSettings();
            } catch (PluginBaseException e) {
                e.printStackTrace();
                getLog().severe("Cannot save config on startup.  Terminating plugin.");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
        }

        // Call the method implements should use in place of onEnable().
        onPluginEnable();

        // Start plugin metrics.
        startMetrics();
    }

    private void setupMetrics() {
        try {
            metrics = new Metrics(this);
        } catch (IOException e) {
            getLog().warning("Error while enabling plugin metrics: " + e.getMessage());
            metrics = null;
        }
    }

    private void startMetrics() {
        getMetrics().start();
    }

    private void setupDB() {
        if (this.db != null) {
            this.db.shutdown();
        }
        if (useDatabase()) {
            this.db = null;
            initDatabase();
            if (getSQLSettings().getDatabaseType().equalsIgnoreCase("mysql")) {
                SQLSettings.DatabaseInfo dbInfo = getSQLSettings().getDatabaseInfo();
                try {
                    this.db = new MySQL(dbInfo.getHost(), dbInfo.getPort(), dbInfo.getDatabase(), dbInfo.getUser(), dbInfo.getPass());
                } catch (ClassNotFoundException e) {
                    getLog().severe("Your server does not support MySQL!");
                }
            } else {
                try {
                    this.db = new SQLite(new File(getDataFolder(), "data"));
                } catch (ClassNotFoundException e) {
                    getLog().severe("Your server does not support SQLite!");
                }
            }
        }
    }

    /**
     * This method should be implemented to return the specific config file you would like to use as well as the
     * classes it will utilize.
     *
     * @return a new config instance.
     * @throws PluginBaseException in case anything goes wrong during config initialization/loading.
     */
    @NotNull
    protected Settings getDefaultSettings() {
        return new Settings(this);
    }

    protected File getConfigurationFile() {
        return configFile;
    }

    private Settings setupConfig() {
        try {
            config = BukkitConfiguration.loadYamlConfig(getConfigurationFile());
            ((YamlConfiguration) config).options().comments(true);
            Settings defaults = getDefaultSettings();
            settings = config.getToObject("settings", defaults);
            if (settings == null) {
                settings = defaults;
            }
            getLog().fine("Loaded config file!");
        } catch (PluginBaseException e) {  // Catch errors loading the config file and exit out if found.
            getLog().severe("Error loading config file!");
            e.logException(getLog(), Level.SEVERE);
            Bukkit.getPluginManager().disablePlugin(this);
            return null;
        }
        getLog().setDebugLevel(getSettings().getDebugLevel());
        return settings;
    }

    private void _registerMessages() {
        registerMessages();
    }

    /**
     * Override this method as a place for registering your plugin's localized message classes at the appropriate time.
     */
    protected void registerMessages() { }

    private void setupMessager(Settings.Language languageSettings) {
        this.messager = BukkitMessager.loadMessagerWithMessages(this, new File(getDataFolder(), languageSettings.getLanguageFile()), languageSettings.getLocale());
    }

    /**
     * Override this method to perform actions that should be performed on the normal bukkit {@link #onEnable()}.
     */
    public void onPluginEnable() { }

    /**
     * If your plugin needs to do anything special for the first time it is run, you can override this convenience
     * method.
     */
    public void firstRun() { }

    /**
     * Nulls the config object and reloads a new one.
     */
    public final void reloadConfig() {
        setupDB();
        Settings settings = setupConfig();
        setupMessager(settings.getLanguageSettings());
        onReloadConfig();
    }

    /**
     * This method will be called when {@link #reloadConfig()} is called and after the db, config and messager
     * have been reloaded.
     */
    protected void onReloadConfig() { }

    /**
     * Override this method if you'd like to return any special information for your plugin when using the version
     * command.
     *
     * @return A list of strings to appears in the version information.  If this is not overriden, null is returned.
     */
    @Nullable
    public List<String> dumpVersionInfo() {
        return null;
    }

    private void setupCommands(@NotNull Settings settings) {
        registerCommand(InfoCommand.class);
        registerCommand(DebugCommand.class);
        registerCommand(ReloadCommand.class);
        registerCommand(VersionCommand.class);
        if (useQueuedCommands()) {
            registerCommand(ConfirmCommand.class);
        }
        //getCommandHandler().registerCommand(new HelpCommand<AbstractBukkitPlugin>(this));
        registerCommands();
    }

    /**
     * Called when commands should be registered.
     * <p/>
     * Override this method in order to register commands at the most correct time.  PluginBase will call this method
     * when appropriate.
     * <p/>
     * Use {@link #registerCommand(Class)} to register your command classes.
     */
    protected void registerCommands() { }

    /**
     * Register the given command class as a command for this plugin.
     *
     * @param commandClass the command class to register as a command for this plugin.
     */
    protected final void registerCommand(Class<? extends Command> commandClass) {
        if (commandClass.getAnnotation(CommandInfo.class) == null) {
            throw new IllegalArgumentException("Command class must be annotated with " + CommandInfo.class);
        }
        getCommandHandler().registerCommand(commandClass);
    }

    /** {@inheritDoc} */
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String commandLabel, String[] args) {
        if (!isEnabled()) {
            sender.sendMessage("This plugin is Disabled!");
            return true;
        }
        String[] allArgs = new String[args.length + 1];
        allArgs[0] = command.getName();
        System.arraycopy(args, 0, allArgs, 1, args.length);
        final BasePlayer wrappedSender = wrapSender(sender);
        try {
            return getCommandHandler().locateAndRunCommand(wrappedSender, allArgs);
        } catch (CommandException e) {
            e.sendException(getMessager(), wrappedSender);
            if (e instanceof CommandUsageException) {
                for (final String usageString : ((CommandUsageException) e).getUsage()) {
                    wrappedSender.sendMessage(usageString);
                }
            }
        }
        return true;
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public CommandHandler getCommandHandler() {
        return this.commandHandler;
    }

    @Override
    public BukkitConfiguration getConfig() {
        if (config == null) {
            reloadConfig();
        }
        return config;
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public Settings getSettings() {
        return settings;
    }

    /** {@inheritDoc} */
    @Override
    public void saveConfig() {
        try {
            saveSettings();
        } catch (PluginBaseException e) {
            e.logException(getLog(), Level.SEVERE);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void saveSettings() throws SendablePluginBaseException {
        config.set("settings", settings);
        File configFile = getConfigurationFile();
        try {
            config.save(configFile);
        } catch (IOException e) {
            new PluginBaseException(e).logException(getLog(), Level.WARNING);
        }
        saveSqlSettings();
    }

    private void saveSqlSettings() throws SendablePluginBaseException {
        if (sqlConfig != null) {
            sqlConfig.set("settings", sqlSettings);
            File sqlConfigFile = getSqlConfigFile();
            try {
                sqlConfig.save(sqlConfigFile);
            } catch (IOException e) {
                new PluginBaseException(e).logException(getLog(), Level.WARNING);
            }
        }
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public BukkitMessager getMessager() {
        return this.messager;
    }

    /** {@inheritDoc} */
    @Nullable
    @Override
    public SQLDatabase getDB() {
        return db;
    }

    /** {@inheritDoc} */
    @Nullable
    @Override
    public SQLSettings getSQLSettings() {
        return sqlSettings;
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public abstract String getCommandPrefix();

    /**
     * Implement this method to tell PluginBase whether to use a database configuration file or not.
     *
     * @return True to automatically set up a database.
     */
    protected abstract boolean useDatabase();

    protected File getSqlConfigFile() {
        return sqlConfigFile;
    }

    protected SQLSettings getDefaultSQLSettings() {
        return new SQLSettings();
    }

    private void initDatabase() {
        try {
            SQLSettings defaults = getDefaultSQLSettings();
            sqlConfig = BukkitConfiguration.loadYamlConfig(getSqlConfigFile());
            ((YamlConfiguration) sqlConfig).options().comments(true);
            sqlSettings = sqlConfig.getToObject("settings", defaults);
            if (sqlSettings == null) {
                sqlSettings = defaults;
            }
            getLog().fine("Loaded db config file!");
        } catch (PluginBaseException e) {
            getLog().severe("Could not create db_config.yml!");
            e.logException(getLog(), Level.SEVERE);
        }
    }

    /** {@inheritDoc} */
    @Nullable
    @Override
    public Metrics getMetrics() {
        return metrics;
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public BasePlayer wrapPlayer(@NotNull final Player player) {
        return BukkitTools.wrapPlayer(player);
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public BasePlayer wrapSender(@NotNull final CommandSender sender) {
        return BukkitTools.wrapSender(sender);
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public PluginInfo getPluginInfo() {
        return pluginInfo;
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public ServerInterface<BukkitPlugin> getServerInterface() {
        return serverInterface;
    }

    /** {@inheritDoc} */
    @Override
    public void scheduleQueuedCommandExpiration(@NotNull final QueuedCommand queuedCommand) {
        if (useQueuedCommands()) {
            getServerInterface().runTaskLater(this, queuedCommand, queuedCommand.getExpirationDuration());
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean useQueuedCommands() {
        return true;
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public PluginLogger getLog() {
        return logger;
    }
}
