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
    public static final Perm COMMAND_DEBUG = new Builder("debug").usePluginName()
            .desc("Spams the console a bunch.").def(PermissionDefault.OP).build();
    /**
     * Permission for reload command.
     */
    public static final Perm COMMAND_RELOAD = new Builder("reload").usePluginName()
            .desc("Reloads the config file.").def(PermissionDefault.OP).build();
    /**
     * Permission for help command.
     */
    public static final Perm COMMAND_HELP = new Builder("help").usePluginName()
            .desc("Displays a nice help menu.").def(PermissionDefault.OP).build();

    private final String name;
    private final String description;
    private final Map<String, Boolean> children;
    private final PermissionDefault permissionDefault;
    private final Map<String, Boolean> parents;

    private final Map<String, Permission> permissionMap = new HashMap<String, Permission>();
    private final boolean baseName;
    //private Permission permission = null;
    
    private String extra = "";
    
    private Perm(String name, String description, Map<String, Boolean> children, PermissionDefault permissionDefault, Map<String, Boolean> parents, boolean baseName) {
        this.name = name;
        this.description = description;
        this.children = children;
        this.permissionDefault = permissionDefault;
        this.parents = parents;
        this.baseName = baseName;
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

    public final Map<String, Boolean> getParents() {
        return this.parents;
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
            String name;
            if (baseName && plugin != null) {
                name = plugin.getName().toLowerCase() + "." + getName();
            } else {
                name = getName();
            }
            permission = new Permission(name, this.description, this.permissionDefault, this.children);
            permissionMap.put(extra, permission);
            for (Map.Entry<String, Boolean> parent : parents.entrySet()) {
                permission.addParent(parent.getKey(), parent.getValue());
            }
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
        private Map<String, Boolean> parents = new HashMap<String, Boolean>();
        private boolean baseName = false;

        public Builder(String permName) {
            this.name = permName;
        }

        public Builder desc(String description) {
            this.description = description;
            return this;
        }

        public Builder child(Perm perm) {
            return child(perm.getName());
        }

        public Builder child(String name) {
            return child(name, true);
        }

        public Builder child(Perm perm, Boolean state) {
            return child(perm.getName(), state);
        }

        public Builder child(String name, Boolean state) {
            children.put(name, state);
            return this;
        }

        public Builder parent(Perm perm) {
            return parent(perm.getName());
        }

        public Builder parent(String name) {
            return parent(name, true);
        }

        public Builder parent(Perm perm, Boolean state) {
            return parent(perm.getName(), state);
        }

        public Builder parent(String name, Boolean state) {
            parents.put(name, state);
            return this;
        }

        public Builder def(PermissionDefault permissionDefault) {
            this.permissionDefault = permissionDefault;
            return this;
        }

        public Builder usePluginName() {
            baseName = true;
            return this;
        }

        public Perm build() {
            return new Perm(this.name, this.description, this.children, this.permissionDefault, this.parents, this.baseName);
        }
    }

    private static Plugin plugin = null;

    /**
     * Registers all Permission to the plugin.
     *
     * @param plugin Plugin to register permissions to.
     */
    public static void registerPlugin(Plugin plugin) {
        if (Perm.plugin != null) {
            throw new IllegalStateException("May not register another plugin!");
        }
        Perm.plugin = plugin;
    }
}
