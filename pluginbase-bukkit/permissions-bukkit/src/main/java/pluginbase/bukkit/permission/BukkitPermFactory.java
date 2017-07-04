/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.bukkit.permission;

import pluginbase.permission.PermFactory;

/**
 * Use this to create new BukkitPerm objects.
 * <br>
 * If you are attempting to abstract your code from Bukkit, you can use {@link pluginbase.permission.PermFactory} instead.
 */
public class BukkitPermFactory extends PermFactory<BukkitPermFactory, BukkitPerm> {

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
    public BukkitPerm build() {
        return new BukkitPerm(this.pluginClass, this.name, this.description, this.children, this.permissionDefault, this.parents, this.baseName, this.specificOnly);
    }
}
