/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.util;

import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Static plugin logger.
 */
public class Logging {

    private static Logger log = Logger.getLogger("Minecraft");
    private static String name = "PluginBase";
    private static String version = "v.???";
    private static String debug = "-Debug";
    private static int debugLevel = 0;
    private static DebugLog debugLog = null;
    private static PluginBase plugin;

    private Logging() {
        throw new AssertionError();
    }

    /**
     * Prepares the log for use.  Debugging will default to disabled when initialized.
     *
     * @param plugin The plugin using this static logger.
     */
    public static void init(final PluginBase plugin) {
        name = plugin.getPluginInfo().getName();
        version = plugin.getPluginInfo().getVersion();
        plugin.getDataFolder().mkdirs();
        Logging.plugin = plugin;
        setDebugLevel(0);
    }

    /**
     * Closes the debug log if it is open.
     */
    public static void close() {
        if (debugLog != null) {
            debugLog.close();
            debugLog = null;
        }
    }
    
    /**
     * Sets the debug logging level of this plugin.  Debug messages will print to the console and to a
     * debug log file when enabled.
     * debugLevel:
     *   0 - turns off debug logging, disabling the debug logger, closing any open file hooks.
     *   1 - enables debug logging of {@link Level#FINE} or lower messages.
     *   2 - enables debug logging of {@link Level#FINER} or lower messages.
     *   3 - enables debug logging of {@link Level#FINEST} or lower messages.
     *
     * @param debugLevel 0 = off, 1-3 = debug level
     */
    public static void setDebugLevel(final int debugLevel) {
        if (debugLevel > 3 || debugLevel < 0) {
            throw new IllegalArgumentException("debugLevel must be between 0 and 3!");
        }
        if (debugLevel > 0) {
            debugLog = new DebugLog(name, plugin.getDataFolder() + File.separator + "debug.log");
        } else {
            close();
        }
        Logging.debugLevel = debugLevel;
    }

    /**
     * Returns the current debug logging level.
     *
     * @return A value 0-3 indicating the debug logging level.
     */
    public static int getDebugLevel() {
        return debugLevel;
    }

    /**
     * Adds the plugin name and optionally the version number to the log message.
     *
     * @param message     Log message
     * @param showVersion Whether to show version in log message
     * @return Modified message
     */
    public static String getPrefixedMessage(final String message, final boolean showVersion) {
        final StringBuilder builder = new StringBuilder("[").append(name);
        if (showVersion) {
            builder.append(" ").append(version);
        }
        builder.append("] ").append(message);
        return builder.toString();
    }

    /**
     * Sets the debug prefix for debug messages that follows the plugin name.  The default is "-Debug".
     *
     * @param debugPrefix the new debug prefix to use.
     */
    public static void setDebugPrefix(final String debugPrefix) {
        Logging.debug = debugPrefix;
    }

    /**
     * Adds the plugin's debug name to the message.
     *
     * @param message     Log message
     * @return Modified message
     */
    public static String getDebugString(final String message) {
        return "[" + name + debug + "] " + message;
    }

    /**
     * Returns the logger object.
     *
     * @return Logger object
     */
    public static Logger getLogger() {
        return log;
    }

    /**
     * Custom log method.
     *
     * @param level       Log level
     * @param message     Log message
     * @param showVersion True adds version into message
     */
    public static void log(final Level level, String message, final boolean showVersion, final Object...args) {
        if (level == Level.FINE && Logging.debugLevel >= 1) {
            debug(Level.INFO, message, args);
        } else if (level == Level.FINER && Logging.debugLevel >= 2) {
            debug(Level.INFO, message, args);
        } else if (level == Level.FINEST && Logging.debugLevel >= 3) {
            debug(Level.INFO, message, args);
        } else if (level != Level.FINE && level != Level.FINER && level != Level.FINEST) {
            message = String.format(message, args);
            log.log(level, getPrefixedMessage(message, showVersion));
            if (debugLog != null) {
                debugLog.log(level, getPrefixedMessage(message, showVersion));
            }
        }
    }

    /**
     * Directly outputs a message with the debug prefix to both the regular logger and the debug logger if one is set.
     *
     * @param message The message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    private static void debug(final Level level, String message, final Object...args) {
        message = String.format(message, args);
        log.log(level, getDebugString(message));
        if (debugLog != null) {
            debugLog.log(level, getDebugString(message));
        }
    }

    /**
     * Info level logging.
     *
     * @param message Message to log.
     */
    public static void fine(final String message, final Object...args) {
        Logging.log(Level.FINE, message, false, args);
    }

    /**
     * Finer debug level logging.  Use for somewhat frequent messages.
     *
     * @param message Message to log.
     */
    public static void finer(final String message, final Object...args) {
        Logging.log(Level.FINER, message, false, args);
    }

    /**
     * Finest debug level logging.  Use for extremely frequent messages.
     *
     * @param message Message to log.
     */
    public static void finest(final String message, final Object...args) {
        Logging.log(Level.FINEST, message, false, args);
    }

    /**
     * Info level logging.
     *
     * @param message Message to log.
     */
    public static void info(final String message, final Object...args) {
        Logging.log(Level.INFO, message, false, args);
    }

    /**
     * Warning level logging.
     *
     * @param message Message to log.
     */
    public static void warning(final String message, final Object...args) {
        Logging.log(Level.WARNING, message, false, args);
    }

    /**
     * Severe level logging.
     *
     * @param message Message to log.
     */
    public static void severe(final String message, final Object...args) {
        Logging.log(Level.SEVERE, message, false, args);
    }

}


