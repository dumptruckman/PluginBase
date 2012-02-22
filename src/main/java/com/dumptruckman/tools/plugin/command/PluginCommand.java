package com.dumptruckman.tools.plugin.command;

import com.dumptruckman.tools.plugin.AbstractPluginBase;
import com.dumptruckman.tools.locale.Messager;
import com.pneumaticraft.commandhandler.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * A generic Multiverse-command.
 */
public abstract class PluginCommand<P extends AbstractPluginBase> extends Command {

    /**
     * The reference to the core.
     */
    protected P plugin;
    /**
     * The reference to {@link Messager}.
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
