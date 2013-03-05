/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.bukkit.properties;

import com.dumptruckman.minecraft.pluginbase.messages.PluginBaseException;
import com.dumptruckman.minecraft.pluginbase.properties.Properties;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Commented Yaml implementation of ConfigBase.
 */
public class YamlProperties extends AbstractFileProperties implements Properties {

    private final File configFile;
    private final boolean doComments;
    private final boolean autoDefaults;

    public YamlProperties(boolean doComments, boolean autoDefaults, File configFile, Class... configClasses) throws PluginBaseException {
        super(CommentedYamlConfiguration.loadCommentedConfiguration(configFile, doComments), configClasses);
        this.configFile = configFile;
        this.doComments = doComments;
        this.autoDefaults = autoDefaults;

        // Prepare the loaded configuration file.
        prepareConfig();

        // Saves the configuration from memory to file.
        flush();
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
        CommentedYamlConfiguration newConfig = new CommentedYamlConfiguration(doComments);
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

    protected String getHeader() {
        return "";
    }
}
