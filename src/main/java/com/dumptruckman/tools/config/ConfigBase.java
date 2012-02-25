package com.dumptruckman.tools.config;

import org.bukkit.configuration.Configuration;

import java.util.Locale;

/**
 * Interface for interacting with the config of this plugin.
 */
public interface ConfigBase {

    /**
     * Convenience method for saving the config to disk.
     */
    void save();

    /**
     * Sets globalDebug.
     * @param globalDebug The new value.
     */
    void setDebug(int globalDebug);

    /**
     * Gets globalDebug.
     * @return globalDebug.
     */
    int getDebug();

    /**
     * Retrieves the locale string from the config.
     *
     * @return The locale string.
     */
    Locale getLocale();

    /**
     * @return The name of the language file to use.
     */
    String getLanguageFileName();

    /**
     * Tells whether this is the first time the plugin has run as set by a config flag.
     *
     * @return True if first_run is set to true in config.
     */
    boolean isFirstRun();

    /**
     * Sets the first_run flag in the config so that the plugin no longer thinks it is the first run.
     *
     * @param firstRun What to set the flag to in the config.
     */
    void setFirstRun(boolean firstRun);
}
