package com.dumptruckman.tools.util;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Permission Enum for easily keeping track of permissions related to this plugin.
 */
public enum Perm {
    /**
     * Permission for debug command.
     */
    COMMAND_DEBUG(new Permission("tools.debug", "Spams the console a bunch.", PermissionDefault.OP)),
    /**
     * Permission for reload command.
     */
    COMMAND_RELOAD(new Permission("tools.reload", "Reloads the config file.", PermissionDefault.OP)),
    /**
     * Permission for placing circuits directly from signs.
     */
    CIRCUITS_FREE_PLACE(new Permission("tools.freeplace",
            "Allows the user to place circuits by editing signs.", PermissionDefault.OP));

    private Permission perm = null;
    private String permNode = "";

    Perm(Permission perm) {
        this.perm = perm;
    }

    Perm(String permNode) {
        this.permNode = permNode;
    }

    /**
     * @return the Permission.
     */
    public Permission getPermission() {
        return this.perm;
    }

    /**
     * @return the Permission node string.
     */
    public String getNode() {
        return this.permNode;
    }

    /**
     * Checks if the sender has the node in question.
     *
     * @param sender CommandSender to check permission for.
     * @return True if sender has the permission.
     */
    public boolean has(CommandSender sender) {
        return sender.hasPermission(perm);
    }

    /**
     * Registers all Permission to the plugin.
     *
     * @param plugin Plugin to register permissions to.
     */
    public static void register(JavaPlugin plugin) {
        PluginManager pm = plugin.getServer().getPluginManager();
        for (Perm perm : Perm.values()) {
            if (perm.getPermission() != null) {
                pm.addPermission(perm.getPermission());
            }
        }
    }
}

