/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.permission;

/**
 * Use this to create new BukkitPerm objects.
 *
 * If you are attempting to abstract your code from Bukkit, you can use {@link PermFactory} instead.
 * If you are solely using BukkitPermFactory then it would be wise to call {@link #setPluginName(String)} early on.
 */
public class BukkitPermFactory extends PermFactory {

    /**
     * Sets this BukkitPermFactory class to use your plugin's name.
     *
     * This name can be easily prepended to your permission's name with
     * {@link com.dumptruckman.minecraft.pluginbase.permission.PermFactory#usePluginName()}.
     *
     * @param pluginName The name of your plugin, or a name of your choice.
     */
    public static void setPluginName(final String pluginName) {
        registerPermissionFactory(new PermInfo() {
            @Override
            public String getPermissionName() {
                return pluginName;
            }
        }, BukkitPermFactory.class);
    }

    /**
     * Creates a builder object for creating new {@link BukkitPerm}s.
     *
     * @param permName The name of the permission, generally without top level namespaces.
     * @return A new PermFactory object used for building a new {@link BukkitPerm}.
     */
    public static BukkitPermFactory newBukkitPerm(final String permName) {
        return new BukkitPermFactory(permName);
    }

    BukkitPermFactory(final String name) {
        super(name);
    }

    @Override
    public BukkitPermFactory desc(String description) {
        return (BukkitPermFactory) super.desc(description);
    }

    @Override
    public BukkitPermFactory child(Perm perm) {
        return (BukkitPermFactory) super.child(perm);
    }

    @Override
    public BukkitPermFactory child(String name) {
        return (BukkitPermFactory) super.child(name);
    }

    @Override
    public BukkitPermFactory child(Perm perm, boolean state) {
        return (BukkitPermFactory) super.child(perm, state);
    }

    @Override
    public BukkitPermFactory child(String name, boolean state) {
        return (BukkitPermFactory) super.child(name, state);
    }

    @Override
    public BukkitPermFactory parent(Perm perm) {
        return (BukkitPermFactory) super.parent(perm);
    }

    @Override
    public BukkitPermFactory parent(String name) {
        return (BukkitPermFactory) super.parent(name);
    }

    @Override
    public BukkitPermFactory parent(Perm perm, boolean state) {
        return (BukkitPermFactory) super.parent(perm, state);
    }

    @Override
    public BukkitPermFactory parent(String name, boolean state) {
        return (BukkitPermFactory) super.parent(name, state);
    }

    @Override
    public BukkitPermFactory addToAll() {
        return (BukkitPermFactory) super.addToAll();
    }

    @Override
    public BukkitPermFactory commandPermission() {
        return (BukkitPermFactory) super.commandPermission();
    }

    @Override
    public BukkitPermFactory def(PermDefault permissionDefault) {
        return (BukkitPermFactory) super.def(permissionDefault);
    }

    @Override
    public BukkitPermFactory usePluginName() {
        return (BukkitPermFactory) super.usePluginName();
    }

    @Override
    public BukkitPerm build() {
        return new BukkitPerm(PermFactory.permInfo, this.name, this.description, this.children, this.permissionDefault, this.parents, this.baseName);
    }
}
