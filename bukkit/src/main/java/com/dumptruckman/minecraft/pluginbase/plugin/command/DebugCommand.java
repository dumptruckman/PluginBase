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
public class DebugCommand<P extends AbstractBukkitPlugin> extends PluginCommand<P> {

    private static Set<String> staticKeys = new LinkedHashSet<String>();
    private static Set<String> staticPrefixedKeys = new LinkedHashSet<String>();

    public static void addStaticKey(String key) {
        staticKeys.add(key);
    }

    public static void addStaticPrefixedKey(String key) {
        staticPrefixedKeys.add(key);
    }

    public DebugCommand(P plugin) {
        super(plugin);
        this.setName(messager.getMessage(CommandMessages.DEBUG_NAME));
        this.setCommandUsage(messager.getMessage(CommandMessages.DEBUG_USAGE, plugin.getCommandPrefixes().get(0)));
        this.setArgRange(0, 1);
        for (String key : staticKeys) {
            this.addKey(key);
        }
        for (String key : staticPrefixedKeys) {
            this.addPrefixedKey(key);
        }
        this.addPrefixedKey(" debug");
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " debug " + ChatColor.GOLD + "2");
        this.setPermission(Perm.COMMAND_DEBUG.getPermission());
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        if (args.size() == 1) {
            if (args.get(0).equalsIgnoreCase("off")) {
                plugin.config().set(BaseConfig.DEBUG_MODE, 0);
                Logging.setDebugMode(plugin.config().get(BaseConfig.DEBUG_MODE));
                plugin.config().save();
            } else {
                try {
                    int debugLevel = Integer.parseInt(args.get(0));
                    if (debugLevel > 3 || debugLevel < 0) {
                        throw new NumberFormatException();
                    }
                    plugin.config().set(BaseConfig.DEBUG_MODE, debugLevel);
                    Logging.setDebugMode(plugin.config().get(BaseConfig.DEBUG_MODE));
                    plugin.config().save();
                } catch (NumberFormatException e) {
                    messager.bad(Messages.INVALID_DEBUG, sender);
                }
            }
        }
        this.displayDebugMode(sender);
    }

    private void displayDebugMode(CommandSender sender) {
        if (plugin.config().get(BaseConfig.DEBUG_MODE) == 0) {
            messager.normal(CommandMessages.DEBUG_SET, sender, ChatColor.RED + messager.getMessage(Messages.GENERIC_OFF));
        } else {
            messager.normal(CommandMessages.DEBUG_SET, sender, ChatColor.GREEN
                    + plugin.config().get(BaseConfig.DEBUG_MODE).toString());
            Logging.fine(this.plugin.getDescription().getName() + " debug ENABLED");
        }
    }
}
