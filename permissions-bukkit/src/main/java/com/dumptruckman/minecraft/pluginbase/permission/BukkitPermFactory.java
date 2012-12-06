/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.permission;

/**
 * Use this to create new BukkitPerm objects.
 *
 * If you are attempting to abstract your code from Bukkit, you can use {@link PermFactory} instead.
 */
public class BukkitPermFactory extends PermFactory {

    /**
     * Creates a builder object for creating new {@link BukkitPerm}s.
     *
     * @param permName The name of the permission, generally without top level namespaces.
     * @return A new PermFactory object used for building a new {@link BukkitPerm}.
     */
    public static BukkitPermFactory newBukkitPerm(final Class pluginClass, final String permName) {
        if (!hasFactory()) {
            registerPermissionFactory(BukkitPermFactory.class);
        }
        return new BukkitPermFactory(pluginClass, permName);
    }

    BukkitPermFactory(final Class pluginClass, final String name) {
        super(pluginClass, name);
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
    public BukkitPermFactory specificOnly() {
        return (BukkitPermFactory) super.specificOnly();
    }

    @Override
    public BukkitPerm build() {
        return new BukkitPerm(this.pluginClass, this.name, this.description, this.children, this.permissionDefault, this.parents, this.baseName, this.specificOnly);
    }
}
