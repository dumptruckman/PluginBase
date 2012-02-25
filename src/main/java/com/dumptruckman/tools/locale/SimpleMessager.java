package com.dumptruckman.tools.locale;

import com.dumptruckman.tools.plugin.PluginBase;
import com.dumptruckman.tools.util.Font;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Implementation of a Messager and MessageProvider using SimpleMessageProvider to implement the latter.
 */
public class SimpleMessager extends SimpleMessageProvider implements Messager, MessageProvider {

    public SimpleMessager(PluginBase plugin) {
        super(plugin);
    }

    private void send(Message message, String prefix, CommandSender sender, Object... args) {
        List<String> messages = this.getMessages(message, args);
        if (!messages.isEmpty()) {
            messages.set(0, prefix + " " + messages.get(0));
            sendMessages(sender, messages);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bad(Message message, CommandSender sender, Object... args) {
        send(message, ChatColor.RED.toString() + this.getMessage(Messages.GENERIC_ERROR), sender, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void normal(Message message, CommandSender sender, Object... args) {
        send(message, "", sender, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void good(Message message, CommandSender sender, Object... args) {
        send(message, ChatColor.GREEN.toString() + this.getMessage(Messages.GENERIC_SUCCESS), sender, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void info(Message message, CommandSender sender, Object... args) {
        send(message, ChatColor.YELLOW.toString() + this.getMessage(Messages.GENERIC_INFO), sender, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void help(Message message, CommandSender sender, Object... args) {
        send(message, ChatColor.GRAY.toString() + this.getMessage(Messages.GENERIC_HELP), sender, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(CommandSender player, String message) {
        List<String> messages = Font.splitString(message);
        sendMessages(player, messages);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessages(CommandSender player, List<String> messages) {
        for (String s : messages) {
            player.sendMessage(s);
        }
    }
}

