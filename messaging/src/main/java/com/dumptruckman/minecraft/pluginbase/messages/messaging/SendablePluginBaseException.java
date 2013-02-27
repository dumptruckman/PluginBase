/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.messages.messaging;

import com.dumptruckman.minecraft.pluginbase.messages.BundledMessage;
import com.dumptruckman.minecraft.pluginbase.messages.PluginBaseException;
import org.jetbrains.annotations.NotNull;

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
        messager.message(receiver, getBundledMessage().getMessage(), getBundledMessage().getArgs());
        if (getCauseException() != null) {
            if (getCauseException() instanceof SendablePluginBaseException) {
                ((SendablePluginBaseException) getCauseException()).sendException(messager, receiver);
            } else {
                messager.message(receiver, getCauseException().getMessage());
            }
        } else if (getCause() != null) {
            messager.message(receiver, getCause().getMessage());
        }
    }
}
