package com.dumptruckman.minecraft.pluginbase.permission;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public abstract class PermFactory {

    private static Constructor<PermFactory> factory;

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
                return new Perm(this.name, this.description, this.children, this.permissionDefault, this.parents, this.baseName) {

                };
            }
        };
    }

    static void registerFactory(Class<PermFactory> clazz) {
        try {
            factory = clazz.getDeclaredConstructor(String.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("PermFactory must have constructor accepting single string!");
        }
    }

    protected final String name;
    protected String description = "";
    protected Map<String, Boolean> children = new HashMap<String, Boolean>();
    protected PermDefault permissionDefault = PermDefault.OP;
    protected Map<String, Boolean> parents = new HashMap<String, Boolean>();
    protected boolean baseName = false;
    protected boolean all = false;

    protected PermFactory(String permName) {
        this.name = permName;
    }

    public final PermFactory desc(String description) {
        this.description = description;
        return this;
    }

    public final PermFactory child(Perm perm) {
        return child(perm.getName());
    }

    public final PermFactory child(String name) {
        return child(name, true);
    }

    public final PermFactory child(Perm perm, Boolean state) {
        return child(perm.getName(), state);
    }

    public final PermFactory child(String name, Boolean state) {
        children.put(name, state);
        return this;
    }

    public final PermFactory parent(Perm perm) {
        return parent(perm.getName());
    }

    public final PermFactory parent(String name) {
        return parent(name, true);
    }

    public final PermFactory parent(Perm perm, Boolean state) {
        return parent(perm.getName(), state);
    }

    public final PermFactory parent(String name, Boolean state) {
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

    public final PermFactory def(PermDefault permissionDefault) {
        this.permissionDefault = permissionDefault;
        return this;
    }

    public final PermFactory usePluginName() {
        baseName = true;
        return this;
    }

    public abstract Perm build();
}
