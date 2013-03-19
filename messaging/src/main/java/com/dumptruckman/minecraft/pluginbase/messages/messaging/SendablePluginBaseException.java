/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.messages.messaging;

import com.dumptruckman.minecraft.pluginbase.messages.BundledMessage;
import com.dumptruckman.minecraft.pluginbase.messages.Message;
import com.dumptruckman.minecraft.pluginbase.messages.Messages;
import com.dumptruckman.minecraft.pluginbase.messages.PluginBaseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A extension of the basic {@link PluginBaseException} that adds ease of use methods for sending the exception to
 * a {@link MessageReceiver}.
 */
public class SendablePluginBaseException extends PluginBaseException {

    /**
     * Constructs a PluginBase exception with the given bundled message.
     *
     * @param languageMessage the bundled message to use for this exception.
     */
    public SendablePluginBaseException(@NotNull final BundledMessage languageMessage) {
        super(languageMessage);
    }

    /**
     * Constructs a PluginBase exception with the given bundled message.
     *
     * @param languageMessage the bundled message to use for this exception.
     */
    public SendablePluginBaseException(@NotNull final BundledMessage languageMessage, @NotNull final Throwable throwable) {
        super(languageMessage, throwable);
    }

    /**
     * Creates a PluginBase exception with the given bundled message that was caused by another PluginBase exception.
     * <p/>
     * This other exception will be retrievable via {@link #getCauseException()} and {@link #getCause()}.
     *
     * @param languageMessage the bundled message to use for this exception.
     * @param cause the cause exception.
     */
    public SendablePluginBaseException(@NotNull final BundledMessage languageMessage, @NotNull final PluginBaseException cause) {
        super(languageMessage, cause);
    }

    /**
     * Copy constructor for PluginBase exceptions.
     *
     * @param e The exception to copy.
     */
    public SendablePluginBaseException(@NotNull final PluginBaseException e) {
        super(e);
    }

    /**
     * Sends the exception details to the specified receiver via the specified messager.
     *
     * @param messager the messager to use to send the message.
     * @param receiver the message receiver.
     */
    public void sendException(@NotNull final Messager messager, @NotNull final MessageReceiver receiver) {
        messager.message(receiver, getBundledMessage());
        final PluginBaseException cause = getCauseException();
        final Throwable throwable = getCause();
        if (cause != null) {
            sendCauseException(cause, messager, receiver);
        } else if (throwable != null) {
            sendCauseException(throwable, messager, receiver);
        }
    }

    private static void sendCauseException(@NotNull final Throwable throwable, @NotNull final Messager messager, @NotNull final MessageReceiver receiver) {
        if (throwable instanceof PluginBaseException) {
            PluginBaseException cause = (PluginBaseException) throwable;
            final BundledMessage bMessage = cause.getBundledMessage();
            final String message = messager.getLocalizedMessage(bMessage.getMessage(), bMessage.getArgs());
            final BundledMessage newMessage = Message.bundleMessage(Messages.CAUSE_EXCEPTION, message);
            messager.message(receiver, newMessage);
            final PluginBaseException newCause = cause.getCauseException();
            final Throwable tCause = cause.getCause();
            if (newCause != null) {
                sendCauseException(newCause, messager, receiver);
            } else if (tCause != null) {
                sendCauseException(tCause, messager, receiver);
            }
        } else {
            final BundledMessage newMessage = Message.bundleMessage(Messages.CAUSE_EXCEPTION, throwable.getMessage());
            messager.message(receiver, newMessage);
            final Throwable tCause = throwable.getCause();
            if (tCause != null) {
                sendCauseException(tCause, messager, receiver);
            }
        }
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public String toString() {
        return toString(Messager.requestMessager());
    }

    private String toString(@Nullable final Messager messager) {
        String message;
        if (messager != null) {
            message = messager.getLocalizedMessage(getBundledMessage().getMessage(), getBundledMessage().getArgs());
        } else {
            message = getMessage();
        }
        return getClass().getName() + ": " + message;
    }
}
