/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.util;

import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;

import java.io.File;
import java.util.IllegalFormatException;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Static plugin logger.
 */
public class Logging {

    static final String ORIGINAL_NAME = Logging.class.getSimpleName();
    static final String ORIGINAL_VERSION = "v.???";
    static final String ORIGINAL_DEBUG = "-Debug";

    static final InterceptedLogger LOG = new InterceptedLogger(Logger.getLogger("Minecraft"));

    static String name = ORIGINAL_NAME;
    static String version = ORIGINAL_VERSION;
    static String debug = ORIGINAL_DEBUG;
    static DebugLog debugLog = null;
    static PluginBase plugin = null;

    protected Logging() {
        throw new AssertionError();
    }

    static class InterceptedLogger extends Logger {

        final Logger logger;

        InterceptedLogger(final Logger logger) {
            super(logger.getName(), logger.getResourceBundleName());
            this.logger = logger;
        }

        synchronized void _log(final Level level, final String message) {
            final LogRecord record = new LogRecord(level, message);
            record.setLoggerName(getName());
            record.setResourceBundle(getResourceBundle());
            _log(record);
        }

        synchronized void _log(final LogRecord record) {
            logger.log(record);
            if (debugLog != null) {
                debugLog.log(record);
            }
        }

        /**
         * Log a message, with no arguments.  Similar to {@link Logger#log(LogRecord)} with the
         * exception that all logging is handled by a single static {@link Logging} instance.
         *
         * @param record the LogRecord.
         */
        @Override
        public synchronized void log(final LogRecord record) {
            final Level level = record.getLevel();
            final String message = record.getMessage();
            final int debugLevel = getDebugLevel();
            if ((level == Level.FINE && debugLevel >= 1)
                    || (level == Level.FINER && debugLevel >= 2)
                    || (level == Level.FINEST && debugLevel >= 3)) {
                record.setLevel(Level.INFO);
                record.setMessage(getDebugString(message));
                LOG._log(record);
            } else if (level != Level.FINE && level != Level.FINER && level != Level.FINEST) {
                record.setMessage(getPrefixedMessage(message, false));
                LOG._log(record);
            }
        }
    }

    /**
     * Prepares the log for use.  Debugging will default to disabled when initialized.  This should be called early on
     * in plugin initialization, such as during onLoad() or onEnable().  If this {@link Logging} class has already
     * been initialized, it will first be shut down before reinitializing.
     *
     * @param plugin The plugin using this static logger.
     */
    public static synchronized void init(final PluginBase plugin) {
        if (Logging.plugin != null) {
            shutdown();
        }
        name = plugin.getPluginInfo().getName();
        version = plugin.getPluginInfo().getVersion();
        DebugLog.init(name, getDebugFileName(plugin));
        setDebugLevel(0);
        Logging.plugin = plugin;
    }

    static synchronized String getDebugFileName(final PluginBase plugin) {
        return plugin.getDataFolder() + File.separator + "debug.log";
    }

    /**
     * Returns the {@link Logging} class to it's original state, releasing the plugin that initialized it.  The
     * {@link Logging} class can be reinitialized once it has been shut down.  This should be called when the plugin
     * is disabled so that a static reference to the plugin is not kept in cases of server reloads.
     */
    public synchronized static void shutdown() {
        closeDebugLog();
        DebugLog.shutdown();
        plugin = null;
        name = ORIGINAL_NAME;
        version = ORIGINAL_VERSION;
        debug = ORIGINAL_DEBUG;
    }

