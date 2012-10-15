package com.dumptruckman.minecraft.pluginbase.permission;

import com.dumptruckman.minecraft.pluginbase.entity.BasePlayer;

import java.util.Map;

public abstract class Perm {

    /**
     * ALL plugin permissions.
     */
    public static final Perm ALL = PermFactory.newPerm("*").usePluginName().build();

    /**
     * ALL commmand permissions.
     */
    public static final Perm ALL_CMD = PermFactory.newPerm("cmd.*").usePluginName().addToAll().build();

    /**
     * Permission for confirm command.
     */
    public static final Perm COMMAND_CONFIRM = PermFactory.newPerm("cmd.confirm").usePluginName().commandPermission()
            .desc("If you have not been prompted to use this, it will not do anything.").build();
    /**
     * Permission for debug command.
     */
    public static final Perm COMMAND_DEBUG = PermFactory.newPerm("cmd.debug").usePluginName().commandPermission()
            .desc("Spams the console a bunch.").build();
    /**
     * Permission for reload command.
     */
    public static final Perm COMMAND_RELOAD = PermFactory.newPerm("cmd.reload").usePluginName().commandPermission()
            .desc("Reloads the config file.").build();
    /**
     * Permission for version command.
     */
    public static final Perm COMMAND_VERSION = PermFactory.newPerm("cmd.version").usePluginName().commandPermission()
            .desc("Sends version information to the console.").build();
    /**
     * Permission for help command.
     */
    public static final Perm COMMAND_HELP = PermFactory.newPerm("cmd.help").usePluginName().commandPermission()
            .desc("Displays a nice help menu.").build();

    protected final String name;
    protected final String description;
    protected final Map<String, Boolean> children;
    protected final PermDefault permissionDefault;
    protected final Map<String, Boolean> parents;
    protected final boolean baseName;

    Perm(String name, String description, Map<String, Boolean> children, PermDefault permissionDefault, Map<String, Boolean> parents, boolean baseName) {
        this.name = name;
        this.description = description;
        this.children = children;
        this.permissionDefault = permissionDefault;
        this.parents = parents;
        this.baseName = baseName;
    }

    protected final String getName() {
        return name;
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

    public final PermDefault getPermissionDefault() {
        return this.permissionDefault;
    }

    /**
     * Checks if the sender has the node in question.
     *
     * @param permissible Permissible to check permission for.
     * @return True if sender has the permission.
     */
    public final boolean hasPermission(final BasePlayer permissible) {
        return permissible.hasPermission(getName());
    }

    public final boolean hasPermission(final BasePlayer permissible, final String specific) {
        return permissible.hasPermission(getName() + "." + specific);
    }
}
