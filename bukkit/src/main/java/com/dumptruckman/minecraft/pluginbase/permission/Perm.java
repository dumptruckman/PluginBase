package com.dumptruckman.minecraft.pluginbase.permission;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Perm {

    /**
     * Permission for debug command.
     */
    public static final Perm COMMAND_DEBUG = new Builder("debug")
            .desc("Spams the console a bunch.").def(PermissionDefault.OP).build();
    /**
     * Permission for reload command.
     */
    public static final Perm COMMAND_RELOAD = new Builder("reload")
            .desc("Reloads the config file.").def(PermissionDefault.OP).build();
    /**
     * Permission for help command.
     */
    public static final Perm COMMAND_HELP = new Builder("help")
            .desc("Displays a nice help menu.").def(PermissionDefault.OP).build();

    private final String name;
    private final String description;
    private final Map<String, Boolean> children;
    private final PermissionDefault permissionDefault;

    private final Map<String, Permission> permissionMap = new HashMap<String, Permission>();
    //private Permission permission = null;
    
    private String extra = "";
    
    private Perm(String name, String description, Map<String, Boolean> children, PermissionDefault permissionDefault) {
        this.name = name;
        this.description = description;
        this.children = children;
        this.permissionDefault = permissionDefault;
    }

    public final String getName() {
        return name + extra;
    }
    
    public final Perm specific(String node) {
        extra = "." + node;
        return this;
    }
    
    public final String getDescription() {
        return this.description;
    }

    public final Map<String, Boolean> getChildren() {
        return this.children;
    }

    public final PermissionDefault getPermissionDefault() {
        return this.permissionDefault;
    }

    /**
     * Checks if the sender has the node in question.
     *
     * @param permissible Permissible to check permission for.
     * @return True if sender has the permission.
     */
    public final boolean hasPermission(Permissible permissible) {
        boolean result = permissible.hasPermission(getPermission());;
        extra = "";
        return result;
    }

    public final Permission getPermission() {
        Permission permission = permissionMap.get(extra);
        if (permission == null) {
            permission = new Permission(getName(), this.description, this.permissionDefault, this.children);
            permissionMap.put(extra, permission);
        }
        if (!registeredPerms.contains(permission)) {
            Bukkit.getPluginManager().addPermission(permission);
            registeredPerms.add(permission);
        }
        return permission;
    }

    static final Set<Permission> registeredPerms = new HashSet<Permission>();

    public static class Builder {

        String name;
        private String description = "";
        private Map<String, Boolean> children = new HashMap<String, Boolean>();
        private PermissionDefault permissionDefault = PermissionDefault.OP;

        public Builder(String permName) {
            this.name = permName;
        }

        public Builder desc(String description) {
            this.description = description;
            return this;
        }

        public Builder child(String name, Boolean state) {
            children.put(name, state);
            return this;
        }

        public Builder def(PermissionDefault permissionDefault) {
            this.permissionDefault = permissionDefault;
            return this;
        }

        public Perm build() {
            return new Perm(this.name, this.description, this.children, this.permissionDefault);
        }
    }
}
