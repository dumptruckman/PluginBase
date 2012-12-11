/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.messaging;

import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;
import org.bukkit.util.ChatPaginator;

import java.util.List;

/**
 * Implementation of a Messager and MessageProvider using YamlMessageProvider to implement the latter.
 */
public class SimpleMessager extends YamlMessageProvider implements Messager, MessageProvider {

    public SimpleMessager(PluginBase plugin) {
        super(plugin);
    }

    private void send(MessageReceiver sender, String prefix, Message message, Object... args) {
        List<String> messages = this.getMessages(message, args);
        if (!messages.isEmpty()) {
            if (prefix.isEmpty()) {
                messages.set(0, messages.get(0));
            } else {
                messages.set(0, prefix + " " + messages.get(0));
            }

            message(sender, messages);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void message(MessageReceiver sender, Message message, Object... args) {
        send(sender, "", message, args);
    }

    @Override
    public void message(MessageReceiver sender, BundledMessage message) {
        message(sender, message.getMessage(), message.getArgs());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void message(MessageReceiver sender, String message) {
        String[] messages = ChatPaginator.wordWrap(message, ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH);
        sendMessages(sender, messages);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void message(MessageReceiver player, List<String> messages) {
        for (String s : messages) {
            player.sendMessage(s);
        }
    }

    private void sendMessages(MessageReceiver player, String[] messages) {
        for (String s : messages) {
            player.sendMessage(s);
        }
    }

    @Override
    public void messageAndLog(MessageReceiver sender, Message message, Object... args) {
        if (sender.isPlayer()) {
            message(sender, message, args);
        }
        Logging.info(getMessage(message, args));
    }
}

