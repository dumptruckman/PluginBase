/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.bukkit.permission;

import pluginbase.permission.Perm;
import pluginbase.permission.PermDefault;
import pluginbase.permission.PermFactory;

/**
 * Use this to create new BukkitPerm objects.
 * <p/>
 * If you are attempting to abstract your code from Bukkit, you can use {@link pluginbase.permission.PermFactory} instead.
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

    /** {@inheritDoc} */
    @Override
    public BukkitPermFactory desc(String description) {
        return (BukkitPermFactory) super.desc(description);
    }

    /** {@inheritDoc} */
    @Override
    public BukkitPermFactory child(Perm perm) {
        return (BukkitPermFactory) super.child(perm);
    }

    /** {@inheritDoc} */
    @Override
    public BukkitPermFactory child(String name) {
        return (BukkitPermFactory) super.child(name);
    }

    /** {@inheritDoc} */
    @Override
    public BukkitPermFactory child(Perm perm, boolean state) {
        return (BukkitPermFactory) super.child(perm, state);
    }

    /** {@inheritDoc} */
    @Override
    public BukkitPermFactory child(String name, boolean state) {
        return (BukkitPermFactory) super.child(name, state);
    }

    /** {@inheritDoc} */
    @Override
    public BukkitPermFactory parent(Perm perm) {
        return (BukkitPermFactory) super.parent(perm);
    }

    /** {@inheritDoc} */
    @Override
    public BukkitPermFactory parent(String name) {
        return (BukkitPermFactory) super.parent(name);
    }

    /** {@inheritDoc} */
    @Override
    public BukkitPermFactory parent(Perm perm, boolean state) {
        return (BukkitPermFactory) super.parent(perm, state);
    }

    /** {@inheritDoc} */
    @Override
    public BukkitPermFactory parent(String name, boolean state) {
        return (BukkitPermFactory) super.parent(name, state);
    }

    /** {@inheritDoc} */
    @Override
    public BukkitPermFactory addToAll() {
        return (BukkitPermFactory) super.addToAll();
    }

    /** {@inheritDoc} */
    @Override
    public BukkitPermFactory commandPermission() {
        return (BukkitPermFactory) super.commandPermission();
    }

    /** {@inheritDoc} */
    @Override
    public BukkitPermFactory def(PermDefault permissionDefault) {
        return (BukkitPermFactory) super.def(permissionDefault);
    }

    /** {@inheritDoc} */
    @Override
    public BukkitPermFactory usePluginName() {
        return (BukkitPermFactory) super.usePluginName();
    }

    /** {@inheritDoc} */
    @Override
    public BukkitPermFactory specificOnly() {
        return (BukkitPermFactory) super.specificOnly();
    }

    /** {@inheritDoc} */
    @Override
    public BukkitPerm build() {
        return new BukkitPerm(this.pluginClass, this.name, this.description, this.children, this.permissionDefault, this.parents, this.baseName, this.specificOnly);
    }
}
