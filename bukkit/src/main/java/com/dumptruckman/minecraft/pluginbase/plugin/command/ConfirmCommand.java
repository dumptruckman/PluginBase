package com.dumptruckman.minecraft.pluginbase.plugin.command;

import com.dumptruckman.minecraft.pluginbase.config.BaseConfig;
import com.dumptruckman.minecraft.pluginbase.locale.CommandMessages;
import com.dumptruckman.minecraft.pluginbase.locale.Messages;
import com.dumptruckman.minecraft.pluginbase.permission.Perm;
import com.dumptruckman.minecraft.pluginbase.plugin.AbstractBukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.util.Logging;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Enables debug-information.
 */
public class ConfirmCommand<P extends AbstractBukkitPlugin> extends PluginCommand<P> {

    private static Set<String> staticKeys = new LinkedHashSet<String>();
    private static Set<String> staticPrefixedKeys = new LinkedHashSet<String>();

    public static void addStaticKey(String key) {
        staticKeys.add(key);
    }

    public static void addStaticPrefixedKey(String key) {
        staticPrefixedKeys.add(key);
    }

    public ConfirmCommand(P plugin) {
        super(plugin);
        this.setName(getMessager().getMessage(CommandMessages.CONFIRM_NAME));
        this.setCommandUsage("/" + plugin.getCommandPrefixes().get(0) + " confirm");
        this.setArgRange(0, 0);
        for (String key : staticKeys) {
            this.addKey(key);
        }
        for (String key : staticPrefixedKeys) {
            this.addPrefixedKey(key);
        }
        this.addPrefixedKey(" confirm");
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " confirm");
        this.setPermission(Perm.COMMAND_CONFIRM.getPermission());
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        plugin.getCommandHandler().confirmQueuedCommand(sender);
    }
}
