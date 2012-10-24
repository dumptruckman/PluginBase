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
import com.dumptruckman.minecraft.pluginbase.locale.CommandMessages;
import com.dumptruckman.minecraft.pluginbase.locale.Messager;
import com.dumptruckman.minecraft.pluginbase.locale.SimpleMessager;
import com.dumptruckman.minecraft.pluginbase.permission.BukkitPermFactory;
import com.dumptruckman.minecraft.pluginbase.permission.PermFactory;
import com.dumptruckman.minecraft.pluginbase.plugin.command.BukkitCommandHandler;
import com.dumptruckman.minecraft.pluginbase.plugin.command.builtin.DebugCommand;
import com.dumptruckman.minecraft.pluginbase.plugin.command.builtin.InfoCommand;
import com.dumptruckman.minecraft.pluginbase.plugin.command.builtin.ReloadCommand;
import com.dumptruckman.minecraft.pluginbase.plugin.command.builtin.VersionCommand;
import com.dumptruckman.minecraft.pluginbase.server.BukkitServerInterface;
import com.dumptruckman.minecraft.pluginbase.server.ServerInterface;
import com.dumptruckman.minecraft.pluginbase.util.Logging;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Main plugin class for dumptruckman's Plugin Template.
 */
public abstract class AbstractBukkitPlugin<C extends BaseConfig> extends JavaPlugin implements BukkitPlugin<C> {

    private C config = null;
    private Messager messager = null;
    private File serverFolder = null;
    private BukkitCommandHandler commandHandler = null;
    private SQLDatabase db = null;
    private Metrics metrics;

    protected SQLConfig sqlConfig = null;

    private final PluginInfo pluginInfo;
    private ServerInterface serverInterface;

    public AbstractBukkitPlugin() {
        this.pluginInfo = new BukkitPluginInfo(this);
    }

    public void preDisable() {

    }

    @Override
    public void onLoad() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void onDisable() {
        preDisable();
        Logging.close();
    }

    public void preEnable() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void onEnable() {
        this.serverInterface = new BukkitServerInterface(getServer());
        preEnable();
        PermFactory.registerPermissionFactory(this, BukkitPermFactory.class);
        CommandMessages.init();
        Logging.init(this);
        setupMetrics();

        reloadConfig();

        this.commandHandler = new BukkitCommandHandler(this);
        // Register Commands
        _registerCommands();
        //getServer().getPluginManager().registerEvents(new PreProcessListener(this), this);

        postEnable();
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

    public void postEnable() {
        
    }

    public void firstRun() {
        
    }

    public void preReload() {

    }

    /**
     * Nulls the config object and reloads a new one.
     */
    public final void reloadConfig() {
        preReload();

        if (db != null) {
            db.shutdown();
        }
        if (sqlConfig != null) {
            initDatabase();
            if (sqlConfig.get(SQLConfig.DB_TYPE).equalsIgnoreCase("mysql")) {
                try {
                    db = new MySQL(sqlConfig.get(SQLConfig.DB_HOST),
                            sqlConfig.get(SQLConfig.DB_PORT),
                            sqlConfig.get(SQLConfig.DB_DATABASE),
                            sqlConfig.get(SQLConfig.DB_USER),
                            sqlConfig.get(SQLConfig.DB_PASS));
                } catch (ClassNotFoundException e) {
                    Logging.severe("Your server does not support MySQL!");
                }
            } else {
                try {
                    db = new SQLite(new File(getDataFolder(), "data"));
                } catch (ClassNotFoundException e) {
                    Logging.severe("Your server does not support SQLite!");
                }
            }
        }


        // Loads the configuration
        try {
            if (this.config == null) {
                this.config = newConfigInstance();
            } else {
                this.config.reload();
            }
            Logging.fine("Loaded config file!");
        } catch (IOException e) {  // Catch errors loading the config file and exit out if found.
            Logging.severe("Error loading config file!");
            Logging.severe(e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        this.messager = new SimpleMessager(this);
        this.messager.setLocale(config().get(BaseConfig.LOCALE));
        this.messager.setLanguage(config().get(BaseConfig.LANGUAGE_FILE));
        
        Logging.setDebugLevel(config().get(BaseConfig.DEBUG_MODE));

        // Do any import first run stuff here.
        if (config().get(BaseConfig.FIRST_RUN)) {
            Logging.info("First run!");
            firstRun();
            config().set(BaseConfig.FIRST_RUN, false);
            config().save();
        }
        postReload();
    }

    public void postReload() {

    }

    public List<String> dumpVersionInfo() {
        return null;
    }

    private void _registerCommands() {
        getCommandHandler().registerCommand(InfoCommand.class);
        getCommandHandler().registerCommand(DebugCommand.class);
        getCommandHandler().registerCommand(ReloadCommand.class);
        getCommandHandler().registerCommand(VersionCommand.class);
        //getCommandHandler().registerCommand(new HelpCommand<AbstractBukkitPlugin>(this));
        //getCommandHandler().registerCommand(new ConfirmCommand<AbstractBukkitPlugin>(this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!isEnabled()) {
            sender.sendMessage("This plugin is Disabled!");
            return true;
        }
        String[] allArgs = new String[args.length + 1];
        allArgs[0] = command.getName();
        System.arraycopy(args, 0, allArgs, 1, args.length);
        return getCommandHandler().locateAndRunCommand(wrapSender(sender), allArgs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BukkitCommandHandler getCommandHandler() {
        return this.commandHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public C config() {
        return this.config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Messager getMessager() {
        return this.messager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File getServerFolder() {
        if (this.serverFolder == null) {
            this.serverFolder = new File(System.getProperty("user.dir"));
        }
        return this.serverFolder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setServerFolder(File newServerFolder) {
        if (!newServerFolder.isDirectory())
            throw new IllegalArgumentException("That's not a folder!");

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

    protected abstract C newConfigInstance() throws IOException;

    protected void initDatabase() {
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
}
