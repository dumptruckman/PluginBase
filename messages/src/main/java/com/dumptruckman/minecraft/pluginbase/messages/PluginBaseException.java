/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.messages;

import com.dumptruckman.minecraft.pluginbase.logging.Formatter;
import com.dumptruckman.minecraft.pluginbase.logging.PluginLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

/**
 * The main exception class for PluginBase.
 * <p/>
 * This exception allows for localized messages via {@link BundledMessage}.
 */
public class PluginBaseException extends Exception {

    @NotNull
    private final BundledMessage languageMessage;

    @Nullable
    private PluginBaseException cause;

    /**
     * Constructs a PluginBase exception with the given bundled message.
     *
     * @param languageMessage the bundled message to use for this exception.
     */
    public PluginBaseException(@NotNull final BundledMessage languageMessage) {
        super(getUnbundledDefaultMessage(languageMessage));
        this.languageMessage = languageMessage;
    }

    /**
     * Constructs a PluginBase exception with the given bundled message.
     *
     * @param languageMessage the bundled message to use for this exception.
     */
    public PluginBaseException(@NotNull final BundledMessage languageMessage, @Nullable final Throwable throwable) {
        super(getUnbundledDefaultMessage(languageMessage), throwable);
        if (throwable instanceof PluginBaseException) {
            this.cause = (PluginBaseException) throwable;
        }
        this.languageMessage = languageMessage;
    }

    /**
     * Creates a PluginBase exception with the given bundled message that was caused by another PluginBase exception.
     * <p/>
     * This other exception will be retrievable via {@link #getCauseException()} and {@link #getCause()}.
     *
     * @param languageMessage the bundled message to use for this exception.
     * @param cause the cause exception.
     */
    public PluginBaseException(@NotNull final BundledMessage languageMessage, @Nullable final PluginBaseException cause) {
        this(languageMessage, (Throwable) cause);
        this.cause = cause;
    }

    /**
     * Copy constructor for PluginBase exceptions.
     *
     * @param e The exception to copy.
     */
    public PluginBaseException(@NotNull final PluginBaseException e) {
        this(e.getBundledMessage(), e.getCause());
    }

    /**
     * Copy constructor for generic exceptions.
     *
     * @param e The exception to copy.
     */
    public PluginBaseException(@NotNull final Exception e) {
        this(Message.bundleMessage(Messages.EXCEPTION, e.getMessage()), e.getCause());
    }

    /**
     * Gets the {@link BundledMessage} used this in exception.
     *
     * @return The {@link BundledMessage} used this in exception.
     */
    @NotNull
    public BundledMessage getBundledMessage() {
        return this.languageMessage;
    }

    /**
     * Gets the PluginBase exception that caused this exception if any.
     *
     * @return the PluginBase exception that caused this exception if any.
     */
    @Nullable
    public PluginBaseException getCauseException() {
        return this.cause;
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public String getMessage() {
        return super.getMessage();    //To change body of overridden methods use File | Settings | File Templates.
    }

    /**
     * Logs the exception to the given logger at the given level.
     * <p/>
     * This logs the message of the original exception and all caused by exceptions when possible.
     *
     * @param logger the logger to log to.
     * @param level the level to log at.
     */
    public void logException(@NotNull final PluginLogger logger, @NotNull final Level level) {
        logger.log(level, toString());
        final Throwable cause = getCause();
        if (cause != null) {
            logCauseException(cause, logger, level);
        }
    }

    private static void logCauseException(@NotNull final Throwable cause, @NotNull final PluginLogger logger, @NotNull final Level level) {
        logger.log(level, "Caused by: " + cause.toString());
        final Throwable newCause = cause.getCause();
        if (newCause != null) {
            logCauseException(newCause, logger, level);
        }
    }

    /**
     * Logs the exception along with its stack trace to the given logger at the given level.
     * <p/>
     * This logs the message and stack trace of the original exception and all caused by exceptions when possible.
     *
     * @param logger the logger to log to.
     * @param level the level to log at.
     */
    public void logExceptionWithStackTrace(@NotNull final PluginLogger logger, @NotNull final Level level) {
        logger.log(level, toString());
        for (final StackTraceElement stackElement : getStackTrace()) {
            logger.log(level, stackElement.toString());
        }
        final Throwable cause = getCause();
        if (cause != null) {
            logCauseExceptionWithStackTrace(cause, logger, level);
        }
    }

    private static void logCauseExceptionWithStackTrace(@NotNull final Throwable cause, @NotNull final PluginLogger logger, @NotNull final Level level) {
        logger.log(level, "Caused by: " + cause.toString());
        for (final StackTraceElement stackElement : cause.getStackTrace()) {
            logger.log(level, stackElement.toString());
        }
        final Throwable newCause = cause.getCause();
        if (newCause != null) {
            logCauseExceptionWithStackTrace(newCause, logger, level);
        }
    }

    protected static String getUnbundledDefaultMessage(@NotNull final BundledMessage message) {
        return Formatter.format(ChatColor.translateAlternateColorCodes('&', message.getMessage().getDefault()), message.getArgs());
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public String toString() {
        return getClass().getName() + ": " + getMessage();
    }
}
