package com.dumptruckman.minecraft.pluginbase.plugin.command;

import com.dumptruckman.minecraft.pluginbase.locale.BundledMessage;
import com.dumptruckman.minecraft.pluginbase.plugin.AbstractBukkitPlugin;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Allows for a command to be queued and require a confirm command.
 */
public abstract class QueuedPluginCommand<P extends AbstractBukkitPlugin> extends PluginCommand<P> {

    private BundledMessage confirmMessage = null;

    public QueuedPluginCommand(P plugin) {
        super(plugin);
    }

    public final void setConfirmMessage(BundledMessage message) {
        this.confirmMessage = message;
    }

    @Override
    public final void runCommand(CommandSender sender, List<String> args) {
        preConfirm(sender, args);
        this.plugin.getCommandHandler().queueCommand(sender, this, args, confirmMessage);
    }

    public abstract void preConfirm(CommandSender sender, List<String> args);

    public abstract void onConfirm(CommandSender sender, List<String> args);

    public abstract void onExpire(CommandSender sender, List<String> args);
}
