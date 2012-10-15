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
import com.dumptruckman.minecraft.pluginbase.permission.PermHandler;
import com.dumptruckman.minecraft.pluginbase.plugin.command.ConfirmCommand;
import com.dumptruckman.minecraft.pluginbase.plugin.command.DebugCommand;
import com.dumptruckman.minecraft.pluginbase.plugin.command.HelpCommand;
import com.dumptruckman.minecraft.pluginbase.plugin.command.ReloadCommand;
import com.dumptruckman.minecraft.pluginbase.plugin.command.VersionCommand;
import com.dumptruckman.minecraft.pluginbase.util.Logging;
import com.dumptruckman.minecraft.pluginbase.util.commandhandler.CommandHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Main plugin class for dumptruckman's Plugin Template.
 */
public abstract class AbstractBukkitPlugin<C extends BaseConfig> extends JavaPlugin implements BukkitPlugin<C> {

    private C config = null;
    private Messager messager = null;
    private File serverFolder = null;
    private CommandHandler commandHandler = null;
    private SQLDatabase db = null;
    private Metrics metrics;

    protected SQLConfig sqlConfig = null;

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
        preEnable();
        PermFactory.registerPermissionFactory(this, BukkitPermFactory.class);
        CommandMessages.init();
        Logging.init(this);
        setupMetrics();

        reloadConfig();

        // Register Commands
        _registerCommands();

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
        this.messager = null;
        getMessager();
        
        Logging.setDebugMode(config().get(BaseConfig.DEBUG_MODE));

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
        getCommandHandler().registerCommand(new DebugCommand<AbstractBukkitPlugin>(this));
        getCommandHandler().registerCommand(new ReloadCommand<AbstractBukkitPlugin>(this));
        getCommandHandler().registerCommand(new HelpCommand<AbstractBukkitPlugin>(this));
        getCommandHandler().registerCommand(new VersionCommand<AbstractBukkitPlugin>(this));
        getCommandHandler().registerCommand(new ConfirmCommand<AbstractBukkitPlugin>(this));
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
        List<String> allArgs = new ArrayList<String>(Arrays.asList(args));
        allArgs.add(0, command.getName());
        return this.getCommandHandler().locateAndRunCommand(sender, allArgs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandHandler getCommandHandler() {
        if (this.commandHandler == null) {
            this.commandHandler = new CommandHandler(this, new PermHandler());
        }
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
        if (this.messager == null) {
            this.messager = new SimpleMessager(this);
            this.messager.setLocale(config().get(BaseConfig.LOCALE));
            this.messager.setLanguage(config().get(BaseConfig.LANGUAGE_FILE));
        }
        return this.messager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMessager(Messager messager) {
        this.messager = messager;
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
    public String getPluginName() {
        return getDescription().getName();
    }
    
    @Override
    public String getPluginVersion() {
        return getDescription().getVersion();
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
}
