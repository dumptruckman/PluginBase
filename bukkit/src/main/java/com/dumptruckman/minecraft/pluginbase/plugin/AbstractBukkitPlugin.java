/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.plugin;

import com.dumptruckman.minecraft.pluginbase.config.BaseConfig;
import com.dumptruckman.minecraft.pluginbase.config.SQLConfig;
import com.dumptruckman.minecraft.pluginbase.config.YamlSQLConfig;
import com.dumptruckman.minecraft.pluginbase.database.MySQL;
import com.dumptruckman.minecraft.pluginbase.database.SQLDatabase;
import com.dumptruckman.minecraft.pluginbase.database.SQLite;
import com.dumptruckman.minecraft.pluginbase.entity.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.entity.BukkitCommandSender;
import com.dumptruckman.minecraft.pluginbase.entity.BukkitPlayer;
import com.dumptruckman.minecraft.pluginbase.exception.CommandUsageException;
import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import com.dumptruckman.minecraft.pluginbase.messaging.Messager;
import com.dumptruckman.minecraft.pluginbase.messaging.SimpleMessager;
import com.dumptruckman.minecraft.pluginbase.permission.BukkitPermFactory;
import com.dumptruckman.minecraft.pluginbase.permission.PermFactory;
import com.dumptruckman.minecraft.pluginbase.plugin.command.BukkitCommandHandler;
import com.dumptruckman.minecraft.pluginbase.plugin.command.CommandMessages;
import com.dumptruckman.minecraft.pluginbase.plugin.command.builtin.DebugCommand;
import com.dumptruckman.minecraft.pluginbase.plugin.command.builtin.InfoCommand;
import com.dumptruckman.minecraft.pluginbase.plugin.command.builtin.ReloadCommand;
import com.dumptruckman.minecraft.pluginbase.plugin.command.builtin.VersionCommand;
import com.dumptruckman.minecraft.pluginbase.server.BukkitServerInterface;
import com.dumptruckman.minecraft.pluginbase.server.ServerInterface;
import com.sk89q.minecraft.util.commands.CommandException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

/**
 * An implemention of PluginBase made for Bukkit Plugin that automatically takes care of many of the setup steps
 * required in a plugin.
 */
public abstract class AbstractBukkitPlugin<C extends BaseConfig> extends JavaPlugin implements BukkitPlugin<C> {

    private final BukkitPluginInfo pluginInfo = new BukkitPluginInfo(this);

    private ServerInterface serverInterface;
    private C config = null;
    private Messager messager = null;
    private File serverFolder = null;
    private BukkitCommandHandler commandHandler = null;
    private SQLDatabase db = null;
    private Metrics metrics = null;

    /**
     * The configuration details for SQL database is contained here.  If not initialized, no SQL config file will
     * be created.
     */
    protected SQLConfig sqlConfig = null;

    static {
        // Statically initializes the members of the command language class.
        CommandMessages.init();
    }

    @Override
    public final void onLoad() {
        // Setup the server interface.
        this.serverInterface = new BukkitServerInterface(getServer());
        // Initialize our logging.
        Logging.init(this);
        // Setup a permission factory for Bukkit permissions.
        PermFactory.registerPermissionFactory(this, BukkitPermFactory.class);
        // Loads the configuration.
        setupConfig();
        // Setup the plugin messager.
        setupMessager();
        // Setup the command handler.
        this.commandHandler = new BukkitCommandHandler(this);

        // Call the method implementers should use in place of onLoad().
        onPluginLoad();
    }

    /**
     * Override this method to perform actions that should be performed on the normal bukkit {@link #onLoad()}.
     */
    protected void onPluginLoad() { }

    @Override
    public final void onDisable() {
        // Call the method implementers should use in place of onDisable().
        onPluginDisable();

        // Shut down our logging.
        Logging.shutdown();
    }

    /**
     * Override this method to perform actions that should be performed on the normal bukkit {@link #onDisable()}.
     */
    protected void onPluginDisable() { }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void onEnable() {
        // Setup plugin metrics.
        setupMetrics();
        // Sets up the plugin database if configured.
        setupDB();
        // Loads the configuration.
        setupConfig();
        // Setup the plugin messager.
        setupMessager();
        // Setup our base commands.
        setupCommands();

        // Do any important first run stuff here.
        if (config().get(BaseConfig.FIRST_RUN)) {
            firstRun();
            config().set(BaseConfig.FIRST_RUN, false);
            config().flush();
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
            Logging.warning("Error while enabling plugin metrics: " + e.getMessage());
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
            if (this.sqlConfig.get(SQLConfig.DB_TYPE).equalsIgnoreCase("mysql")) {
                try {
                    this.db = new MySQL(this.sqlConfig.get(SQLConfig.DB_HOST),
                            this.sqlConfig.get(SQLConfig.DB_PORT),
                            this.sqlConfig.get(SQLConfig.DB_DATABASE),
                            this.sqlConfig.get(SQLConfig.DB_USER),
                            this.sqlConfig.get(SQLConfig.DB_PASS));
                } catch (ClassNotFoundException e) {
                    Logging.severe("Your server does not support MySQL!");
                }
            } else {
                try {
                    this.db = new SQLite(new File(getDataFolder(), "data"));
                } catch (ClassNotFoundException e) {
                    Logging.severe("Your server does not support SQLite!");
                }
            }
        }
    }

