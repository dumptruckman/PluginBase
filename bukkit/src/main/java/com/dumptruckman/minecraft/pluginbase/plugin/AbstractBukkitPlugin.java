package com.dumptruckman.minecraft.pluginbase.plugin;

import com.dumptruckman.minecraft.pluginbase.config.BaseConfig;
import com.dumptruckman.minecraft.pluginbase.locale.CommandMessages;
import com.dumptruckman.minecraft.pluginbase.locale.Messager;
import com.dumptruckman.minecraft.pluginbase.locale.SimpleMessager;
import com.dumptruckman.minecraft.pluginbase.permission.Perm;
import com.dumptruckman.minecraft.pluginbase.permission.PermHandler;
import com.dumptruckman.minecraft.pluginbase.plugin.command.DebugCommand;
import com.dumptruckman.minecraft.pluginbase.plugin.command.HelpCommand;
import com.dumptruckman.minecraft.pluginbase.plugin.command.ReloadCommand;
import com.dumptruckman.minecraft.pluginbase.util.Logging;
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

/**
 * Main plugin class for dumptruckman's Plugin Template.
 */
public abstract class AbstractBukkitPlugin<C extends BaseConfig> extends JavaPlugin implements BukkitPlugin<C> {

    private C config = null;
    private Messager messager = null;
    private File serverFolder = null;
    private CommandHandler commandHandler = null;

    public void preDisable() {

    }

    @Override
    public void onLoad() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDisable() {
        preDisable();
        // Display disable message/version info
        Logging.info("disabled.", true);
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
        Perm.registerPlugin(this);
        CommandMessages.init();
        Logging.init(this);

        reloadConfig();

        // Register Commands
        _registerCommands();

        postEnable();
        // Display enable message/version info
        Logging.info("enabled.", true);
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
        this.config = null;
        this.messager = null;
        
        Logging.setDebugMode(config().get(BaseConfig.DEBUG_MODE));

        // Do any import first run stuff here.
        if (config().get(BaseConfig.FIRST_RUN)) {
            Logging.info("First run!");
            firstRun();
            config().set(BaseConfig.FIRST_RUN, false);
            config().save();
        }
    }

    public void postReload() {

    }

    private final void _registerCommands() {
        getCommandHandler().registerCommand(new DebugCommand<AbstractBukkitPlugin>(this));
        getCommandHandler().registerCommand(new ReloadCommand<AbstractBukkitPlugin>(this));
        getCommandHandler().registerCommand(new HelpCommand<AbstractBukkitPlugin>(this));
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
    public abstract List<String> getCommandPrefixes();

    protected abstract C newConfigInstance() throws IOException;
}
