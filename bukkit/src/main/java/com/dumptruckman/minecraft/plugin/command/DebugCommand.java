package com.dumptruckman.minecraft.plugin.command;

import com.dumptruckman.minecraft.config.BaseConfig;
import com.dumptruckman.minecraft.locale.Messages;
import com.dumptruckman.minecraft.permission.Perm;
import com.dumptruckman.minecraft.plugin.AbstractBukkitPlugin;
import com.dumptruckman.minecraft.util.Logging;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Enables debug-information.
 */
public class DebugCommand<P extends AbstractBukkitPlugin> extends PluginCommand<P> {

    public DebugCommand(P plugin) {
        super(plugin);
        this.setName("Turn Debug on/off?");
        this.setCommandUsage("/" + plugin.getCommandPrefixes().get(0) + " debug" + ChatColor.GOLD + " [1|2|3|off]");
        this.setArgRange(0, 1);
        for (String prefix : (List<String>) plugin.getCommandPrefixes()) {
            this.addKey(prefix + " debug");
            this.addKey(prefix + "debug");
        }
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " debug " + ChatColor.GOLD + "2");
        this.setPermission(Perm.COMMAND_DEBUG.getPermission());
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        if (args.size() == 1) {
            if (args.get(0).equalsIgnoreCase("off")) {
                plugin.config().set(BaseConfig.DEBUG_MODE, 0);
                plugin.config().save();
            } else {
                try {
                    int debugLevel = Integer.parseInt(args.get(0));
                    if (debugLevel > 3 || debugLevel < 0) {
                        throw new NumberFormatException();
                    }
                    plugin.config().set(BaseConfig.DEBUG_MODE, debugLevel);
                    plugin.config().save();
                } catch (NumberFormatException e) {
                    messager.bad(Messages.INVALID_DEBUG, sender);
                }
            }
        }
        this.displayDebugMode(sender);
    }

    private void displayDebugMode(CommandSender sender) {
        if (this.plugin.config().get(BaseConfig.DEBUG_MODE) == 0) {
            messager.normal(Messages.DEBUG_SET, sender, ChatColor.RED + messager.getMessage(Messages.GENERIC_OFF));
        } else {
            messager.normal(Messages.DEBUG_SET, sender, ChatColor.GREEN
                    + plugin.config().get(BaseConfig.DEBUG_MODE).toString());
            Logging.fine(this.plugin.getDescription().getName() + " debug ENABLED");
        }
    }
}