    /**
     * Closes the debug log if it is open.
     */
    static synchronized void closeDebugLog() {
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
     *   1 - enables debug logging of {@link java.util.logging.Level#FINE} or lower messages.
     *   2 - enables debug logging of {@link java.util.logging.Level#FINER} or lower messages.
     *   3 - enables debug logging of {@link java.util.logging.Level#FINEST} or lower messages.
     *
     * @param debugLevel 0 = off, 1-3 = debug level
     */
    public static synchronized void setDebugLevel(final int debugLevel) {
        if (debugLevel > 3 || debugLevel < 0) {
            throw new IllegalArgumentException("debugLevel must be between 0 and 3!");
        }
        if (debugLevel > 0) {
            debugLog = DebugLog.getDebugLogger();
        } else {
            closeDebugLog();
        }
        DebugLog.setDebugLevel(debugLevel);
    }

    /**
     * Returns the current debug logging level.
     *
     * @return A value 0-3 indicating the debug logging level.
     */
    public static synchronized int getDebugLevel() {
        return DebugLog.getDebugLevel();
    }

    /**
     * Adds the plugin name and optionally the version number to the log message.
     *
     * @param message     Log message
     * @param showVersion Whether to show version in log message
     * @return Modified message
     */
    public static synchronized String getPrefixedMessage(final String message, final boolean showVersion) {
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
    public static synchronized void setDebugPrefix(final String debugPrefix) {
        Logging.debug = debugPrefix;
    }

    /**
     * Adds the plugin's debug name to the message.
     *
     * @param message     Log message
     * @return Modified message
     */
    public static synchronized String getDebugString(final String message) {
        return "[" + name + debug + "] " + message;
    }

    /**
     * Returns the static Logger instance used by this class.
     *
     * @return the static Logger instance used by this class.
     */
    public static Logger getLogger() {
        return LOG;
    }

    /**
     * Custom log method.  Always logs to a single static logger.  Applies String.format() to the message if it is a
     * non-debug level logging and to debug level logging IF debug logging is enabled.  Optionally appends version to
     * prefix.
     *
     * @param showVersion True adds version into message prefix.
     * @param level       One of the message level identifiers, e.g. SEVERE.
     * @param message     The string message.
     * @param args        Arguments for the String.format() that is applied to the message.
     */
    public static synchronized void log(final boolean showVersion, final Level level, String message, final Object... args) {
        final int debugLevel = getDebugLevel();
        if ((level == Level.FINE && debugLevel >= 1)
                || (level == Level.FINER && debugLevel >= 2)
                || (level == Level.FINEST && debugLevel >= 3)) {
            debug(Level.INFO, message, args);
        } else if (level != Level.FINE && level != Level.FINER && level != Level.FINEST) {
            LOG._log(level, getPrefixedMessage(format(message, args), showVersion));
        }
    }

    private static String format(final String message, final Object[] args) {
        try {
            return String.format(message, args);
        } catch (IllegalFormatException e) {
            getLogger().fine("Illegal format in the following message:");
        }
        return message;
    }

    /**
     * Custom log method.  Always logs to a single static logger.  Applies String.format() to the message if it is a
     * non-debug level logging and to debug level logging IF debug logging is enabled.  Does not append version to
     * prefix.
     *
     * @param level       One of the message level identifiers, e.g. SEVERE.
     * @param message     The string message.
     * @param args        Arguments for the String.format() that is applied to the message.
     */
    public static void log(final Level level, String message, final Object... args) {
        log(false, level, message, args);
    }

    /**
     * Directly outputs a message with the debug prefix to both the regular logger and the debug logger if one is set.
     *
     * @param message The message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    static void debug(final Level level, String message, final Object...args) {
        LOG._log(level, getDebugString(format(message, args)));
    }

    /**
     * Fine debug level logging.  Use for infrequent messages.
     *
     * @param message Message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    public static void fine(final String message, final Object...args) {
        Logging.log(false, Level.FINE, message, args);
    }

    /**
     * Finer debug level logging.  Use for somewhat frequent messages.
     *
     * @param message Message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    public static void finer(final String message, final Object...args) {
        Logging.log(false, Level.FINER, message, args);
    }

    /**
     * Finest debug level logging.  Use for extremely frequent messages.
     *
     * @param message Message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    public static void finest(final String message, final Object...args) {
        Logging.log(false, Level.FINEST, message, args);
    }

    /**
     * Info level logging.
     *
     * @param message Message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    public static void info(final String message, final Object...args) {
        Logging.log(false, Level.INFO, message, args);
    }

    /**
     * Warning level logging.
     *
     * @param message Message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    public static void warning(final String message, final Object...args) {
        Logging.log(false, Level.WARNING, message, args);
    }

    /**
     * Severe level logging.
     *
     * @param message Message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    public static void severe(final String message, final Object...args) {
        Logging.log(false, Level.SEVERE, message, args);
    }

}



