/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.logging;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Maintains a connection to a file debug log for writing debug messages to from PluginLogger.
 */
class DebugLog {

    /** Represents the original debug level. */
    static final int ORIGINAL_DEBUG_LEVEL = 0;

    /** The debug level for this instance. */
    volatile int debugLevel = ORIGINAL_DEBUG_LEVEL;

    /**
     * Returns a new DebugLog for use with a PluginLogger.
     *
     * @param logger The logger this debug logger should be linked to.
     * @param fileName The file name where a file copy of the log will be placed.
     * @return A new debug log.
     */
    static DebugLog getDebugLog(final Logger logger, final String fileName) {
        return new DebugLog(logger, fileName);
    }

    /**
     * Returns the file name set for this {@link com.dumptruckman.minecraft.pluginbase.logging.DebugLog}.
     *
     * @return the file name set for this {@link com.dumptruckman.minecraft.pluginbase.logging.DebugLog}.
     */
    public synchronized String getFileName() {
        return fileName;
    }

    /**
     * Sets the debug level for this logger.
     *
     * PluginLogger stores its debug level in this debug logger so that plugins sharing the debug log can share a
     * debug level setting.
     *
     * @param debugLevel The debug level to use.  Must be 0-3.
     */
    public void setDebugLevel(final int debugLevel) {
        if (debugLevel < 0 || debugLevel > 3) {
            throw new IllegalArgumentException("Debug level must be between 0 and 3");
        }
        this.debugLevel = debugLevel;
    }

    /**
     * Gets the debug level for this debug logger.
     *
     * @return The debug level for this debug logger.
     */
    public int getDebugLevel() {
        return debugLevel;
    }

    private FileHandler fileHandler = null;
    /** The Logger associated with this DebugLog. */
    final Logger log;

    private final String fileName;

    /**
     * Creates a new debug logger.
     *
     * @param logger The name of the logger.
     * @param file   The file to log to.
     */
    private DebugLog(final Logger logger, final String file) {
        this.log = logger;
        this.fileName = file;
    }

    /**
     * Opens a new file handle for this debug logger so that messages will be logged in the file.
     */
    public final synchronized void open() {
        try {
            fileHandler = new FileHandler(fileName, true);
            Set<Handler> toRemove = new HashSet<Handler>(log.getHandlers().length);
            Collections.addAll(toRemove, log.getHandlers());
            for (Handler handler : toRemove) {
                log.removeHandler(handler);
            }
            log.addHandler(fileHandler);
            log.setLevel(Level.ALL);
            fileHandler.setFormatter(new LogFormatter());
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs a log record to the debug file.
     *
     * @param record The log record to record to file.
     */
    public void log(final LogRecord record) {
        log.log(record);
    }

    /**
     * Checks whether or not the debug file handle is open or close.
     *
     * @return True if the
     */
    public boolean isClosed() {
        return fileHandler == null;
    }

    /**
     * Log a message at a certain level.
     *
     * @param level The log-{@link java.util.logging.Level}.
     * @param msg the message.
     */
    public void log(final Level level, final String msg) {
        log(new LogRecord(level, msg));
    }

    /**
     * Our log-{@link java.util.logging.Formatter}.
     */
    private static class LogFormatter extends Formatter {
        private final SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        @Override
        public String format(final LogRecord record) {
            final StringBuilder builder = new StringBuilder();
            final Throwable ex = record.getThrown();

            builder.append(this.date.format(record.getMillis()));
            builder.append(" [");
            builder.append(record.getLevel().getLocalizedName().toUpperCase());
            builder.append("] ");
            builder.append(record.getMessage());
            builder.append('\n');

            if (ex != null) {
                final StringWriter writer = new StringWriter();
                ex.printStackTrace(new PrintWriter(writer));
                builder.append(writer);
            }

            return builder.toString();
        }
    }

    /**
     * Closes this {@link com.dumptruckman.minecraft.pluginbase.logging.DebugLog}.
     */
    public synchronized void close() {
        if (fileHandler != null) {
            log.removeHandler(fileHandler);
            fileHandler.close();
            fileHandler = null;
        }
    }
}
