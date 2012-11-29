/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.permission;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Use this to create new Perm objects.
 *
 * This class will function without any initialization but should generally be registered with a Minecraft server
 * implementation specific implementation of itself via {@link #registerPermissionFactory(PermInfo, Class)}.
 * This allows the PermFactory to create properly implemented permissions.
 */
public abstract class PermFactory {

    private static Constructor<? extends PermFactory> factory;
    /** Represents the information for the plugin utilizing this PermFactory. */
    protected static PermInfo permInfo;

    /**
     * Creates a builder object for creating new {@link Perm}s.
     *
     * @param permName The name of the permission, generally without top level namespaces.
     * @return A new PermFactory object used for building a new {@link Perm}.
     */
    public static PermFactory newPerm(final String permName) {
        if (factory == null) {
            throw new IllegalStateException("Must register a PermFactory class!");
        }
        try {
            factory.newInstance(permName);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return new PermFactory(permName) {
            @Override
            public Perm build() {
                return new Perm(permInfo, this.name, this.description, this.children, this.permissionDefault,
                        this.parents, this.baseName) {
                    @Override
                    protected void verify(final String name) { }
                };
            }
        };
    }

    /**
     * Registers an implementation specific PermissionFactory.
     *
     * Call this before initializing any Perm objects!
     * The given PermissionFactory class must have a constructor that accepts a single String object.
     *
     * @param permInfo Information for permissions specific to the plugin using this PermFactory.
     * @param clazz The implementation specific PermissionFactory class to use.
     */
    public static void registerPermissionFactory(final PermInfo permInfo, final Class<? extends PermFactory> clazz) {
        try {
            factory = clazz.getDeclaredConstructor(String.class);
            Perm.init();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("PermFactory must have constructor accepting single string!");
        }
        PermFactory.permInfo = permInfo;
    }

    /** The permission's name. */
    protected final String name;
    /** The permission's description. */
    protected String description = "";
    /** The permission's children. */
    protected Map<String, Boolean> children = new HashMap<String, Boolean>();
    /** The permission's default. */
    protected PermDefault permissionDefault = PermDefault.OP;
    /** The permission's parents. */
    protected Map<String, Boolean> parents = new HashMap<String, Boolean>();
    /** Whether or not to use the plugin name as a top level namespace. */
    protected boolean baseName = false;

    protected PermFactory(final String permName) {
        this.name = permName;
    }

    /**
     * Sets the description for the permission.
     *
     * @param description The description.
     * @return this PermFactory for method chaining.
     */
    public PermFactory desc(final String description) {
        this.description = description;
        return this;
    }

    /**
     * Adds a child permission with a default value of true.
     *
     * @param perm The child permission to add.
     * @return this PermFactory for method chaining.
     */
    public PermFactory child(final Perm perm) {
        return child(perm.getName());
    }

    /**
     * Adds a child permission with a default value of true.
     *
     * @param name The child permission to add.
     * @return this PermFactory for method chaining.
     */
    public PermFactory child(final String name) {
        return child(name, true);
    }

    /**
     * Adds a child permission with a specified default value.
     *
     * @param perm The child permission to add.
     * @param state The default value for the child.
     * @return this PermFactory for method chaining.
     */
    public PermFactory child(final Perm perm, final boolean state) {
        return child(perm.getName(), state);
    }

    /**
     * Adds a child permission with a specified default value.
     *
     * @param name The child permission to add.
     * @param state The default value for the child.
     * @return this PermFactory for method chaining.
     */
    public PermFactory child(final String name, final boolean state) {
        children.put(name, state);
        return this;
    }

    /**
     * Adds a parent permission that will grant this permission by default.
     *
     * @param perm The parent permission to add.
     * @return this PermFactory for method chaining.
     */
    public PermFactory parent(final Perm perm) {
        return parent(perm.getName());
    }

    /**
     * Adds a parent permission that will grant this permission by default.
     *
     * @param name The parent permission to add.
     * @return this PermFactory for method chaining.
     */
    public PermFactory parent(final String name) {
        return parent(name, true);
    }

    /**
     * Adds a parent permission that will grant the specified default for this permission.
     *
     * @param perm The parent permission to add.
     * @param state The default state for this permission when given the parent.
     * @return this PermFactory for method chaining.
     */
    public PermFactory parent(final Perm perm, final boolean state) {
        return parent(perm.getName(), state);
    }

    /**
     * Adds a parent permission that will grant the specified default for this permission.
     *
     * @param name The parent permission to add.
     * @param state The default state for this permission when given the parent.
     * @return this PermFactory for method chaining.
     */
    public PermFactory parent(final String name, final boolean state) {
        parents.put(name, state);
        return this;
    }

    /**
     * Adds this permission as a child to {@link Perm#ALL}.
     *
     * @return this PermFactory for method chaining.
     */
    public PermFactory addToAll() {
        return parent(Perm.ALL);
    }

    /**
     * Adds this permission as a child to {@link Perm#ALL_CMD}.
     *
     * @return this PermFactory for method chaining.
     */
    public PermFactory commandPermission() {
        return parent(Perm.ALL_CMD);
    }

    /**
     * Sets the default for this permission.
     *
     * By default {@link PermDefault#OP} is used.
     *
     * @param permissionDefault The default for this permission.
     * @return this PermFactory for method chaining.
     */
    public PermFactory def(final PermDefault permissionDefault) {
        this.permissionDefault = permissionDefault;
        return this;
    }

    /**
     * Calling this will cause the permission to be prefixed with a top level name space provided by the plugin that
     * called {@link #registerPermissionFactory(PermInfo, Class)}.
     *
     * @return this PermFactory for method chaining.
     */
    public PermFactory usePluginName() {
        baseName = true;
        return this;
    }

    /**
     * Creates the Perm object.
     *
     * Call this after using all the other methods in this class that you require.  Afterwards, this PermFactory object
     * may be discarded.
     *
     * @return a new Perm based on all the supplied values of this PermFactory.
     */
    public abstract Perm build();
}
