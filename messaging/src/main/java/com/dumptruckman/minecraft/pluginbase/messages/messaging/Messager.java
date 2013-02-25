/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.messages.messaging;

import com.dumptruckman.minecraft.pluginbase.messages.BundledMessage;
import com.dumptruckman.minecraft.pluginbase.messages.Message;
import com.dumptruckman.minecraft.pluginbase.messages.MessageProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * This interface describes a Messager which sends messages to {@link MessageReceiver}s.
 */
public interface Messager extends MessageProvider {

    /**
     * Sends a message to the specified player with NO special prefix.
     *
     * @param sender  The entity to send the messages to.
     * @param message The message to send.
     * @param args    arguments for String.format().
     */
    void message(@NotNull final MessageReceiver sender, @NotNull final Message message, @NotNull final Object... args);

    /**
     * Sends a message to the specified player with NO special prefix.
     *
     * @param sender  The entity to send the messages to.
     * @param message The message to send.
     */
    void message(@NotNull final MessageReceiver sender, @NotNull final BundledMessage message);

    /**
     * Sends a message to a player that automatically takes words that are too long and puts them on a new line.
     *
     * @param player  Player to send message to.
     * @param message Message to send.
     */
    void message(@NotNull final MessageReceiver player, @NotNull final String message);

    /**
     * Sends a message to a player that automatically takes words that are too long and puts them on a new line.
     *
     * @param player   Player to send message to.
     * @param messages Messages to send.
     */
    void message(@NotNull final MessageReceiver player, @NotNull final List<String> messages);

    void messageSuccess(@NotNull final MessageReceiver sender, @NotNull final Message message, @NotNull final Object... args);

    void messageSuccess(@NotNull final MessageReceiver sender, @NotNull final BundledMessage message);

    void messageSuccess(@NotNull final MessageReceiver sender, @NotNull final String message);

    void messageError(@NotNull final MessageReceiver sender, @NotNull final Message message, @NotNull final Object... args);

    void messageError(@NotNull final MessageReceiver sender, @NotNull final BundledMessage message);

    void messageError(@NotNull final MessageReceiver sender, @NotNull final String message);

    void messageAndLog(@NotNull final MessageReceiver sender, @NotNull final Message message, @NotNull final Object... args);
}

