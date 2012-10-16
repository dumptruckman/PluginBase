/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.permission;

import com.dumptruckman.minecraft.pluginbase.entity.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;

import java.util.Map;

public abstract class Perm {

    protected static final String SEPARATOR = ".";

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
    protected final PermDefault permDefault;
    protected final Map<String, Boolean> parents;

    Perm(final PluginBase plugin, final String name, final String description, final Map<String, Boolean> children,
         final PermDefault permDefault, final Map<String, Boolean> parents, final boolean baseName) {
        if (baseName && plugin != null) {
            this.name = plugin.getPluginInfo().getName().toLowerCase() + SEPARATOR + name.toLowerCase();
        } else {
            this.name = name.toLowerCase();
        }
        this.description = description;
        this.children = children;
        this.permDefault = permDefault;
        this.parents = parents;
    }

    public final String getName() {
        return name;
    }

    public String getDescription() {
        return this.description;
    }

    public Map<String, Boolean> getChildren() {
        return this.children;
    }

    public Map<String, Boolean> getParents() {
        return this.parents;
    }

    public PermDefault getPermDefault() {
        return this.permDefault;
    }

    /**
     * Checks if the sender has the node in question.
     *
     * @param permissible Permissible to check permission for.
     * @return True if sender has the permission.
     */
    public final boolean hasPermission(final BasePlayer permissible) {
        verify(getName());
        return permissible.hasPermission(getName());
    }

    public final boolean hasPermission(final BasePlayer permissible, final String specific) {
        final String fullName = getName() + SEPARATOR + specific;
        verify(fullName);
        return permissible.hasPermission(fullName);
    }

    protected abstract void verify(final String name);
}
