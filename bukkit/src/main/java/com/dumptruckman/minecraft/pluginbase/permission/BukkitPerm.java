/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.permission;

import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

import java.util.Map;

public class BukkitPerm extends Perm {
    
    BukkitPerm(final PluginBase plugin, final String name, final String description, final Map<String, Boolean> children,
                       final PermDefault permDefault, final Map<String, Boolean> parents, final boolean baseName) {
        super(plugin, name, description, children, permDefault, parents, baseName);
        verify(getName());
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

    static void init() { }
}
