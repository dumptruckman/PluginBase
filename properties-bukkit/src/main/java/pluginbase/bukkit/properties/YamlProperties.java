/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.bukkit.properties;

import pluginbase.logging.Logging;
import pluginbase.logging.PluginLogger;
import pluginbase.messages.PluginBaseException;
import pluginbase.properties.Properties;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Optionally commented Yaml implementation of Properties.
 * <p/>
 * <b>Do note:</b> Using comments will cause disk writes to take significantly longer than not using them.
 * <p/>
 * See {@link pluginbase.bukkit.properties.YamlProperties.Loader} for creating a YamlProperties object.
 */
public class YamlProperties extends AbstractFileProperties implements Properties {

    private final File configFile;
    private final boolean doComments;
    private final boolean autoDefaults;

    /**
     * Constructs a new YamlProperties object with the specified parameters.
     *
     * @param logger a logger to use for any important messages this Properties object may need to log.
     * @param doComments true to use comments.
     * @param autoDefaults true to set default values automatically.
     * @param configFile the file to load config from and to write config to.
     * @param configClasses the classes containing the Property objects associated with this Properties object.
     * @throws PluginBaseException if anything goes wrong while loading/writing-to the file.
     */
    protected YamlProperties(@NotNull final PluginLogger logger, boolean doComments, boolean autoDefaults,
                             @NotNull final File configFile, @NotNull final Class... configClasses) throws PluginBaseException {
        super(logger, CommentedYamlConfiguration.loadCommentedConfiguration(configFile, doComments), configClasses);
        this.configFile = configFile;
        this.doComments = doComments;
        this.autoDefaults = autoDefaults;

        // Prepare the loaded configuration file.
        prepareConfig();

        // Saves the configuration from memory to file.
        flush();
    }

    /**
     * A helper class used to load new YamlProperties objects from a file with the right options.
     */
    public static class Loader {

        private final PluginLogger logger;
        private final File configFile;
        private final Class[] configClasses;
        private boolean doComments = true;
        private boolean autoDefaults = true;

        /**
         * Creates a new loader for a YamlProperties object.
         * <p/>
         * By default, the loaded YamlProperties will have comments and automatically set defaults.
         * Use the optional methods in this class to change these options.
         * <p/>
         * Use {@link #load()} to finalize the options and create the YamlProperties object.
         *
         * @param logger a logger to use for any important messages this Properties object may need to log.
         * @param configFile the file to use for the Configuration object.
         * @param configClasses the classes the YamlProperties will get its Property objects from.
         */
        public Loader(@NotNull final PluginLogger logger,
                      @NotNull final File configFile,
                      @NotNull final Class... configClasses) {
            this.logger = logger;
            this.configFile = configFile;
            this.configClasses = configClasses;
        }

        /**
         * Specifies whether ot not to use comments on the YamlProperties object.
         * <p/>
         * <b>default:</b> true
         *
         * @param doComments true to use comments.
         * @return this Loader to chain methods.
         */
        public Loader comments(final boolean doComments) {
            this.doComments = doComments;
            return this;
        }

        /**
         * Specifies whether ot not to automatically default values on the YamlProperties object.
         * <p/>
         * <b>default:</b> true
         *
         * @param autoDefaults true to use automatically default Property values.
         * @return this Loader to chain methods.
         */
        public Loader defaults(final boolean autoDefaults) {
            this.autoDefaults = autoDefaults;
            return this;
        }

        /**
         * Loads a new YamlProperties object with all the specified options.
         *
         * @return a new YamlProperties object with all the specified options.
         * @throws PluginBaseException if anything goes wrong while loading the file.
         */
        public YamlProperties load() throws PluginBaseException {
            return new YamlProperties(logger, doComments, autoDefaults, configFile, configClasses);
        }
    }

    private void prepareConfig() {
        deserializeAll();

        // Sets defaults config values
        if (autoDefaults) {
            this.setDefaults();
        }

        config.options().header(getHeader());
    }

    private void loadConfig() throws PluginBaseException {
        try {
            config.load(configFile);
        } catch (FileNotFoundException e) {
            throw new PluginBaseException(e);
        } catch (IOException e) {
            throw new PluginBaseException(e);
        } catch (InvalidConfigurationException e) {
            throw new PluginBaseException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void reload() throws PluginBaseException {
        loadConfig();
        prepareConfig();
    }

    /** {@inheritDoc} */
    @Override
    public void flush() throws PluginBaseException {
        CommentedYamlConfiguration newConfig;
        try {
            newConfig = new EncodedYamlConfiguration("UTF-8", doComments);
        } catch (UnsupportedEncodingException e) {
            Logging.warning("Could not create UTF-8 configuration.  Special/Foreign characters may not be saved.");
            newConfig = new CommentedYamlConfiguration(doComments);
        }

        newConfig.options().header(getHeader());
        serializeAll(newConfig);
        if (doComments) {
            doComments(newConfig);
        }
        try {
            newConfig.save(configFile);
        } catch (IOException e) {
            throw new PluginBaseException(e);
        }
    }

    @NotNull
    protected String getHeader() {
        return "";
    }
}
