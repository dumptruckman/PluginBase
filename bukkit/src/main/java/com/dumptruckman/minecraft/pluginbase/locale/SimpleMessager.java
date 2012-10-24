/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.locale;

import com.dumptruckman.minecraft.pluginbase.entity.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;
import org.bukkit.ChatColor;
import org.bukkit.util.ChatPaginator;

import java.util.List;

/**
 * Implementation of a Messager and MessageProvider using SimpleMessageProvider to implement the latter.
 */
public class SimpleMessager extends SimpleMessageProvider implements Messager, MessageProvider {

    public SimpleMessager(PluginBase plugin) {
        super(plugin);
    }

    private void send(BasePlayer sender, String prefix, Message message, Object... args) {
        List<String> messages = this.getMessages(message, args);
        if (!messages.isEmpty()) {
            if (prefix.isEmpty()) {
                messages.set(0, messages.get(0));
            } else {
                messages.set(0, prefix + " " + messages.get(0));
            }

            sendMessages(sender, messages);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bad(BasePlayer sender, Message message, Object... args) {
        send(sender, ChatColor.RED.toString() + this.getMessage(Messages.GENERIC_ERROR), message, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void normal(BasePlayer sender, Message message, Object... args) {
        send(sender, "", message, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void good(BasePlayer sender, Message message, Object... args) {
        send(sender, ChatColor.GREEN.toString() + this.getMessage(Messages.GENERIC_SUCCESS), message, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void info(BasePlayer sender, Message message, Object... args) {
        send(sender, ChatColor.YELLOW.toString() + this.getMessage(Messages.GENERIC_INFO), message, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void help(BasePlayer sender, Message message, Object... args) {
        send(sender, ChatColor.GRAY.toString() + this.getMessage(Messages.GENERIC_HELP), message, args);
    }

    @Override
    public void bad(BasePlayer sender, BundledMessage message) {
        bad(sender, message.getMessage(), message.getArgs());
    }

    @Override
    public void normal(BasePlayer sender, BundledMessage message) {
        normal(sender, message.getMessage(), message.getArgs());
    }

    @Override
    public void good(BasePlayer sender, BundledMessage message) {
        good(sender, message.getMessage(), message.getArgs());
    }

    @Override
    public void info(BasePlayer sender, BundledMessage message) {
        info(sender, message.getMessage(), message.getArgs());
    }

    @Override
    public void help(BasePlayer sender, BundledMessage message) {
        help(sender, message.getMessage(), message.getArgs());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(BasePlayer sender, String message) {
        String[] messages = ChatPaginator.wordWrap(message, ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH);
        sendMessages(sender, messages);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessages(BasePlayer player, List<String> messages) {
        for (String s : messages) {
            player.sendMessage(s);
        }
    }

    private void sendMessages(BasePlayer player, String[] messages) {
        for (String s : messages) {
            player.sendMessage(s);
        }
    }
}