    private void setupConfig() {
        try {
            if (this.config == null) {
                this.config = newConfigInstance();
            } else {
                this.config.reload();
            }
            Logging.fine("Loaded config file!");
        } catch (Exception e) {  // Catch errors loading the config file and exit out if found.
            Logging.severe("Error loading config file!");
            Logging.getLogger().log(Level.SEVERE, "Exception: ", e);
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        Logging.setDebugLevel(config().get(BaseConfig.DEBUG_MODE));
    }

    private void setupMessager() {
        this.messager = new SimpleMessager(this);
        this.messager.setLocale(config().get(BaseConfig.LOCALE));
        this.messager.setLanguage(config().get(BaseConfig.LANGUAGE_FILE));
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
        setupConfig();
        setupMessager();
    }

    /**
     * Override this method if you'd like to return any special information for your plugin when using the version
     * command.
     *
     * @return A list of strings to appears in the version information.  If this is not overriden, null is returned.
     */
    public List<String> dumpVersionInfo() {
        return null;
    }

    private void setupCommands() {
        getCommandHandler().registerCommand(InfoCommand.class);
        getCommandHandler().registerCommand(DebugCommand.class);
        getCommandHandler().registerCommand(ReloadCommand.class);
        getCommandHandler().registerCommand(VersionCommand.class);
        //getCommandHandler().registerCommand(new HelpCommand<AbstractBukkitPlugin>(this));
        //getCommandHandler().registerCommand(new ConfirmCommand<AbstractBukkitPlugin>(this));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
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
            if (e.getMessage() != null) {
                getMessager().message(wrappedSender, e.getMessage());
            }
            if (e instanceof CommandUsageException) {
                getMessager().message(wrappedSender, ((CommandUsageException) e).getUsage());
            }
        }
        return true;
    }

    @Override
    public BukkitCommandHandler getCommandHandler() {
        return this.commandHandler;
    }

    @Override
    public C config() {
        return this.config;
    }

    @Override
    public Messager getMessager() {
        return this.messager;
    }

    @Override
    public File getServerFolder() {
        if (this.serverFolder == null) {
            this.serverFolder = new File(System.getProperty("user.dir"));
        }
        return this.serverFolder;
    }

    @Override
    public void setServerFolder(File newServerFolder) {
        if (!newServerFolder.isDirectory()) {
            throw new IllegalArgumentException("That's not a folder!");
        }

        this.serverFolder = newServerFolder;
    }

    @Override
    public SQLDatabase getDB() {
        if (sqlConfig == null) {
            throw new IllegalStateException("SQL database not configured for this plugin!");
        }
        return db;
    }

    @Override
    public SQLConfig sqlConfig() {
        return sqlConfig;
    }

    @Override
    public abstract String getCommandPrefix();

    /**
     * Implement this method with by returning a new instance of your Configuration object.
     *
     * {@link com.dumptruckman.minecraft.pluginbase.properties.AbstractYamlProperties} is given to make implementing your
     * config easy.
     *
     * @return A new config instance.
     * @throws Exception If any problems occur when creating a new config instance.
     */
    protected abstract C newConfigInstance() throws Exception;

    /**
     * Implement this method to tell PluginBase whether to use a database configuration file or not.
     *
     * @return True to automatically set up a database.
     */
    protected abstract boolean useDatabase();

    private void initDatabase() {
        try {
            sqlConfig = new YamlSQLConfig(this, new File(getDataFolder(), "db_config.yml"));
        } catch (IOException e) {
            Logging.severe("Could not create db_config.yml! " + e.getMessage());
        }
    }

    @Override
    public Metrics getMetrics() {
        return metrics;
    }

    @Override
    public BasePlayer wrapPlayer(Player player) {
        return new BukkitPlayer(player);
    }

    @Override
    public BasePlayer wrapSender(CommandSender sender) {
        if (sender instanceof Player) {
            return wrapPlayer((Player) sender);
        }
        return new BukkitCommandSender(sender);
    }

    @Override
    public PluginInfo getPluginInfo() {
        return pluginInfo;
    }

    @Override
    public ServerInterface getServerInterface() {
        return serverInterface;
    }

    protected void setPermissionName(final String name) {
        this.pluginInfo.setPermissionName(name);
    }
}
