package com.dumptruckman.tools.locale;

import com.dumptruckman.tools.util.Font;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * Implementation of a Messager and MessageProvider using SimpleMessageProvider to implement the latter.
 */
public class SimpleMessager extends SimpleMessageProvider implements Messager, MessageProvider {

    public SimpleMessager(JavaPlugin plugin) {
        super(plugin);
    }

    private void send(BaseMessages message, String prefix, CommandSender sender, Object... args) {
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
    public void bad(BaseMessages message, CommandSender sender, Object... args) {
        send(message, ChatColor.RED.toString() + this.getMessage(BaseMessages.GENERIC_ERROR), sender, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void normal(BaseMessages message, CommandSender sender, Object... args) {
        send(message, "", sender, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void good(BaseMessages message, CommandSender sender, Object... args) {
        send(message, ChatColor.GREEN.toString() + this.getMessage(BaseMessages.GENERIC_SUCCESS), sender, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void info(BaseMessages message, CommandSender sender, Object... args) {
        send(message, ChatColor.YELLOW.toString() + this.getMessage(BaseMessages.GENERIC_INFO), sender, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void help(BaseMessages message, CommandSender sender, Object... args) {
        send(message, ChatColor.GRAY.toString() + this.getMessage(BaseMessages.GENERIC_HELP), sender, args);
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

