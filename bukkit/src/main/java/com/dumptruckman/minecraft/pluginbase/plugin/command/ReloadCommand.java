package com.dumptruckman.minecraft.pluginbase.plugin.command;

import com.dumptruckman.minecraft.pluginbase.locale.CommandMessages;
import com.dumptruckman.minecraft.pluginbase.permission.Perm;
import com.dumptruckman.minecraft.pluginbase.plugin.AbstractBukkitPlugin;
import org.bukkit.command.CommandSender;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Enables debug-information.
 */
public class ReloadCommand<P extends AbstractBukkitPlugin> extends PluginCommand<P> {

    private static Set<String> staticKeys = new LinkedHashSet<String>();
    private static Set<String> staticPrefixedKeys = new LinkedHashSet<String>();

    public static void addStaticKey(String key) {
        staticKeys.add(key);
    }

    public static void addStaticPrefixedKey(String key) {
        staticPrefixedKeys.add(key);
    }

    public ReloadCommand(P plugin) {
        super(plugin);
        setName(getMessager().getMessage(CommandMessages.RELOAD_NAME));
        setCommandUsage("/" + plugin.getCommandPrefixes().get(0) + " reload");
        setArgRange(0, 0);
        for (String key : staticKeys) {
            this.addKey(key);
        }
        for (String key : staticPrefixedKeys) {
            this.addPrefixedKey(key);
        }
        this.addPrefixedKey(" reload");
        setPermission(Perm.COMMAND_RELOAD.getPermission());
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        this.plugin.reloadConfig();
        this.getMessager().normal(CommandMessages.RELOAD_COMPLETE, sender);
    }
}
