package com.dumptruckman.minecraft.pluginbase.plugin.command;

import com.sk89q.bukkit.util.DynamicPluginCommand;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

class DefaultPluginCommand extends DynamicPluginCommand {

    public DefaultPluginCommand(String[] aliases, String desc, String usage, CommandExecutor owner, Object registeredWith, Plugin plugin) {
        super(aliases, desc, usage, owner, registeredWith, plugin);
    }

    @Override
    public boolean testPermissionSilent(CommandSender sender) {
        if (permissions == null || permissions.length == 0) {
            return true;
        }

        if (registeredWith instanceof BukkitCommandHandler) {
            try {
                for (String permission : permissions) {
                    if (((BukkitCommandHandler) registeredWith).hasPermission(sender, permission)) {
                        return true;
                    }
                }
                return false;
            } catch (Throwable ignore) {
            }
        }
        for (String permission : permissions) {
            if (sender.hasPermission(permission)) {
                return true;
            }
        }
        return false;
    }
}