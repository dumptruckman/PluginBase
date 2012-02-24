package com.dumptruckman.tools.plugin.command;

import com.dumptruckman.tools.locale.Message;
import com.dumptruckman.tools.permission.Perm;
import com.dumptruckman.tools.plugin.AbstractPluginBase;
import com.dumptruckman.tools.util.Logging;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Enables debug-information.
 */
public class DebugCommand<P extends AbstractPluginBase> extends PluginCommand<P> {

    public DebugCommand(P plugin) {
        super(plugin);
        this.setName("Turn Debug on/off?");
        this.setCommandUsage("/" + plugin.getCommandPrefix() + " debug" + ChatColor.GOLD + " [1|2|3|off]");
        this.setArgRange(0, 1);
        this.addKey(plugin.getCommandPrefix() + " debug");
        this.addKey(plugin.getCommandPrefix() + "debug");
        this.addCommandExample("/" + plugin.getCommandPrefix() + " debug " + ChatColor.GOLD + "2");
        this.setPermission(Perm.COMMAND_DEBUG.getPermission());
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        if (args.size() == 1) {
            if (args.get(0).equalsIgnoreCase("off")) {
                plugin.getSettings().setDebug(0);
                plugin.getSettings().save();
            } else {
                try {
                    int debugLevel = Integer.parseInt(args.get(0));
                    if (debugLevel > 3 || debugLevel < 0) {
                        throw new NumberFormatException();
                    }
                    plugin.getSettings().setDebug(debugLevel);
                    plugin.getSettings().save();
                } catch (NumberFormatException e) {
                    messager.bad(Message.INVALID_DEBUG, sender);
                }
            }
        }
        this.displayDebugMode(sender);
    }

    private void displayDebugMode(CommandSender sender) {
        if (plugin.getSettings().getDebug() == 0) {
            messager.normal(Message.DEBUG_SET, sender, ChatColor.RED + messager.getMessage(Message.GENERIC_OFF));
        } else {
            messager.normal(Message.DEBUG_SET, sender, ChatColor.GREEN
                    + Integer.toString(plugin.getSettings().getDebug()));
            Logging.fine(this.plugin.getDescription().getName() + " debug ENABLED");
        }
    }
}
