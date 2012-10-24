/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.locale;

import com.dumptruckman.minecraft.pluginbase.entity.BasePlayer;

import java.util.List;

/**
 * This interface describes a Messager which sends messages to CommandSenders.
 */
public interface Messager extends MessageProvider {

    /**
     * Sends a message to the specified player with the generic ERROR prefix.
     *
     * @param sender  The entity to send the messages to.
     * @param message The message to send.
     * @param args    arguments for String.format().
     */
    void bad(BasePlayer sender, Message message, Object... args);

    /**
     * Sends a message to the specified player with the generic ERROR prefix.
     *
     * @param sender  The entity to send the messages to.
     * @param message The message to send.
     */
    void bad(BasePlayer sender, BundledMessage message);

    /**
     * Sends a message to the specified player with NO special prefix.
     *
     * @param sender  The entity to send the messages to.
     * @param message The message to send.
     * @param args    arguments for String.format().
     */
    void normal(BasePlayer sender, Message message, Object... args);

    /**
     * Sends a message to the specified player with NO special prefix.
     *
     * @param sender  The entity to send the messages to.
     * @param message The message to send.
     */
    void normal(BasePlayer sender, BundledMessage message);

    /**
     * Sends a message to the specified player with the generic SUCCESS prefix.
     *
     * @param sender  The entity to send the messages to.
     * @param message The message to send.
     * @param args    arguments for String.format().
     */
    void good(BasePlayer sender, Message message, Object... args);

    /**
     * Sends a message to the specified player with the generic SUCCESS prefix.
     *
     * @param sender  The entity to send the messages to.
     * @param message The message to send.
     */
    void good(BasePlayer sender, BundledMessage message);

    /**
     * Sends a message to the specified player with the generic INFO prefix.
     *
     * @param sender  The entity to send the messages to.
     * @param message The message to send.
     * @param args    arguments for String.format().
     */
    void info(BasePlayer sender, Message message, Object... args);

    /**
     * Sends a message to the specified player with the generic INFO prefix.
     *
     * @param sender  The entity to send the messages to.
     * @param message The message to send.
     */
    void info(BasePlayer sender, BundledMessage message);

    /**
     * Sends a message to the specified player with the generic HELP prefix.
     *
     * @param sender  The entity to send the messages to.
     * @param message The message to send.
     * @param args    arguments for String.format().
     */
    void help(BasePlayer sender, Message message, Object... args);

    /**
     * Sends a message to the specified player with the generic HELP prefix.
     *
     * @param sender  The entity to send the messages to.
     * @param message The message to send.
     */
    void help(BasePlayer sender, BundledMessage message);

    /**
     * Sends a message to a player that automatically takes words that are too long and puts them on a new line.
     *
     * @param player  Player to send message to.
     * @param message Message to send.
     */
    void sendMessage(BasePlayer player, String message);

    /**
     * Sends a message to a player that automatically takes words that are too long and puts them on a new line.
     *
     * @param player   Player to send message to.
     * @param messages Messages to send.
     */
    void sendMessages(BasePlayer player, List<String> messages);
}

