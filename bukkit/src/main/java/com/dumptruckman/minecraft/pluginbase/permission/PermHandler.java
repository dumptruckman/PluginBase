package com.dumptruckman.minecraft.pluginbase.permission;

import com.dumptruckman.minecraft.pluginbase.util.commandhandler.PermissionsInterface;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * A required class for CommandHandler.
 */
public class PermHandler implements PermissionsInterface {

    @Override
    public boolean hasPermission(CommandSender commandSender, String node, boolean opRequired) {
        return commandSender.hasPermission(node);
    }

    @Override
    public boolean hasAnyPermission(CommandSender commandSender, List<String> nodes, boolean opRequired) {
        for (String node : nodes) {
            if (commandSender.hasPermission(node)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasAllPermission(CommandSender commandSender, List<String> nodes, boolean opRequired) {
        for (String node : nodes) {
            if (!commandSender.hasPermission(node)) {
                return false;
            }
        }
        return true;
    }
}
