/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties;

import com.dumptruckman.minecraft.pluginbase.plugin.BukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.util.Logging;

import java.io.File;
import java.io.IOException;

/**
 * Commented Yaml implementation of ConfigBase.
 */
public class YamlProperties extends AbstractYamlProperties implements Properties {

    private final File configFile;
    private final boolean doComments;
    private final boolean autoDefaults;

    public YamlProperties(BukkitPlugin plugin, boolean doComments, boolean autoDefaults, File configFile, Class... configClasses) throws IOException {
        super(plugin, new CommentedYamlConfiguration(configFile, doComments), configClasses);
        this.configFile = configFile;
        this.doComments = doComments;
        this.autoDefaults = autoDefaults;

        // Load the configuration file into memory
        load();

        // Saves the configuration from memory to file
        flush();
    }

    private boolean isValid(ValueProperty valueProperty, Object o) {
        if (!valueProperty.isValid(o)) {
            Logging.warning(valueProperty.getName() + " contains an invalid value!");
            Logging.warning(plugin.getMessager().getMessage(valueProperty.getInvalidMessage()));
            Logging.warning("Setting to default of: " + valueProperty.getDefault());
            getConfig().set(valueProperty.getName(), valueProperty.getDefault());
            flush();
            return false;
        }
        return true;
    }

    private void load() throws IOException {
        config.load();
        deserializeAll();

        // Sets defaults config values
        if (autoDefaults) {
            this.setDefaults();
        }

        config.getConfig().options().header(getHeader());
    }

    @Override
    public void reload() throws Exception {
        load();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush() {
        CommentedYamlConfiguration newConfig = new CommentedYamlConfiguration(configFile, doComments);
        newConfig.newConfig();
        newConfig.getConfig().options().header(getHeader());
        serializeAll(newConfig.getConfig());
        if (doComments) {
            doComments(newConfig);
        }
        newConfig.save();
    }
    
    protected String getHeader() {
        return "";
    }
}
