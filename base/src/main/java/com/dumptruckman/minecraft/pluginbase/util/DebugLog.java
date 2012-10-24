/**
 * Copyright (c) 2012, The Multiverse Team All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of The Multiverse Team nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.dumptruckman.minecraft.pluginbase.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * The Multiverse debug-logger.
 */
public class DebugLog {

    static final int ORIGINAL_DEBUG_LEVEL = 0;

    private static String loggerName = null;
    private static String fileName = null;

    static volatile int debugLevel = ORIGINAL_DEBUG_LEVEL;

    /**
     * Initializes the {@link DebugLog} the first time this is called with the information passed in.  The DebugLog must be
     * initializes before use.
     *
     * @param loggerName The name of the logger to apply this DebugLog to.
     * @param fileName The file name where a file copy of the log will be placed.
     */
    public static synchronized void init(final String loggerName, final String fileName) {
        if (DebugLog.loggerName == null) {
            DebugLog.loggerName = loggerName;
            DebugLog.fileName = fileName;
        }
    }

    /**
     * Unitializes the {@link DebugLog} so that it may be reinitialized with new information.
     */
    public static synchronized void shutdown() {
        loggerName = null;
        fileName = null;
        debugLevel = ORIGINAL_DEBUG_LEVEL;
    }

    /**
     * Returns the logger name set for this {@link DebugLog}.
     *
     * @return the logger name set for this {@link DebugLog}.
     */
    public static synchronized String getLoggerName() {
        return loggerName;
    }

    /**
     * Returns the file name set for this {@link DebugLog}.
     *
     * @return the file name set for this {@link DebugLog}.
     */
    public static synchronized String getFileName() {
        return fileName;
    }

    public static void setDebugLevel(final int debugLevel) {
        DebugLog.debugLevel = debugLevel;
    }

    public static int getDebugLevel() {
        return debugLevel;
    }

    private static DebugLog instance = null;

    /**
     * Retrieves the open instance of DebugLog if one has already open or will open one and return it if not.
     *
     * @return The static instance of DebugLog.
     */
    public static synchronized DebugLog getDebugLogger() {
        if (instance == null) {
            if (loggerName == null) {
                throw new IllegalStateException("DebugLog has not been initialized!");
            }
            instance = new DebugLog(loggerName, fileName);
        }
        return instance;
    }

    /**
     * Returns whether their is an open instance of this {@link DebugLog}.
     *
     * @return true if there is an open instance of this {@link DebugLog}.
     */
    public static synchronized boolean isClosed() {
        return instance == null;
    }

    /**
     * The FileHandler for file logging purposes.
     */
    protected final FileHandler fileHandler;
    /**
     * The Logger associated with this DebugLog.
     */
    protected final Logger log;

    /**
     * Creates a new debug logger.
     *
     * @param logger The name of the logger.
     * @param file   The file to log to.
     */
    protected DebugLog(final String logger, final String file) {
        log = Logger.getLogger(logger);
        FileHandler fh = null;
        try {
            fh = new FileHandler(file, true);
            log.setUseParentHandlers(false);
            Set<Handler> toRemove = new HashSet<Handler>(log.getHandlers().length);
            for (Handler handler : log.getHandlers()) {
                toRemove.add(handler);
            }
            for (Handler handler : toRemove) {
                log.removeHandler(handler);
            }
            log.addHandler(fh);
            log.setLevel(Level.ALL);
            fh.setFormatter(new LogFormatter());
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (fh != null) {
            fileHandler = fh;
        } else {
            fileHandler = fh;
        }
    }

    public void log(final LogRecord record) {
        log.log(record);
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
     * Closes this {@link DebugLog}.
     */
    public synchronized void close() {
        log.removeHandler(fileHandler);
        fileHandler.close();
        instance = null;
    }
}