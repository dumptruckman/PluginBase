package com.dumptruckman.minecraft.permission;

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

    private String name;
    private final String description;
    private final Map<String, Boolean> children;
    private final PermissionDefault permissionDefault;
    
    private Permission permission = null;
    
    private Perm(String name, String description, Map<String, Boolean> children, PermissionDefault permissionDefault) {
        this.name = name;
        this.description = description;
        this.children = children;
        this.permissionDefault = permissionDefault;
    }

    public final void setName(String name) {
        if (permission != null) {
            throw new IllegalStateException("Permission already finalized!");
        }
        this.name = name;
    }

    public final String getName() {
        return this.name;
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
    public final boolean hasPermission(Permissible permissible) throws IllegalStateException {
        return permissible.hasPermission(getPermission());
    }

    public final Permission getPermission() throws IllegalStateException {
        if (!Perm.isRegistered(this)) {
            if (plugin == null) {
                throw new IllegalStateException("Plugin must be registered first!");
            }
            setName(plugin.getDescription().getName().toLowerCase() + "." + getName());
            this.permission = new Permission(this.name, this.description, this.permissionDefault, this.children);
            Bukkit.getPluginManager().addPermission(this.permission);
            registeredPerms.add(this);
        }
        return this.permission;
    }

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

    static final Set<Perm> registeredPerms = new HashSet<Perm>();
    
    private static boolean isRegistered(Perm perm) {
        return registeredPerms.contains(perm);
    }
}
