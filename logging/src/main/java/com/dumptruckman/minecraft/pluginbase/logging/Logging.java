/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.logging;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.logging.Level;

/**
 * Static plugin logger.  {@link #init(LoggablePlugin)} should be called before using other methods in this class.
 */
public final class Logging {

    private Logging() {
        throw new AssertionError();
    }

    /**
     * The default "plugin" class to use for static logging.
     */
    private static class LoggingPlugin implements LoggablePlugin {
        @NotNull
        @Override
        public String getName() {
            return LoggingPlugin.class.getSimpleName();
        }

        @NotNull
        @Override
        public File getDataFolder() {
            return new File(System.getProperty("user.dir"));
        }
    }

    /** Single instance of LoggingPlugin for use as the default plugin for this static logging class. */
    static final LoggingPlugin DEFAULT_PLUGIN = new LoggingPlugin();

    /** The PluginLogger instance used for static logging.  Package-Private so PluginLogger may change this. */
    @NotNull
    static volatile PluginLogger pluginLogger = PluginLogger.getLogger(DEFAULT_PLUGIN);

    /**
     * Initializes this static logging class for the FIRST plugin to call this method.  Any subsequent calls will
     * DO NOTHING.
     *
     * @param plugin The plugin that will use this static logging class.
     */
    public static void init(@NotNull final LoggablePlugin plugin) {
        if (pluginLogger.plugin == DEFAULT_PLUGIN) {
            pluginLogger = PluginLogger.getLogger(plugin);
        }
    }

    /**
     * Returns the single PluginLogger instance for this static logging class.
     *
     * @return the PluginLogger used for static logging by this class.
     */
    @NotNull
    public static PluginLogger getLogger() {
        return pluginLogger;
    }

    /**
     * Sets the debug logging level of this plugin.  Debug messages will print to the console and to a
     * debug log file when enabled.
     * debugLevel:
     *   0 - turns off debug logging, disabling the debug logger, closing any open file hooks.
     *   1 - enables debug logging of {@link java.util.logging.Level#FINE} or lower messages.
     *   2 - enables debug logging of {@link java.util.logging.Level#FINER} or lower messages.
     *   3 - enables debug logging of {@link java.util.logging.Level#FINEST} or lower messages.
     *
     * @param debugLevel 0 = off, 1-3 = debug level
     */
    public static void setDebugLevel(final int debugLevel) {
        pluginLogger.setDebugLevel(debugLevel);
    }

    /**
     * Performs any necessary shutdown steps to ensure this logger keeps no open file hooks.
     */
    public static void shutdown() {
        pluginLogger.shutdown();
    }

    /**
     * Custom log method.  Always logs to a single static logger.  Applies String.format() to the message if it is a
     * non-debug level logging and to debug level logging IF debug logging is enabled.  Optionally appends version to
     * prefix.
     *
     * @param level       One of the message level identifiers, e.g. SEVERE.
     * @param message     The string message.
     * @param args        Arguments for the String.format() that is applied to the message.
     */
    public static void log(@NotNull final Level level, @NotNull final String message, @NotNull final Object... args) {
        pluginLogger.log(level, message, args);
    }

    /**
     * Fine debug level logging.  Use for infrequent messages.
     *
     * @param message Message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    public static void fine(@NotNull final String message, @NotNull final Object...args) {
        pluginLogger.fine(message, args);
    }

    /**
     * Finer debug level logging.  Use for somewhat frequent messages.
     *
     * @param message Message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    public static void finer(@NotNull final String message, @NotNull final Object...args) {
        pluginLogger.finer(message, args);
    }

    /**
     * Finest debug level logging.  Use for extremely frequent messages.
     *
     * @param message Message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    public static void finest(@NotNull final String message, @NotNull final Object...args) {
        pluginLogger.finest(message, args);
    }

    /**
     * Config level logging.  Use for messages that should be INFO level but have the option to be disabled
     * via debug level -1.
     *
     * @param message Message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    public static void config(@NotNull final String message, @NotNull final Object...args) {
        pluginLogger.config(message, args);
    }

    /**
     * Info level logging.
     *
     * @param message Message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    public static void info(@NotNull final String message, @NotNull final Object...args) {
        pluginLogger.info(message, args);
    }

    /**
     * Warning level logging.
     *
     * @param message Message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    public static void warning(@NotNull final String message, @NotNull final Object...args) {
        pluginLogger.warning(message, args);
    }

    /**
     * Severe level logging.
     *
     * @param message Message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    public static void severe(@NotNull final String message, @NotNull final Object...args) {
        pluginLogger.severe(message, args);
    }

}


