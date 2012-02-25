package com.dumptruckman.tools.plugin.command;

import com.dumptruckman.tools.locale.Messages;
import com.dumptruckman.tools.permission.Perm;
import com.dumptruckman.tools.plugin.AbstractPluginBase;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Enables debug-information.
 */
public class ReloadCommand<P extends AbstractPluginBase> extends PluginCommand<P> {

    public ReloadCommand(P plugin) {
        super(plugin);
        setName("Reloads config file");
        setCommandUsage("/" + plugin.getCommandPrefix() + " reload");
        setArgRange(0, 0);
        addKey(plugin.getCommandPrefix() + " reload");
        addKey(plugin.getCommandPrefix() + "reload");
        setPermission(Perm.COMMAND_RELOAD.getPermission());
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        this.plugin.reloadConfig();
        this.messager.normal(Messages.RELOAD_COMPLETE, sender);
    }
}
