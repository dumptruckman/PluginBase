/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.logging;

import java.io.File;
import java.util.logging.Level;

/**
 * Static plugin logger.
 */
public class Logging {

    private Logging() {
        throw new AssertionError();
    }

    static class LoggingPlugin implements LoggablePlugin {
        @Override
        public String getName() {
            return LoggingPlugin.class.getSimpleName();
        }

        @Override
        public File getDataFolder() {
            return new File(System.getProperty("user.dir"));
        }
    }

    final static LoggingPlugin DEFAULT_PLUGIN = new LoggingPlugin();

    static PluginLogger pluginLogger = PluginLogger.getLogger(DEFAULT_PLUGIN);

    public static void init(final LoggablePlugin plugin) {
        if (pluginLogger.plugin == DEFAULT_PLUGIN) {
            pluginLogger = PluginLogger.getLogger(plugin);
        }
    }

    public static PluginLogger getLogger() {
        return pluginLogger;
    }

    public static void setDebugLevel(final int level) {
        pluginLogger.setDebugLevel(level);
    }

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
    public static synchronized void log(final Level level, String message, final Object... args) {
        if (pluginLogger != null) {
            pluginLogger.log(level, message, args);
        }
    }

    /**
     * Fine debug level logging.  Use for infrequent messages.
     *
     * @param message Message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    public static void fine(final String message, final Object...args) {
        log(Level.FINE, message, args);
    }

    /**
     * Finer debug level logging.  Use for somewhat frequent messages.
     *
     * @param message Message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    public static void finer(final String message, final Object...args) {
        log(Level.FINER, message, args);
    }

    /**
     * Finest debug level logging.  Use for extremely frequent messages.
     *
     * @param message Message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    public static void finest(final String message, final Object...args) {
        log(Level.FINEST, message, args);
    }

    /**
     * Config level logging.  Use for messages that should be INFO level but have the option to be disabled
     * via debug level -1.
     *
     * @param message Message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    public static void config(final String message, final Object...args) {
        log(Level.CONFIG, message, args);
    }

    /**
     * Info level logging.
     *
     * @param message Message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    public static void info(final String message, final Object...args) {
        log(Level.INFO, message, args);
    }

    /**
     * Warning level logging.
     *
     * @param message Message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    public static void warning(final String message, final Object...args) {
        log(Level.WARNING, message, args);
    }

    /**
     * Severe level logging.
     *
     * @param message Message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    public static void severe(final String message, final Object...args) {
        log(Level.SEVERE, message, args);
    }

}


