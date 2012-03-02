package com.dumptruckman.minecraft.plugin.command;

import com.dumptruckman.minecraft.locale.CommandMessages;
import com.dumptruckman.minecraft.permission.Perm;
import com.dumptruckman.minecraft.plugin.AbstractBukkitPlugin;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Enables debug-information.
 */
public class ReloadCommand<P extends AbstractBukkitPlugin> extends PluginCommand<P> {

    public ReloadCommand(P plugin) {
        super(plugin);
        setName(messager.getMessage(CommandMessages.RELOAD_NAME));
        setCommandUsage("/" + plugin.getCommandPrefixes().get(0) + " reload");
        setArgRange(0, 0);
        for (String prefix : (List<String>) plugin.getCommandPrefixes()) {
            this.addKey(prefix + " reload");
            this.addKey(prefix + "reload");
        }
        setPermission(Perm.COMMAND_RELOAD.getPermission());
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        this.plugin.reloadConfig();
        this.messager.normal(CommandMessages.RELOAD_COMPLETE, sender);
    }
}
