/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.messaging;

import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.util.ChatPaginator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

/**
 * A Bukkit specific implementation of {@link Messager}.
 *
 * Provides word wrapping on messages too long to fit on one line.
 */
public class BukkitMessager extends SimpleMessager {

    public BukkitMessager(@NotNull File dataFolder) {
        super(dataFolder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void message(@NotNull MessageReceiver sender, @NotNull String message) {
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

    public void message(@NotNull CommandSender sender, @NotNull String message) {
        sendMessages(sender, ChatPaginator.wordWrap(message, ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH));
    }

    public void message(@NotNull CommandSender sender, @NotNull Message message, Object... args) {
        send(sender, "", message, args);
    }

    public void message(@NotNull CommandSender sender, @NotNull BundledMessage message) {
        message(sender, message.getMessage(), message.getArgs());
    }

    public void message(@NotNull CommandSender player, @NotNull List<String> messages) {
        for (String s : messages) {
            player.sendMessage(s);
        }
    }

    public void messageSuccess(CommandSender sender, Message message, Object... args) {
        send(sender, getMessage(SUCCESS), message, args);
    }

    public void messageSuccess(CommandSender sender, BundledMessage message) {
        send(sender, getMessage(SUCCESS), message.getMessage(), message.getArgs());
    }

    public void messageSuccess(CommandSender sender, String message) {
        message(sender, getMessage(SUCCESS) + " " + message);
    }

    public void messageError(CommandSender sender, Message message, Object... args) {
        send(sender, getMessage(ERROR), message, args);
    }

    public void messageError(CommandSender sender, BundledMessage message) {
        send(sender, getMessage(ERROR), message.getMessage(), message.getArgs());
    }

    public void messageError(CommandSender sender, String message) {
        message(sender, getMessage(ERROR) + " " + message);
    }

    protected void sendMessages(@NotNull CommandSender player, @NotNull String[] messages) {
        player.sendMessage(messages);
    }

    public void messageAndLog(@NotNull CommandSender sender, @NotNull Message message, @NotNull Object... args) {
        if (!(sender instanceof ConsoleCommandSender)) {
            message(sender, message, args);
        }
        Logging.info(getMessage(message, args));
    }
}

