/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.permission;

import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public abstract class PermFactory {

    private static Constructor<? extends PermFactory> factory;
    protected static PluginBase plugin;

    public static PermFactory newPerm(String permName) {
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
                if (all) {
                    parent(Perm.ALL);
                }
                return new Perm(plugin, this.name, this.description, this.children, this.permissionDefault, this.parents, this.baseName) {
                    @Override
                    protected void verify(final String name) { }
                };
            }
        };
    }

    public static void registerPermissionFactory(final PluginBase plugin, final Class<? extends PermFactory> clazz) {
        try {
            factory = clazz.getDeclaredConstructor(String.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("PermFactory must have constructor accepting single string!");
        }
        PermFactory.plugin = plugin;
    }

    protected final String name;
    protected String description = "";
    protected Map<String, Boolean> children = new HashMap<String, Boolean>();
    protected PermDefault permissionDefault = PermDefault.OP;
    protected Map<String, Boolean> parents = new HashMap<String, Boolean>();
    protected boolean baseName = false;
    protected boolean all = false;

    protected PermFactory(final String permName) {
        this.name = permName;
    }

    public final PermFactory desc(final String description) {
        this.description = description;
        return this;
    }

    public final PermFactory child(final Perm perm) {
        return child(perm.getName());
    }

    public final PermFactory child(final String name) {
        return child(name, true);
    }

    public final PermFactory child(final Perm perm, final boolean state) {
        return child(perm.getName(), state);
    }

    public final PermFactory child(final String name, final boolean state) {
        children.put(name, state);
        return this;
    }

    public final PermFactory parent(final Perm perm) {
        return parent(perm.getName());
    }

    public final PermFactory parent(final String name) {
        return parent(name, true);
    }

    public final PermFactory parent(final Perm perm, final boolean state) {
        return parent(perm.getName(), state);
    }

    public final PermFactory parent(final String name, final boolean state) {
        parents.put(name, state);
        return this;
    }

    public final PermFactory addToAll() {
        all = true;
        return this;
    }

    public final PermFactory commandPermission() {
        return parent(Perm.ALL_CMD);
    }

    public final PermFactory def(final PermDefault permissionDefault) {
        this.permissionDefault = permissionDefault;
        return this;
    }

    public final PermFactory usePluginName() {
        baseName = true;
        return this;
    }

    public abstract Perm build();
}
