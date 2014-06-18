/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.bukkit.permission;

import pluginbase.permission.Permission;
import pluginbase.permission.PermDefault;
import org.bukkit.Bukkit;
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
public class BukkitPermission extends Permission {
    
    protected BukkitPermission(final Class pluginClass, final String name, final String description,
                               final Map<String, Boolean> children, final PermDefault permDefault,
                               final Map<String, Boolean> parents, final boolean baseName, final boolean specificOnly) {
        super(pluginClass, name, description, children, permDefault, parents, baseName, specificOnly);
        if (!specificOnly) {
            verify(getName());
        }
    }

    private org.bukkit.permissions.Permission setupPermission(final String name) {
        final PluginManager pluginManager = Bukkit.getPluginManager();
        org.bukkit.permissions.Permission permission = pluginManager.getPermission(name.toLowerCase());
        if (permission == null) {
            permission = new org.bukkit.permissions.Permission(name, getDescription(), getPermissionDefault(), getChildren());
            for (Map.Entry<String, Boolean> parent : parents.entrySet()) {
                permission.addParent(parent.getKey(), parent.getValue());
            }
            pluginManager.addPermission(permission);
            return permission;
        } else {
            boolean changed = false;
            if (!permission.getDescription().equals(getDescription())) {
                permission.setDescription(getDescription());
                changed = true;
            }
            if (!permission.getDefault().equals(getPermissionDefault())) {
                permission.setDefault(getPermissionDefault());
                changed = true;
            }
            if (!permission.getChildren().equals(getChildren())) {
                permission.getChildren().clear();
                permission.getChildren().putAll(getChildren());
                changed = true;
            }
            if (changed) {
                return permission;
            }
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    protected void verify(final String name) {
        final org.bukkit.permissions.Permission permission = setupPermission(name);
        if (permission != null) {
            try {
                Bukkit.getPluginManager().recalculatePermissionDefaults(permission);
            } catch (IllegalArgumentException e) { }
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
    public final org.bukkit.permissions.Permission getPermission() {
        return Bukkit.getPluginManager().getPermission(getName());
    }

    /**
     * Retrieves the Bukkit permission linked to this Perm object for the specific sub node.
     *
     * @param specific The specific sub node to get permission for.
     * @return the Bukkit permission linked to this Perm object for the specific sub node.
     */
    public final org.bukkit.permissions.Permission getPermission(final String specific) {
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
