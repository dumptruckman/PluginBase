package com.dumptruckman.tools.plugin;

import com.dumptruckman.tools.config.ConfigBase;
import com.dumptruckman.tools.locale.Messager;
import com.dumptruckman.tools.locale.SimpleMessager;
import com.dumptruckman.tools.permission.Perm;
import com.dumptruckman.tools.permission.PermHandler;
import com.dumptruckman.tools.plugin.command.DebugCommand;
import com.dumptruckman.tools.plugin.command.ReloadCommand;
import com.dumptruckman.tools.util.Logging;
import com.pneumaticraft.commandhandler.CommandHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Main plugin class for dumptruckman's Plugin Template.
 */
public abstract class AbstractPluginBase<C extends ConfigBase> extends JavaPlugin implements PluginBase<C> {

    private C config = null;
    private Messager messager = null;
    private File serverFolder = null;
    private CommandHandler commandHandler = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDisable() {
        // Display disable message/version info
        Logging.info("disabled.", true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEnable() {
        Logging.init(this);
        Perm.registerPlugin(this);

        try {
            this.getMessager().setLocale(new Locale(this.getSettings().getLocale()));
        } catch (IllegalArgumentException e) {
            Logging.severe(e.getMessage());
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.reloadConfig();

        // Register Events
        this.registerEvents();

        // Register Commands
        this.registerCommands();

        // Display enable message/version info
        Logging.info("enabled.", true);
    }

    /**
     * Nulls the config object and reloads a new one.
     */
    public void reloadConfig() {
        this.config = null;

        // Do any import first run stuff here.
        if (this.getSettings().isFirstRun()) {
            Logging.info("First run!");
        }
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PluginListener(this), this);
    }

    private void registerCommands() {
        getCommandHandler();
        getCommandHandler().registerCommand(new DebugCommand<AbstractPluginBase>(this));
        getCommandHandler().registerCommand(new ReloadCommand<AbstractPluginBase>(this));
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
    public C getSettings() {
        if (this.config == null) {
            // Loads the configuration
            try {
                this.config = newConfigInstance();
                Logging.fine("Loaded config file!");
            } catch (IOException e) {  // Catch errors loading the config file and exit out if found.
                Logging.severe("Error loading config file!");
                Logging.severe(e.getMessage());
                Bukkit.getPluginManager().disablePlugin(this);
                return null;
            }
        }
        return this.config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Messager getMessager() {
        if (this.messager == null) {
            this.messager = new SimpleMessager(this);
        }
        return this.messager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMessager(Messager messager) {
        if (messager == null) {
            throw new IllegalArgumentException("The new messager can't be null!");
        }

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
    public abstract String getCommandPrefix();

    protected abstract C newConfigInstance() throws IOException;
}
