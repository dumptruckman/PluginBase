/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.permission;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a permissions that a Minecraft player may have.
 *
 * These should generally be defined as constants.
 * This class must be implemented for your specific Minecraft Server implementation.  See {@link #verify(String)}.
 */
public abstract class Perm {

    /** Static initialization method. */
    static void init() { }

    /**
     * Character used to separate permission namespaces.
     */
    protected static final char SEPARATOR = '.';

    private static final Map<String, Perm> ALL_PERM_MAP = new HashMap<String, Perm>();

    private static final Map<String, Perm> ALL_CMD_PERM_MAP = new HashMap<String, Perm>();

    /**
     * Retrieves the Perm that represents the top level all encompassing permission for your plugin.
     *
     * {@link com.dumptruckman.minecraft.pluginbase.permission.PermFactory#addToAll()} adds this as a parent to the new
     * Perm.
     *
     * @param clazz Your plugin class.
     * @return The "all" permission.
     */
    public static Perm getAllPerm(final Class clazz) {
        return ALL_PERM_MAP.get(clazz.getName());
    }

    /**
     * Retrieves the Perm that represents the top level all command encompassing permission for your plugin.
     *
     * {@link com.dumptruckman.minecraft.pluginbase.permission.PermFactory#commandPermission()} adds this as a parent
     * to the new Perm.
     *
     * @param clazz Your plugin class.
     * @return The "all" permission.
     */
    public static Perm getCommandPerm(final Class clazz) {
        return ALL_CMD_PERM_MAP.get(clazz.getName());
    }

    static String getBaseNameFromClass(final Class clazz) {
        final String possibleName = PermFactory.getPermissionName(clazz);
        if (possibleName != null) {
            return possibleName;
        }
        try {
            final Method m = clazz.getDeclaredMethod("getPermissionName");
            m.setAccessible(true);
            try {
                return (String) m.invoke(null);
            } finally {
                m.setAccessible(false);
            }
        } catch (NoSuchMethodException ignore) {
        } catch (IllegalAccessException ignore) {
        } catch (InvocationTargetException ignore) {
        } catch (ClassCastException ignore) { }
        return clazz.getSimpleName();
    }

    static void ensureParentPermsConfigured(final Class clazz) {
        if (!ALL_PERM_MAP.containsKey(clazz.getName())) {
            ALL_PERM_MAP.put(clazz.getName(), PermFactory.newUncheckedPerm(clazz, "*").usePluginName().build());
            ALL_CMD_PERM_MAP.put(clazz.getName(), PermFactory.newUncheckedPerm(clazz, "cmd" + SEPARATOR + "*").usePluginName().addToAll().build());
        }
    }

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
    /** Whether or not this permissions is allowed to be used without a specific node added. */
    protected final boolean specificOnly;

    protected Perm(final Class declaringPluginClass, final String name, final String description,
         final Map<String, Boolean> children, final PermDefault permDefault, final Map<String, Boolean> parents,
         final boolean baseName, final boolean specificOnly) {
        if (baseName) {
            this.name = (getBaseNameFromClass(declaringPluginClass) + SEPARATOR + name).toLowerCase();
        } else {
            this.name = name.toLowerCase();
        }
        this.description = description;
        this.children = Collections.unmodifiableMap(children);
        this.permDefault = permDefault;
        this.parents = Collections.unmodifiableMap(parents);
        this.specificOnly = specificOnly;
    }

    /**
     * Gets the permission's fully realized name.
     *
     * This name represents the full namespace for the permission.  Example: pluginbase.cmd.info.
     *
     * This method will also verify that the name given represents a valid permission.
     *
     * @return The permission's fully realized name.
     */
    public final String getName() {
        if (specificOnly) {
            throw new UnsupportedOperationException("This Perm is only usable with an additional specific node!");
        }
        verify(name);
        return name;
    }

    /**
     * Gets the permission's fully realized name with a specific sub node attachment.
     *
     * This name represents the full namespace for the permission.  Example: multiverse.access.hellworld
     *
     * This method will also verify that the name given represents a valid permission.
     *
     * @param specific  The specific sub-node to attach.
     * @return The permission's fully realized name with a specific sub node attachment.
     */
    public final String getName(final String specific) {
        final String fullName = name + SEPARATOR + specific.toLowerCase();
        verify(fullName);
        return fullName;
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
        return permissible.hasPermission(getName(specific));
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
