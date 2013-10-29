/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.plugin;

import org.springframework.jdbc.core.JdbcTemplate;
import pluginbase.command.CommandHandler;
import pluginbase.command.CommandProvider;
import pluginbase.logging.LoggablePlugin;
import pluginbase.logging.PluginLogger;
import pluginbase.messages.messaging.Messager;
import pluginbase.messages.messaging.Messaging;
import pluginbase.messages.messaging.SendablePluginBaseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mcstats.Metrics;

import javax.sql.DataSource;
import java.io.File;
import java.util.List;

/**
 * This represents the PluginBase plugin itself.
 * <p/>
 * Provides numerous useful methods for general plugin self-management.
 */
public interface PluginBase extends LoggablePlugin, Messaging, CommandProvider {

    @NotNull
    Settings getSettings();

    void saveConfig();

    void saveSettings() throws SendablePluginBaseException;

    /**
     * Gets the info object for this plugin.
     *
     * @return the info object for this plugin.
     */
    @NotNull
    PluginInfo getPluginInfo();

    /**
     * Returns the directory that the plugin data's files are located in.
     *
     * @return the directory that the plugin data's files are located in.
     */
    @NotNull
    File getDataFolder();

    /**
     * Returns the short prefix for the commands owned by this plugin.
     * <p/>
     * This is particularly necessary due to PluginBase offering built in commands.
     *
     * @return the short prefix for the commands owned by this plugin.
     */
    @NotNull
    String getCommandPrefix();

    /**
     * Gets the sql data source for this plugin if it uses one.
     *
     * @return the sql data source or null if unused.
     */
    @Nullable
    DataSource getDataSource();

    /**
     * Creates a new JDBC Template which is the basis for Spring's JDBC API.
     *
     * @return a new JDBC Template.
     * @throws IllegalStateException if this plugin is not set up for a database.
     */
    @NotNull
    JdbcTemplate createJdbcTemplate() throws IllegalStateException;

    /**
     * Gets the sql database configuration for this plugin if it uses one.
     *
     * @return the sql database configuration or null if unused.
     */
    @Nullable
    SQLSettings getSQLSettings();

    /**
     * Gets the metrics object for this plugin.
     *
     * @return the metrics object for this plugin or null if something went wrong while enabling one or if the
     * plugin chooses not to use metrics.
     */
    @Nullable
    Metrics getMetrics();

    /**
     * Tells the plugin to reload the configuration and other data files.
     * <p/>
     * Exactly what this entails is up to the implementation.
     */
    void reloadConfig();

    /**
     * Gets the messager for this plugin.
     *
     * @return the messager for this plugin.
     */
    @NotNull
    Messager getMessager();

    /**
     * Gets a list of special data points this plugin wishes to be shown when using the version command.
     *
     * @return a list of special data points for version info.
     */
    @NotNull
    List<String> dumpVersionInfo();

    /**
     * Gets the interface for interacting with the server implementation.
     *
     * @return the interface for interacting with the server implementation.
     */
    @NotNull
    ServerInterface getServerInterface();

    /**
     * Gets the command handler for this plugin.
     *
     * @return the command handler for this plugin.
     */
    @NotNull
    CommandHandler getCommandHandler();

    /**
     * Gets the PluginBase logger for this PluginBase plugin.
     *
     * @return the PluginBase logger for this PluginBase plugin.
     */
    @NotNull
    PluginLogger getLog();
}
