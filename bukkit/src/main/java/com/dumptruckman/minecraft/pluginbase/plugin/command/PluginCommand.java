package com.dumptruckman.minecraft.pluginbase.plugin.command;

import com.dumptruckman.minecraft.pluginbase.locale.Messager;
import com.dumptruckman.minecraft.pluginbase.plugin.AbstractBukkitPlugin;
import com.pneumaticraft.commandhandler.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * A generic Multiverse-command.
 */
public abstract class PluginCommand<P extends AbstractBukkitPlugin> extends Command {

    /**
     * The reference to the core.
     */
    protected P plugin;
    /**
     * The reference to {@link com.dumptruckman.minecraft.pluginbase.locale.Messager}.
     */
    protected Messager messager;

    public PluginCommand(P plugin) {
        super(plugin);
        this.plugin = plugin;
        this.messager = this.plugin.getMessager();
    }

    @Override
    public abstract void runCommand(CommandSender sender, List<String> args);
}
