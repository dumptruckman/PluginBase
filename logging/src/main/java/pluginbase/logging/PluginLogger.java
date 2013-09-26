/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.logging;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * This is the primary class to use for logging purposes.  It allows you to obtain your own copy of PluginLogger
 * via {@link #getLogger(LoggablePlugin)}.
 * <p/>
 * Features:
 * <br/>- Thread safe logging.
 * <br/>- Plugin name prepended to log messages.
 * <br/>- Debug messages append plugin name with -Debug.  {@link #setDebugPrefix(String)}.
 * <br/>- {@link Level#CONFIG} messages can be silenced via {@link #setShowingConfig(boolean)}.
 * <br/>- {@link Level#FINE}, {@link Level#FINER}, {@link Level#FINEST} show as {@link Level#INFO} with
 *   {@link #setDebugLevel(int)} while also being written to a debug.log file.
 * <br/>- Allows you to share debug logs with another plugin.  (How Multiverse does it..)
 * <br/>- Adds var-arg parameter logging methods for use with messages in the style of
 * {@link String#format(String, Object...)}.
 * <br/>- {@link Level#FINE} and finer message do not perform {@link String#format(String, Object...)} on messages that
 * won't be logged.
 * <p/>
 * Tip: Create a static {@link Logging} class of your own in your own namespace to allow high flexibility in your
 * plugin's logging. (No need to pass an object around everywhere!)
 */
public class PluginLogger extends Logger {

    /** The original debug suffix. */
    static final String ORIGINAL_DEBUG = "-Debug";
    private static final boolean SHOW_CONFIG = true;

    /** Logger everything is logged to. */
    @NotNull
    final Logger logger;
    /** Name of the plugin using this logger for appending purposes. */
    @NotNull
    final String pluginName;
    /** The debug log instance for this PluginLogger. */
    @NotNull
    final DebugLog debugLog;
    /** The loggable plugin we use for this Plugin Logger. */
    @NotNull
    final LoggablePlugin plugin;

    @NotNull
    private volatile String debugString = ORIGINAL_DEBUG;
    private volatile boolean showConfig = SHOW_CONFIG;

    /** A map containing all initialized PluginLoggers mapped to the name of the plugin that initialized them. */
    static final Map<String, PluginLogger> INITIALIZED_LOGGERS = new HashMap<String, PluginLogger>();

    /**
     * Prepares the log for use.
     * <p/>
     * Debugging will default to disabled when initialized.
     * <p/>
     * This should be called early on in plugin initialization, such as during onLoad() or onEnable().
     * <p/>
     * If a logger has already been created for the plugin passed then that will be returned with no additional
     * initialization steps.
     *
     * @param plugin The plugin using this logger.
     * @param pluginToShareDebugLogger If you would like to use the same debug log file as another LoggablePlugin
     *                                 specify that plugin here.  Otherwise specify null.
     * @return A logger for your plugin.
     */
    public static synchronized PluginLogger getLogger(@NotNull final LoggablePlugin plugin,
                                                      @Nullable final LoggablePlugin pluginToShareDebugLogger) {
        if (INITIALIZED_LOGGERS.containsKey(plugin.getName())) {
            return INITIALIZED_LOGGERS.get(plugin.getName());
        }
        final DebugLog debugLog;
        final Logger logger = Logger.getLogger(plugin.getName());
        if (pluginToShareDebugLogger != null && INITIALIZED_LOGGERS.containsKey(pluginToShareDebugLogger.getName())) {
            debugLog = INITIALIZED_LOGGERS.get(pluginToShareDebugLogger.getName()).debugLog;
        } else {
            debugLog = DebugLog.getDebugLog(logger, getDebugFolder(plugin));
        }
        final PluginLogger logging = new PluginLogger(plugin, logger, debugLog);
        INITIALIZED_LOGGERS.put(logging.getName(), logging);
        return logging;
    }

    /**
     * Prepares the log for use.
     * <p/>
     * Debugging will default to disabled when initialized.
     * <p/>
     * This should be called early on in plugin initialization, such as during onLoad() or onEnable().
     * <p/>
     * If a logger has already been created for the plugin passed then that will be returned with no additional
     * initialization steps.
     *
     * @param plugin The plugin using this logger.
     * @return A logger for your plugin.
     */
    public static synchronized PluginLogger getLogger(@NotNull final LoggablePlugin plugin) {
        return getLogger(plugin, null);
    }

    private PluginLogger(@NotNull final LoggablePlugin plugin,
                         @NotNull final Logger logger,
                         @NotNull final DebugLog debugLog) {
        super(logger.getName(), logger.getResourceBundleName());
        this.logger = logger;
        this.debugLog = debugLog;
        this.plugin = plugin;
        this.pluginName = plugin.getName();
    }

    private synchronized void privateLog(@NotNull final Level level, @NotNull final String message) {
        final LogRecord record = new LogRecord(level, message);
        record.setLoggerName(getName());
        record.setResourceBundle(getResourceBundle());
        privateLog(record);
    }

    private synchronized void privateLog(@NotNull final LogRecord record) {
        super.log(record);
        if (debugLog != null) {
            debugLog.log(record);
        }
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized void log(@NotNull final Level level, @NotNull final String message) {
        log(level, message, new Object[0]);
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized void log(@NotNull final LogRecord record) {
        final Level level = record.getLevel();
        final String message = record.getMessage();
        final int debugLevel = getDebugLevel();
        if ((level == Level.FINE && debugLevel >= 1)
                || (level == Level.FINER && debugLevel >= 2)
                || (level == Level.FINEST && debugLevel >= 3)) {
            record.setLevel(Level.INFO);
            record.setMessage(getDebugString(message));
            privateLog(record);
        } else if (level != Level.FINE && level != Level.FINER && level != Level.FINEST) {
            if (level != Level.CONFIG || showConfig) {
                if (level == Level.CONFIG) {
                    record.setLevel(Level.INFO);
                }
                record.setMessage(getPrefixedMessage(message));
                privateLog(record);
            }
        }
    }

    /**
     * Returns the folder where we will store debug logs.  Package-private for testing purposes.
     *
     * @param plugin The loggable plugin to get the debug log file for.
     * @return The name of the debug log file.
     */
    @NotNull
    static synchronized File getDebugFolder(@NotNull final LoggablePlugin plugin) {
        final File debugFolder = new File(plugin.getDataFolder(), "debug");
        debugFolder.mkdirs();
        return debugFolder;
    }

    /**
     * Performs any necessary shutdown steps to ensure this logger keeps no open file hooks.
     */
    public final synchronized void shutdown() {
        setDebugLevel(0);
    }

    /**
     * Sets the debug logging level of this plugin.
     * <p/>
     * Debug messages will print to the console and to a debug log file when enabled.
     * <p/>
     * debugLevel:
     * <br/>0 - turns off debug logging, disabling the debug logger, closing any open file hooks.
     * <br/>1 - enables debug logging of {@link java.util.logging.Level#FINE} or lower messages.
     * <br/>2 - enables debug logging of {@link java.util.logging.Level#FINER} or lower messages.
     * <br/>3 - enables debug logging of {@link java.util.logging.Level#FINEST} or lower messages.
     *
     * @param debugLevel 0 = off, 1-3 = debug level
     */
    public final synchronized void setDebugLevel(final int debugLevel) {
        if (debugLevel > 3 || debugLevel < 0) {
            throw new IllegalArgumentException("debugLevel must be between 0 and 3!");
        }
        debugLog.setDebugLevel(debugLevel);
    }

    /**
     * Returns the current debug logging level.
     *
     * @return A value 0-3 indicating the debug logging level.
     */
    public final synchronized int getDebugLevel() {
        return debugLog.getDebugLevel();
    }

    /**
     * Sets whether or not to display {@link java.util.logging.Level#CONFIG} messages.
     *
     * @param showConfig true to enable, false to disable.
     */
    public final void setShowingConfig(final boolean showConfig) {
        this.showConfig = showConfig;
    }

    /**
     * Whether or not this Logging will show {@link java.util.logging.Level#CONFIG} messages.
     *
     * @return true if this Logging will show {@link java.util.logging.Level#CONFIG} messages.
     */
    public final boolean isShowingConfig() {
        return showConfig;
    }

    /**
     * Adds the plugin name and optionally the version number to the log message.
     *
     * @param message Log message
     * @return Modified message
     */
    @NotNull
    public final synchronized String getPrefixedMessage(@NotNull final String message) {
        if (pluginName.equals(Logging.class.getName())) {
            return message;
        }
        final StringBuilder builder = new StringBuilder("[").append(pluginName);
        builder.append("] ").append(message);
        return builder.toString();
    }

    /**
     * Sets the debug prefix for debug messages that follows the plugin name.  The default is "-Debug".
     *
     * @param debugPrefix the new debug prefix to use.
     */
    public final synchronized void setDebugPrefix(@NotNull final String debugPrefix) {
        this.debugString = debugPrefix;
    }

    /**
     * Adds the plugin's debug name to the message.
     *
     * @param message     Log message
     * @return Modified message
     */
    @NotNull
    public final synchronized String getDebugString(@NotNull final String message) {
        if (pluginName.equals(Logging.class.getName())) {
            return "[" + debugString + "] " + message;
        }
        return "[" + pluginName + debugString + "] " + message;
    }

    /**
     * Custom log method.
     * <p/>
     * Applies String.format() to the message if it is a non-debug level logging and to debug level logging IF debug
     * logging is enabled.
     * <p/>
     * Optionally appends version to prefix.
     *
     * @param level       One of the message level identifiers, e.g. SEVERE.
     * @param message     The string message.
     * @param args        Arguments for the String.format() that is applied to the message.
     */
    public final synchronized void log(@NotNull final Level level,
                                       @NotNull final String message,
                                       @NotNull final Object... args) {
        final int debugLevel = getDebugLevel();
        if ((level == Level.FINE && debugLevel >= 1)
                || (level == Level.FINER && debugLevel >= 2)
                || (level == Level.FINEST && debugLevel >= 3)) {
            debug(message, args);
        } else if (level != Level.FINE && level != Level.FINER && level != Level.FINEST) {
            if (level != Level.CONFIG || showConfig) {
                if (level == Level.CONFIG) {
                    privateLog(Level.INFO, getPrefixedMessage(Formatter.format(message, args)));
                } else {
                    privateLog(level, getPrefixedMessage(Formatter.format(message, args)));
                }
            }
        }
    }

    /**
     * Directly outputs a message with the debug prefix to both the regular logger and the debug logger if one is set.
     *
     * @param message The message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    private void debug(@NotNull final String message, @NotNull final Object...args) {
        privateLog(Level.INFO, getDebugString(Formatter.format(message, args)));
    }

    /**
     * Fine debug level logging.  Use for infrequent messages.
     *
     * @param message Message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    public final void fine(@NotNull final String message, @NotNull final Object...args) {
        log(Level.FINE, message, args);
    }

    /**
     * Finer debug level logging.  Use for somewhat frequent messages.
     *
     * @param message Message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    public final void finer(@NotNull final String message, @NotNull final Object...args) {
        log(Level.FINER, message, args);
    }

    /**
     * Finest debug level logging.  Use for extremely frequent messages.
     *
     * @param message Message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    public final void finest(@NotNull final String message, @NotNull final Object...args) {
        log(Level.FINEST, message, args);
    }

    /**
     * Config level logging.  Use for messages that should be INFO level but have the option to be disabled
     * via debug level -1.
     *
     * @param message Message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    public final void config(@NotNull final String message, @NotNull final Object...args) {
        log(Level.CONFIG, message, args);
    }

    /**
     * Info level logging.
     *
     * @param message Message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    public final void info(@NotNull final String message, @NotNull final Object...args) {
        log(Level.INFO, message, args);
    }

    /**
     * Warning level logging.
     *
     * @param message Message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    public final void warning(@NotNull final String message, @NotNull final Object...args) {
        log(Level.WARNING, message, args);
    }

    /**
     * Severe level logging.
     *
     * @param message Message to log.
     * @param args    Arguments for the String.format() that is applied to the message.
     */
    public final void severe(@NotNull final String message, @NotNull final Object...args) {
        log(Level.SEVERE, message, args);
    }

}
