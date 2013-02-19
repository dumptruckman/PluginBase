package com.dumptruckman.minecraft.pluginbase.bukkit;

import com.sk89q.util.StringUtil;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

class DynamicPluginCommand extends org.bukkit.command.Command implements PluginIdentifiableCommand {

    protected final CommandExecutor owner;
    protected final Object registeredWith;
    protected final Plugin owningPlugin;
    protected String[] permissions = new String[0];

    public DynamicPluginCommand(String[] aliases, String desc, String usage, CommandExecutor owner, Object registeredWith, Plugin plugin) {
        super(aliases[0], desc, usage, Arrays.asList(aliases));
        this.owner = owner;
        this.owningPlugin = plugin;
        this.registeredWith = registeredWith;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        return owner.onCommand(sender, this, label, args);
    }

    public void setPermissions(String[] permissions) {
        this.permissions = permissions;
        if (permissions != null) {
            super.setPermission(StringUtil.joinString(permissions, ";"));
        }
    }

    @Override
    public Plugin getPlugin() {
        return owningPlugin;
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
