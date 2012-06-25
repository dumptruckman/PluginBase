package com.dumptruckman.minecraft.pluginbase.locale;

import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.util.ChatPaginator;

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

    @Override
    public void bad(BundledMessage message, CommandSender sender) {
        bad(message.getMessage(), sender, message.getArgs());
    }

    @Override
    public void normal(BundledMessage message, CommandSender sender) {
        normal(message.getMessage(), sender, message.getArgs());
    }

    @Override
    public void good(BundledMessage message, CommandSender sender) {
        good(message.getMessage(), sender, message.getArgs());
    }

    @Override
    public void info(BundledMessage message, CommandSender sender) {
        info(message.getMessage(), sender, message.getArgs());
    }

    @Override
    public void help(BundledMessage message, CommandSender sender) {
        help(message.getMessage(), sender, message.getArgs());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(CommandSender player, String message) {
        String[] messages = ChatPaginator.wordWrap(message, ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH);
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

    private void sendMessages(CommandSender player, String[] messages) {
        for (String s : messages) {
            player.sendMessage(s);
        }
    }
}

