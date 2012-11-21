/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.logging;

import java.io.File;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * This is the primary class to use for logging purposes.  It allows you to obtain your own copy of PluginLogger
 * via {@link #getLogger(LoggablePlugin)}.
 *
 * Features:
 * - Thread safe logging.
 * - Plugin name prepended to log messages.
 * - Debug messages append plugin name with -Debug.  {@link #setDebugPrefix(String)}.
 * - {@link Level#CONFIG} messages can be silenced via {@link #setShowingConfig(boolean)}.
 * - {@link Level#FINE}, {@link Level#FINER}, {@link Level#FINEST} show as {@link Level#INFO} with
 *   {@link #setDebugLevel(int)} while also being written to a debug.log file.
 * - Allows you to share debug logs with another plugin.  (How Multiverse does it..)
 * - Adds var-arg parameter logging methods for use with messages in the style of
 * {@link String#format(String, Object...)}.
 * - {@link Level#FINE} and finer message do not perform {@link String#format(String, Object...)} on messages that
 * won't be logged.
 *
 * Tip: Create a static {@link Logging} class of your own in your own namespace to allow high flexibility in your
 * plugin's logging. (No need to pass an object around everywhere!)
 */
public class PluginLogger extends Logger {

    /**
     * Original debug suffix.
     */
    static final String ORIGINAL_DEBUG = "-Debug";
    /**
     * The default setting for whether or not to show {@link Level#CONFIG} messages.
     */
    static final boolean SHOW_CONFIG = true;

    // SUPPRESS-CHECKSTYLE:ACCESSOR
    final Logger logger;
    final String name;
    final DebugLog debugLog;
    final LoggablePlugin plugin;

    volatile String debugString = ORIGINAL_DEBUG;
    volatile boolean showConfig = SHOW_CONFIG;

    static final Map<String, PluginLogger> initializedLoggers = new HashMap<String, PluginLogger>();

    /**
     * Prepares the log for use.  Debugging will default to disabled when initialized.  This should be called early on
     * in plugin initialization, such as during onLoad() or onEnable().  If this {@link com.dumptruckman.minecraft.pluginbase.logging.Logging} class has already
     * been initialized, it will first be shut down before reinitializing.
     *
     * @param plugin The plugin using this static logger.
     */
    public static synchronized PluginLogger getLogger(final LoggablePlugin plugin, final LoggablePlugin pluginToShareDebugLogger) {
        if (initializedLoggers.containsKey(plugin.getName())) {
            return initializedLoggers.get(plugin.getName());
        }
        final DebugLog debugLog;
        final Logger logger = Logger.getLogger(plugin.getName());
        if (pluginToShareDebugLogger != null && initializedLoggers.containsKey(pluginToShareDebugLogger.getName())) {
            debugLog = initializedLoggers.get(pluginToShareDebugLogger.getName()).debugLog;
        } else {
            debugLog = DebugLog.getDebugLog(logger, getDebugFileName(plugin));
        }
        final PluginLogger logging = new PluginLogger(plugin, logger, debugLog);
        initializedLoggers.put(logging.getName(), logging);
        if (Logging.pluginLogger != null && Logging.pluginLogger.plugin == Logging.DEFAULT_PLUGIN) {
            Logging.pluginLogger = logging;
        }
        return logging;
    }

    public static synchronized PluginLogger getLogger(final LoggablePlugin plugin) {
        return getLogger(plugin, null);
    }

    PluginLogger(final LoggablePlugin plugin, final Logger logger, final DebugLog debugLog) {
        super(logger.getName(), logger.getResourceBundleName());
        this.logger = logger;
        this.debugLog = debugLog;
        this.plugin = plugin;
        this.name = plugin.getName();
    }

    synchronized void _log(final Level level, final String message) {
        final LogRecord record = new LogRecord(level, message);
        record.setLoggerName(getName());
        record.setResourceBundle(getResourceBundle());
        _log(record);
    }

    synchronized void _log(final LogRecord record) {
        super.log(record);
        if (debugLog != null) {
            debugLog.log(record);
        }
    }

    @Override
    public void log(final Level level, final String message) {
        log(level, message, new Object[0]);
    }

    /**
     * Log a message, with no arguments.  Similar to {@link java.util.logging.Logger#log(java.util.logging.LogRecord)} with the
     * exception that all logging is handled by a single static {@link com.dumptruckman.minecraft.pluginbase.logging.Logging} instance.
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
            _log(record);
        } else if (level != Level.FINE && level != Level.FINER && level != Level.FINEST) {
            if (level != Level.CONFIG || showConfig) {
                if (level == Level.CONFIG) {
                    record.setLevel(Level.INFO);
                }
                record.setMessage(getPrefixedMessage(message));
                _log(record);
            }
        }
    }

    static synchronized String getDebugFileName(final LoggablePlugin plugin) {
        return plugin.getDataFolder() + File.separator + "debug.log";
    }

    /**
     * Returns the {@link com.dumptruckman.minecraft.pluginbase.logging.Logging} class to it's original state, releasing the plugin that initialized it.  The
     * {@link com.dumptruckman.minecraft.pluginbase.logging.Logging} class can be reinitialized once it has been shut down.  This should be called when the plugin
     * is disabled so that a static reference to the plugin is not kept in cases of server reloads.
     */
    public synchronized void shutdown() {
        setDebugLevel(0);
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
    public synchronized void setDebugLevel(final int debugLevel) {
        if (debugLevel > 3 || debugLevel < 0) {
            throw new IllegalArgumentException("debugLevel must be between 0 and 3!");
        }
        if (debugLevel > 0) {
            debugLog.open();
        } else {
            debugLog.close();
        }
        debugLog.setDebugLevel(debugLevel);
    }

    /**
     * Returns the current debug logging level.
     *
     * @return A value 0-3 indicating the debug logging level.
     */
    public synchronized int getDebugLevel() {
        return debugLog.getDebugLevel();
    }

    /**
     * Sets whether or not to display {@link java.util.logging.Level#CONFIG} messages.
     *
     * @param showConfig true to enable, false to disable.
     */
    public void setShowingConfig(final boolean showConfig) {
        this.showConfig = showConfig;
    }

    /**
     * Whether or not this Logging will show {@link java.util.logging.Level#CONFIG} messages.
     *
     * @return true if this Logging will show {@link java.util.logging.Level#CONFIG} messages.
     */
    public boolean isShowingConfig() {
        return showConfig;
    }

    /**
     * Adds the plugin name and optionally the version number to the log message.
     *
     * @param message Log message
     * @return Modified message
     */
    public synchronized String getPrefixedMessage(final String message) {
        final StringBuilder builder = new StringBuilder("[").append(name);
        builder.append("] ").append(message);
        return builder.toString();
    }

    /**
     * Sets the debug prefix for debug messages that follows the plugin name.  The default is "-Debug".
     *
     * @param debugPrefix the new debug prefix to use.
     */
    public synchronized void setDebugPrefix(final String debugPrefix) {
        this.debugString = debugPrefix;
    }

    /**
     * Adds the plugin's debug name to the message.
     *
     * @param message     Log message
     * @return Modified message
     */
    public synchronized String getDebugString(final String message) {
        return "[" + name + debugString + "] " + message;
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
    public synchronized void log(final Level level, String message, final Object... args) {
        final int debugLevel = getDebugLevel();
        if ((level == Level.FINE && debugLevel >= 1)
                || (level == Level.FINER && debugLevel >= 2)
                || (level == Level.FINEST && debugLevel >= 3)) {
            debug(message, args);
        } else if (level != Level.FINE && level != Level.FINER && level != Level.FINEST) {
            if (level != Level.CONFIG || showConfig) {
                if (level == Level.CONFIG) {
                    _log(Level.INFO, getPrefixedMessage(format(message, args)));
                } else {
                    _log(level, getPrefixedMessage(format(message, args)));
                }
            }
        }
    }

    private String format(final String message, final Object[] args) {
        try {
            return String.format(message, args);
        } catch (IllegalFormatException e) {
            final StringBuilder builder = new StringBuilder();
            for (final Object object : args) {
                if (builder.length() != 0) {
                    builder.append(", ");
                }
                builder.append(object);
            }
            System.out.println("Illegal format in the following message with args: " + builder.toString());
        }
        return message;
    }

    /**
     * Directly outputs a message with the debug prefix to both the regular logger and the debug logger if one is set.
     *
     * @param message The message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    void debug(String message, final Object...args) {
        _log(Level.INFO, getDebugString(format(message, args)));
    }

    /**
     * Fine debug level logging.  Use for infrequent messages.
     *
     * @param message Message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    public void fine(final String message, final Object...args) {
        log(Level.FINE, message, args);
    }

    /**
     * Finer debug level logging.  Use for somewhat frequent messages.
     *
     * @param message Message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    public void finer(final String message, final Object...args) {
        log(Level.FINER, message, args);
    }

    /**
     * Finest debug level logging.  Use for extremely frequent messages.
     *
     * @param message Message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    public void finest(final String message, final Object...args) {
        log(Level.FINEST, message, args);
    }

    /**
     * Config level logging.  Use for messages that should be INFO level but have the option to be disabled
     * via debug level -1.
     *
     * @param message Message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    public void config(final String message, final Object...args) {
        log(Level.CONFIG, message, args);
    }

    /**
     * Info level logging.
     *
     * @param message Message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    public void info(final String message, final Object...args) {
        log(Level.INFO, message, args);
    }

    /**
     * Warning level logging.
     *
     * @param message Message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    public void warning(final String message, final Object...args) {
        log(Level.WARNING, message, args);
    }

    /**
     * Severe level logging.
     *
     * @param message Message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    public void severe(final String message, final Object...args) {
        log(Level.SEVERE, message, args);
    }

}
