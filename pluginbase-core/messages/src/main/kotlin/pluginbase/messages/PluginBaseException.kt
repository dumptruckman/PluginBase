/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.messages

import pluginbase.logging.Formatter
import pluginbase.logging.PluginLogger

import java.util.logging.Level

/**
 * The main exception class for PluginBase.
 *
 * This exception allows for localized messages via [BundledMessage].
 */
open class PluginBaseException : Exception {

    /**
     * Gets the [BundledMessage] used this in exception.
     *
     * @return The [BundledMessage] used this in exception.
     */
    val bundledMessage: BundledMessage

    /**
     * Gets the PluginBase exception that caused this exception if any.
     *
     * @return the PluginBase exception that caused this exception if any.
     */
    var causeException: PluginBaseException? = null
        private set

    /**
     * Constructs a PluginBase exception with the given bundled message.
     *
     * @param languageMessage the bundled message to use for this exception.
     */
    constructor(languageMessage: BundledMessage) : super(getUnbundledDefaultMessage(languageMessage)) {
        this.bundledMessage = languageMessage
    }

    /**
     * Constructs a PluginBase exception with the given static message.
     *
     * @param message the exception message to send.
     */
    constructor(message: String) : super(message) {
        this.bundledMessage = Messages.createStaticMessage(message).bundle()
    }

    /**
     * Constructs a PluginBase exception with the given bundled message.
     *
     * @param languageMessage the bundled message to use for this exception.
     */
    constructor(languageMessage: BundledMessage, throwable: Throwable?)
            : super(getUnbundledDefaultMessage(languageMessage), throwable) {
        if (throwable is PluginBaseException) {
            this.causeException = throwable
        }
        this.bundledMessage = languageMessage
    }

    /**
     * Constructs a PluginBase exception with the given static message.
     *
     * @param message the exception message to send.
     */
    constructor(message: String, throwable: Throwable?) : super(message, throwable) {
        if (throwable is PluginBaseException) {
            this.causeException = throwable
        }
        this.bundledMessage = Messages.createStaticMessage(message).bundle()
    }

    /**
     * Creates a PluginBase exception with the given bundled message that was caused by another PluginBase exception.
     *
     * This other exception will be retrievable via [getCauseException] and [.getCause].
     *
     * @param languageMessage the bundled message to use for this exception.
     * @param cause the cause exception.
     */
    constructor(languageMessage: BundledMessage, cause: PluginBaseException?)
            : this(languageMessage, cause as Throwable?) {
        this.causeException = cause
    }

    /**
     * Creates a PluginBase exception with the given static message that was caused by another PluginBase exception.
     *
     * This other exception will be retrievable via [.getCauseException] and [.getCause].
     *
     * @param message the static message to use for this exception.
     * @param cause the cause exception.
     */
    constructor(message: String, cause: PluginBaseException?) : this(message, cause as Throwable?) {
        this.causeException = cause
    }

    /**
     * Copy constructor for PluginBase exceptions.
     *
     * @param e The exception to copy.
     */
    constructor(e: PluginBaseException) : this(e.bundledMessage, e.cause) {}

    /**
     * Copy constructor for generic exceptions.
     *
     * @param e The exception to copy.
     */
    constructor(e: Exception) : this(Messages.EXCEPTION.bundle(e.message), e.cause) {}

    /** {@inheritDoc}  */
    override val message: String?
        get() = super.message

    /**
     * Logs the exception to the given logger at the given level.
     *
     * This logs the message of the original exception and all caused by exceptions when possible.
     *
     * @param logger the logger to log to.
     * @param level the level to log at.
     */
    fun logException(logger: PluginLogger, level: Level) {
        logger.log(level, toString())
        val cause = cause
        if (cause != null) {
            logCauseException(cause, logger, level)
        }
    }

    /**
     * Logs the exception along with its stack trace to the given logger at the given level.
     *
     * This logs the message and stack trace of the original exception and all caused by exceptions when possible.
     *
     * @param logger the logger to log to.
     * @param level the level to log at.
     */
    fun logExceptionWithStackTrace(logger: PluginLogger, level: Level) {
        logger.log(level, toString())
        for (stackElement in stackTrace) {
            logger.log(level, stackElement.toString())
        }
        val cause = cause
        if (cause != null) {
            logCauseExceptionWithStackTrace(cause, logger, level)
        }
    }

    /** {@inheritDoc}  */
    override fun toString(): String {
        return javaClass.name + ": " + message
    }

    companion object {

        private fun logCauseException(cause: Throwable, logger: PluginLogger, level: Level) {
            logger.log(level, "Caused by: " + cause.toString())
            val newCause = cause.cause
            if (newCause != null) {
                logCauseException(newCause, logger, level)
            }
        }

        private fun logCauseExceptionWithStackTrace(cause: Throwable, logger: PluginLogger, level: Level) {
            logger.log(level, "Caused by: " + cause.toString())
            for (stackElement in cause.stackTrace) {
                logger.log(level, stackElement.toString())
            }
            val newCause = cause.cause
            if (newCause != null) {
                logCauseExceptionWithStackTrace(newCause, logger, level)
            }
        }

        protected fun getUnbundledDefaultMessage(message: BundledMessage): String {
            return Formatter.format(ChatColor.translateAlternateColorCodes('&', message.message.default), *message.args)
        }
    }
}
