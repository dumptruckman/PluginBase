/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.bukkit;

import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import com.dumptruckman.minecraft.pluginbase.messages.BundledMessage;
import com.dumptruckman.minecraft.pluginbase.messages.Message;
import com.dumptruckman.minecraft.pluginbase.messages.messaging.MessageReceiver;
import com.dumptruckman.minecraft.pluginbase.messages.messaging.SimpleMessager;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.util.ChatPaginator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

/**
 * A Bukkit specific implementation of {@link com.dumptruckman.minecraft.pluginbase.messages.messaging.Messager}.
 * <p/>
 * Provides word wrapping on messages too long to fit on one line.
 * <p/>
 * Please refer to {@link com.dumptruckman.minecraft.pluginbase.messages.messaging.Messager} for javadoc for the methods in this class.  This class merely adds
 * convenience methods for Bukkit CommandSenders.
 */
public class BukkitMessager extends SimpleMessager {

    BukkitMessager(@NotNull final File dataFolder) {
        super(dataFolder);
    }

    /** {@inheritDoc} */
    @Override
    public void message(@NotNull final MessageReceiver sender, @NotNull final String message) {
        sendMessages(sender, ChatPaginator.wordWrap(message, ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH));
    }

    protected void send(@NotNull final CommandSender sender,
                        @Nullable final String prefix,
                        @NotNull final Message message,
                        @NotNull final Object... args) {
        String string = getMessage(message, args);
        if (prefix != null && !prefix.isEmpty()) {
            string = prefix + " " + string;
        }
        message(sender, string);
    }

    public void message(@NotNull final CommandSender sender, @NotNull final String message) {
        sendMessages(sender, ChatPaginator.wordWrap(message, ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH));
    }

    public void message(@NotNull final CommandSender sender, @NotNull final Message message, @NotNull final Object... args) {
        send(sender, "", message, args);
    }

    public void message(@NotNull final CommandSender sender, @NotNull final BundledMessage message) {
        message(sender, message.getMessage(), message.getArgs());
    }

    public void message(@NotNull final CommandSender player, @NotNull final List<String> messages) {
        for (String s : messages) {
            player.sendMessage(s);
        }
    }

    public void messageSuccess(@NotNull final CommandSender sender, @NotNull final Message message, @NotNull final Object... args) {
        send(sender, getMessage(SUCCESS), message, args);
    }

    public void messageSuccess(@NotNull final CommandSender sender, @NotNull final BundledMessage message) {
        send(sender, getMessage(SUCCESS), message.getMessage(), message.getArgs());
    }

    public void messageSuccess(@NotNull final CommandSender sender, @NotNull final String message) {
        message(sender, getMessage(SUCCESS) + " " + message);
    }

    public void messageError(@NotNull final CommandSender sender, @NotNull final Message message, @NotNull final Object... args) {
        send(sender, getMessage(ERROR), message, args);
    }

    public void messageError(@NotNull final CommandSender sender, @NotNull final BundledMessage message) {
        send(sender, getMessage(ERROR), message.getMessage(), message.getArgs());
    }

    public void messageError(@NotNull final CommandSender sender, @NotNull final String message) {
        message(sender, getMessage(ERROR) + " " + message);
    }

    protected void sendMessages(@NotNull final CommandSender player, @NotNull final String[] messages) {
        player.sendMessage(messages);
    }

    public void messageAndLog(@NotNull final CommandSender sender, @NotNull final Message message, @NotNull final Object... args) {
        if (!(sender instanceof ConsoleCommandSender)) {
            message(sender, message, args);
        }
        Logging.info(getMessage(message, args));
    }
}

