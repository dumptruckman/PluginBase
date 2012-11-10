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
package com.dumptruckman.minecraft.logging;

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
class DebugLog {

    static final int ORIGINAL_DEBUG_LEVEL = 0;

    volatile int debugLevel = ORIGINAL_DEBUG_LEVEL;

    /**
     * Initializes the {@link com.dumptruckman.minecraft.logging.DebugLog} the first time this is called with the information passed in.  The DebugLog must be
     * initializes before use.
     *
     * @param logger The logger this debug logger should be linked to.
     * @param fileName The file name where a file copy of the log will be placed.
     */
    static DebugLog getDebugLog(final Logger logger, final String fileName) {
        return new DebugLog(logger, fileName);
    }

    Logger getLogger() {
        return this.log;
    }

    /**
     * Returns the file name set for this {@link com.dumptruckman.minecraft.logging.DebugLog}.
     *
     * @return the file name set for this {@link com.dumptruckman.minecraft.logging.DebugLog}.
     */
    public synchronized String getFileName() {
        return fileName;
    }

    public void setDebugLevel(final int debugLevel) {
        this.debugLevel = debugLevel;
    }

    public int getDebugLevel() {
        return debugLevel;
    }

    /**
     * The FileHandler for file logging purposes.
     */
    protected FileHandler fileHandler = null;
    /**
     * The Logger associated with this DebugLog.
     */
    protected final Logger log;

    private final String fileName;

    /**
     * Creates a new debug logger.
     *
     * @param logger The name of the logger.
     * @param file   The file to log to.
     */
    protected DebugLog(final Logger logger, final String file) {
        this.log = logger;
        this.fileName = file;
    }

    public final synchronized void open() {
        try {
            fileHandler = new FileHandler(fileName, true);
            log.setUseParentHandlers(false);
            Set<Handler> toRemove = new HashSet<Handler>(log.getHandlers().length);
            for (Handler handler : log.getHandlers()) {
                toRemove.add(handler);
            }
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

    public void log(final LogRecord record) {
        log.log(record);
    }

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
     * Closes this {@link com.dumptruckman.minecraft.logging.DebugLog}.
     */
    public synchronized void close() {
        if (fileHandler != null) {
            log.removeHandler(fileHandler);
            fileHandler.close();
            fileHandler = null;
        }
    }
}
