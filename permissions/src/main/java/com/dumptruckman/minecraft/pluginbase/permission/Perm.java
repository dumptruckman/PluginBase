/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.permission;

import java.util.Collections;
import java.util.Map;

/**
 * Represents a permissions that a Minecraft player may have.
 *
 * These should generally be defined as constants.
 * This class must be implemented for your specific Minecraft Server implementation.  See {@link #verify(String)}.
 */
public abstract class Perm {

    static void init() { }

    /**
     * Character used to separate permission namespaces.
     */
    protected static final char SEPARATOR = '.';

    /**
     * ALL plugin permissions.
     */
    public static final Perm ALL = PermFactory.newPerm("*").usePluginName().build();

    /**
     * ALL command permissions.
     */
    public static final Perm ALL_CMD = PermFactory.newPerm("cmd" + SEPARATOR + "*").usePluginName().addToAll().build();

    /**
     * The permission's fully realized name.
     *
     * See: {@link #getName()}
     */
    protected final String name;
    /** A description of this permission. */
    protected final String description;
    /** A map of all child permissions and their default setting. */
    protected final Map<String, Boolean> children;
    /** The default setting for this permission. */
    protected final PermDefault permDefault;
    /** A map of all parent permissions and the default THIS permission will be set to if the player has the parent. */
    protected final Map<String, Boolean> parents;

    Perm(final PermInfo permInfo, final String name, final String description, final Map<String, Boolean> children,
         final PermDefault permDefault, final Map<String, Boolean> parents, final boolean baseName) {
        if (baseName && permInfo != null) {
            this.name = (permInfo.getPermissionName() + SEPARATOR + name).toLowerCase();
        } else {
            this.name = name.toLowerCase();
        }
        this.description = description;
        this.children = Collections.unmodifiableMap(children);
        this.permDefault = permDefault;
        this.parents = Collections.unmodifiableMap(parents);
    }

    /**
     * Gets the permission's fully realized name.
     *
     * This name represents the full namespace for the permission.  Example: pluginbase.cmd.info.
     *
     * @return The permission's fully realized name.
     */
    public final String getName() {
        return name;
    }

    /**
     * Gets the permission's description.
     *
     * @return The permission's description.
     */
    public final String getDescription() {
        return this.description;
    }

    /**
     * Gets a map of all child permissions and their default setting.
     *
     * @return An unmodifiable map of all child permissions.
     */
    public final Map<String, Boolean> getChildren() {
        return this.children;
    }

    /**
     * Gets a map of all parent permissions and the default THIS permission will be set to if a player has them.
     *
     * @return An unmodifiable map of all parent permissions.
     */
    public final Map<String, Boolean> getParents() {
        return this.parents;
    }

    /**
     * Gets the default setting for this permission.
     *
     * @return The default setting for this permission.
     */
    public final PermDefault getPermDefault() {
        return this.permDefault;
    }

    /**
     * Checks if the sender has the permission in question.
     *
     * This method will also take any steps necessary to initialize the permission in Minecraft if required.
     *
     * @param permissible Permissible to check permission for.
     * @return True if sender has access to the permission.
     */
    public final boolean hasPermission(final Permissible permissible) {
        //TODO Add concept of sub-node only permissions.  Should probably throw UOE on non-specific perm checks.
        verify(getName());
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
    public final boolean hasPermission(final Permissible permissible, final String specific) {
        final String fullName = getName() + SEPARATOR + specific;
        verify(fullName);
        return permissible.hasPermission(fullName);
    }

    /**
     * This method will perform all the necessary steps required to initialize a permission in your Minecraft server
     * implementation.
     *
     * For example, in Bukkit, this will register any permission that is not already registered.
     *
     * @param name The name of the permission to verify.
     */
    protected abstract void verify(final String name);
}
