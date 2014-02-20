/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.plugin;

import pluginbase.command.Command;
import pluginbase.command.CommandHandler;
import pluginbase.command.CommandProvider;
import pluginbase.command.QueuedCommand;
import pluginbase.config.SerializationRegistrar;
import pluginbase.jdbc.JdbcAgent;
import pluginbase.logging.LoggablePlugin;
import pluginbase.logging.PluginLogger;
import pluginbase.messages.messaging.Messager;
import pluginbase.messages.messaging.Messaging;
import pluginbase.messages.messaging.SendablePluginBaseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.permission.PermFactory;
import pluginbase.plugin.Settings.Language;
import pluginbase.plugin.command.builtin.VersionCommand;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * This represents the PluginBase plugin itself.
 * <p/>
 * Provides numerous useful methods for general plugin self-management.
 */
public final class PluginBase<P> implements LoggablePlugin, Messaging, CommandProvider {

    static {
        SerializationRegistrar.registerClass(Settings.class);
        SerializationRegistrar.registerClass(Language.class);
    }

    @NotNull
    private final PluginAgent<P> pluginAgent;
    private PluginLogger logger;
    private Settings settings = null;
    private CommandHandler commandHandler = null;
    private Messager messager = null;

    PluginBase(@NotNull PluginAgent<P> pluginAgent) {
        this.pluginAgent = pluginAgent;
    }

    public P getPlugin() {
        return pluginAgent.getPlugin();
    }

    void onLoad() {
        // Initialize our logging.
        logger = PluginLogger.getLogger(this);

        System.out.println("Plugin Class: " + getPluginClass());
        // Register the permission name for the plugin.
        PermFactory.registerPermissionName(getPluginClass(), pluginAgent.getPermissionPrefix());

        // Loads the configuration.
        settings = pluginAgent.loadSettings();

        // Setup the command handler.
        commandHandler = pluginAgent.getNewCommandHandler();
    }

    void onEnable() {
        // Loads the configuration.
        settings = pluginAgent.loadSettings();

        // Set up commands.
        pluginAgent.registerCommands();

        // Setup the plugin messager.
        messager = pluginAgent.getNewMessager(settings.getLanguageSettings());

        // Do any important first run stuff here.
        if (getSettings().isFirstRun()) {
            pluginAgent.firstRun();
            getSettings().setFirstRun(false);
            try {
                saveSettings();
            } catch (SendablePluginBaseException e) {
                e.printStackTrace();
                getLog().severe("Cannot save config on startup.  Terminating plugin.");
                pluginAgent.disablePlugin();
            }
        }
    }

    void onDisable() {
        getLog().shutdown();
    }

    public Class getPluginClass() {
        return pluginAgent.getPluginClass();
    }

    @NotNull
    public Settings getSettings() {
        return settings;
    }

    public void saveSettings() throws SendablePluginBaseException {
        pluginAgent.saveSettings();
    }

    /**
     * Gets the info object for this plugin.
     *
     * @return the info object for this plugin.
     */
    @NotNull
    public PluginInfo getPluginInfo() {
        return pluginAgent.getPluginInfo();
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public File getDataFolder() {
        return pluginAgent.getDataFolder();
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public String getCommandPrefix() {
        return pluginAgent.getCommandPrefix();
    }

    /**
     * Retrieves the JDBC agent for this plugin, if configured.
     * <p/>
     * This agent will provide a connection to a database which can be used to execute queries.
     *
     * @return the JDBC agent for this plugin or null if not configured.
     */
    @Nullable
    public JdbcAgent getJdbcAgent() {
        return pluginAgent.getJdbcAgent();
    }

    /**
     * Tells the plugin to reload the configuration and other data files.
     */
    public void reloadConfig() {
        Settings settings = pluginAgent.loadSettings();
        messager = pluginAgent.getNewMessager(settings.getLanguageSettings());
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public Messager getMessager() {
        return messager;
    }

    /**
     * Gets a list of special data points this plugin wishes to be shown when using the version command.
     *
     * @return a list of special data points for version info.
     */
    @NotNull
    public List<String> dumpVersionInfo() {
        List<String> buffer = new LinkedList<String>();
        buffer.add(getMessager().getLocalizedMessage(VersionCommand.VERSION_PLUGIN_VERSION, getPluginInfo().getName(), getPluginInfo().getVersion()));
        buffer.add(getMessager().getLocalizedMessage(VersionCommand.VERSION_SERVER_NAME, getServerInterface().getName()));
        buffer.add(getMessager().getLocalizedMessage(VersionCommand.VERSION_SERVER_VERSION, getServerInterface().getVersion()));
        buffer.add(getMessager().getLocalizedMessage(VersionCommand.VERSION_LANG_FILE, getSettings().getLanguageSettings().getLanguageFile()));
        buffer.add(getMessager().getLocalizedMessage(VersionCommand.VERSION_DEBUG_MODE, getSettings().getDebugLevel()));
        buffer = pluginAgent.getModifiedVersionInfo(buffer);
        return buffer;
    }

    /** {@inheritDoc} */
    @NotNull
    public ServerInterface getServerInterface() {
        return pluginAgent.getServerInterface();
    }

    /**
     * Gets the command handler for this plugin.
     *
     * @return the command handler for this plugin.
     */
    @NotNull
    @Override
    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    /**
     * Gets the PluginBase logger for this PluginBase plugin.
     *
     * @return the PluginBase logger for this PluginBase plugin.
     */
    @NotNull
    @Override
    public PluginLogger getLog() {
        return logger;
    }

    /** {@inheritDoc} */
    @Override
    public void scheduleQueuedCommandExpiration(@NotNull QueuedCommand queuedCommand) {
        if (useQueuedCommands()) {
            getServerInterface().runTaskLater(queuedCommand, queuedCommand.getExpirationDuration());
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean useQueuedCommands() {
        return pluginAgent.isQueuedCommandsEnabled();
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public String[] getAdditionalCommandAliases(Class<? extends Command> commandClass) {
        return pluginAgent.getAdditionalCommandAliases(commandClass);
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public String getName() {
        return getPluginInfo().getName();
    }
}
