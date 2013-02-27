/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.bukkit.permission;

import com.dumptruckman.minecraft.pluginbase.permission.Perm;
import com.dumptruckman.minecraft.pluginbase.permission.PermDefault;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

import java.util.Map;

/**
 * Represents a permissions that a Minecraft player may have.
 * <p/>
 * These should generally be defined as constants.
 * <p/>
 * This class must be implemented for your specific Minecraft Server implementation.  See {@link #verify(String)}.
 */
public class BukkitPerm extends Perm {
    
    protected BukkitPerm(final Class pluginClass, final String name, final String description,
               final Map<String, Boolean> children, final PermDefault permDefault, final Map<String, Boolean> parents,
               final boolean baseName, final boolean specificOnly) {
        super(pluginClass, name, description, children, permDefault, parents, baseName, specificOnly);
        if (!specificOnly) {
            verify(getName());
        }
    }

    private Permission setupPermission(final String name) {
        final PluginManager pluginManager = Bukkit.getPluginManager();
        Permission permission = pluginManager.getPermission(name);
        if (permission == null) {
            permission = new Permission(name, getDescription(), getPermissionDefault(), getChildren());
            for (Map.Entry<String, Boolean> parent : parents.entrySet()) {
                permission.addParent(parent.getKey(), parent.getValue());
            }
            pluginManager.addPermission(permission);
            return permission;
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    protected void verify(final String name) {
        final Permission permission = setupPermission(name);
        if (permission != null) {
            Bukkit.getPluginManager().recalculatePermissionDefaults(permission);
        }
    }

    private PermissionDefault getPermissionDefault() {
        switch (permDefault) {
            case TRUE:
                return PermissionDefault.TRUE;
            case FALSE:
                return PermissionDefault.FALSE;
            case OP:
                return PermissionDefault.OP;
            case NOT_OP:
                return PermissionDefault.NOT_OP;
            default:
                return PermissionDefault.OP;
        }
    }

    /**
     * Retrieves the Bukkit permission linked to this Perm object.
     *
     * @return the Bukkit permission linked to this Perm object.
     */
    public final Permission getPermission() {
        return Bukkit.getPluginManager().getPermission(getName());
    }

    /**
     * Retrieves the Bukkit permission linked to this Perm object for the specific sub node.
     *
     * @param specific The specific sub node to get permission for.
     * @return the Bukkit permission linked to this Perm object for the specific sub node.
     */
    public final Permission getPermission(final String specific) {
        return Bukkit.getPluginManager().getPermission(getName(specific));
    }

    /**
     * Checks if the sender has the permission in question.
     *
     * This method will also take any steps necessary to initialize the permission in Minecraft if required.
     *
     * @param permissible Permissible to check permission for.
     * @return True if sender has access to the permission.
     */
    public boolean hasPermission(final org.bukkit.permissions.Permissible permissible) {
        return permissible.hasPermission(getName());
    }

    /**
     * Checks if the sender has a specific sub-node of the permission in question.
     *
     * Sub-nodes are useful when you need permissions for non-constant things.
     * For example, if you need to check if someone can access a specific world, you can have a
     * permission like 'multiverse.access' and use this method to check the name of the world which would
     * ultimately check if the player has access to 'multiverse.access.worldname'.
     *
     * This method will also take any steps necessary to initialize the specific permission in Minecraft if required.
     *
     * @param permissible Permissible to check permission for.
     * @param specific The specific sub-node to check for.
     * @return True if sender has access to the permission.
     */
    public final boolean hasPermission(final org.bukkit.permissions.Permissible permissible,
                                       final String specific) {
        return permissible.hasPermission(getName(specific));
    }
}
