/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.permission;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Use this to create new Perm objects.
 * <p/>
 * This class will function without any initialization but should generally be registered with a Minecraft server
 * implementation specific implementation of itself via {@link #registerPermissionFactory(Class)}.
 * This allows the PermFactory to create properly implemented permissions.
 */
public abstract class PermFactory {

    private static Constructor<? extends PermFactory> factory;
    /** Represents the information for the plugin utilizing this PermFactory. */

    protected static boolean hasFactory() {
        return factory != null;
    }

    private static final Map<String, String> PERM_NAME_MAP = new HashMap<String, String>();

    /**
     * Registers a given base name to the given class.
     * <p/>
     * This affects what name is used as the top level namespace when using {@link #usePluginName()}
     *
     * @param pluginClass Your plugin class.
     * @param permissionName The top level namespace for your plugin's permissions.
     */
    public static void registerPermissionName(final Class pluginClass, final String permissionName) {
        PERM_NAME_MAP.put(pluginClass.getName(), permissionName);
    }

    static String getPermissionName(final Class pluginClass) {
        return PERM_NAME_MAP.get(pluginClass.getName());
    }

    /**
     * Creates a builder object for creating new {@link Permission}s.
     *
     * @param pluginClass The Class for the Plugin declaring this permission.  This is used for setting top level
     *                    permissions and a base permission name.
     * @param permName The name of the permission, generally without top level namespaces.
     * @return A new PermFactory object used for building a new {@link Permission}.
     */
    public static PermFactory newPerm(final Class pluginClass, final String permName) {
        if (factory == null) {
            throw new IllegalStateException("Must register a PermFactory class!");
        }
        if (!PERM_NAME_MAP.containsKey(pluginClass.getName())) {
            throw new IllegalArgumentException(pluginClass + " does not have a registered permission name!");
        }
        Permission.ensureParentPermsConfigured(pluginClass);
        return newUncheckedPerm(pluginClass, permName);
    }

    static PermFactory newUncheckedPerm(final Class pluginClass, final String permName) {
        final boolean access = factory.isAccessible();
        try {
            if (!access) {
                factory.setAccessible(true);
            }
            return factory.newInstance(pluginClass, permName);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            if (!access) {
                factory.setAccessible(false);
            }
        }
        return new PermFactory(pluginClass, permName) {
            @Override
            public Permission build() {
                return new Permission(pluginClass, this.name, this.description, this.children, this.permissionDefault,
                        this.parents, this.baseName, this.specificOnly) {
                    @Override
                    protected void verify(final String name) { }
                };
            }
        };
    }

    /**
     * Registers an implementation specific PermissionFactory.
     * <p/>
     * <b>Call this before initializing any Perm objects!</b>
     * <p/>
     * The given PermissionFactory class must have a constructor that accepts only a Class object and String object,
     * respectively.
     *
     * @param clazz The implementation specific PermissionFactory class to use.
     */
    public static void registerPermissionFactory(final Class<? extends PermFactory> clazz) {
        try {
            factory = clazz.getDeclaredConstructor(Class.class, String.class);
            Permission.init();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("PermFactory must have constructor accepting single string!");
        }
    }

    protected final Class pluginClass;
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
    /** Whether or not this permissions is allowed to be used without a specific node added. */
    protected boolean specificOnly = false;

    protected PermFactory(final Class pluginClass, final String permName) {
        if (pluginClass == null) {
            throw new IllegalArgumentException("pluginClass may not be null!");
        }
        if (permName == null) {
            throw new IllegalArgumentException("permName may not be null!");
        }
        this.pluginClass = pluginClass;
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
     * @param permission The child permission to add.
     * @return this PermFactory for method chaining.
     */
    public PermFactory child(final Permission permission) {
        return child(permission.getName());
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
     * @param permission The child permission to add.
     * @param state The default value for the child.
     * @return this PermFactory for method chaining.
     */
    public PermFactory child(final Permission permission, final boolean state) {
        return child(permission.getName(), state);
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
     * @param permission The parent permission to add.
     * @return this PermFactory for method chaining.
     */
    public PermFactory parent(final Permission permission) {
        return parent(permission.getName());
    }

    /**
     * Adds a parent permission that will grant this permission by default.
     *
     * @param name The parent permission to add.
     * @return this PermFactory for method chaining.
     */
    public PermFactory parent(final String name) {
        return parent(name.toLowerCase(), true);
    }

    /**
     * Adds a parent permission that will grant the specified default for this permission.
     *
     * @param permission The parent permission to add.
     * @param state The default state for this permission when given the parent.
     * @return this PermFactory for method chaining.
     */
    public PermFactory parent(final Permission permission, final boolean state) {
        return parent(permission.getName(), state);
    }

    /**
     * Adds a parent permission that will grant the specified default for this permission.
     *
     * @param name The parent permission to add.
     * @param state The default state for this permission when given the parent.
     * @return this PermFactory for method chaining.
     */
    public PermFactory parent(final String name, final boolean state) {
        parents.put(name.toLowerCase(), state);
        return this;
    }

    /**
     * Adds this permission as a child to {@link Permission#getAllPerm(Class)}.
     *
     * @return this PermFactory for method chaining.
     */
    public PermFactory addToAll() {
        return parent(Permission.getAllPerm(pluginClass));
    }

    /**
     * Adds this permission as a child to {@link Permission#getCommandPerm(Class)}.
     *
     * @return this PermFactory for method chaining.
     */
    public PermFactory commandPermission() {
        return parent(Permission.getCommandPerm(pluginClass));
    }

    /**
     * Sets the default for this permission.
     * <p/>
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
     * Calling this will cause the permission to be prefixed with a top level name space provided by the class object
     * passed into the {@link #newPerm(Class, String)} method.
     * <p/>
     * The name is based on the name registered through {@link #registerPermissionName(Class, String)} or the
     * static method 'String getPermissionName()' defined in the plugin class.  If neither of those methods result
     * in a name, the simple class name will be used.
     *
     * @return this PermFactory for method chaining.
     */
    public PermFactory usePluginName() {
        baseName = true;
        return this;
    }

    /**
     * Indicates that the permissions is only to be used with the specific node method
     * {@link Permission#hasPermission(Permissible, String)}.
     * <p/>
     * This means that the name of the permission defined will not be registered in the server implementation and that
     * it only serves as a placeholder.
     *
     * @return this PermFactory for method chaining.
     */
    public PermFactory specificOnly() {
        specificOnly = true;
        return this;
    }

    /**
     * Creates the Perm object.
     * <p/>
     * Call this after using all the other methods in this class that you require.  Afterwards, this PermFactory object
     * may be discarded.
     *
     * @return a new Perm based on all the supplied values of this PermFactory.
     */
    public abstract Permission build();
}
