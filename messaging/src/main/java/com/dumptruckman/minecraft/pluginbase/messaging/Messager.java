/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.messaging;

import java.util.List;

/**
 * This interface describes a Messager which sends messages to CommandSenders.
 */
public interface Messager extends MessageProvider {

    /**
     * Sends a message to the specified player with NO special prefix.
     *
     * @param sender  The entity to send the messages to.
     * @param message The message to send.
     * @param args    arguments for String.format().
     */
    void message(MessageReceiver sender, Message message, Object... args);

    /**
     * Sends a message to the specified player with NO special prefix.
     *
     * @param sender  The entity to send the messages to.
     * @param message The message to send.
     */
    void message(MessageReceiver sender, BundledMessage message);

    /**
     * Sends a message to a player that automatically takes words that are too long and puts them on a new line.
     *
     * @param player  Player to send message to.
     * @param message Message to send.
     */
    void message(MessageReceiver player, String message);

    /**
     * Sends a message to a player that automatically takes words that are too long and puts them on a new line.
     *
     * @param player   Player to send message to.
     * @param messages Messages to send.
     */
    void message(MessageReceiver player, List<String> messages);

    void messageAndLog(MessageReceiver sender, Message message, Object... args);
}

