/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.permission;

public class BukkitPermFactory extends PermFactory {

    static {
        BukkitPerm.init();
    }

    BukkitPermFactory(final String name) {
        super(name);
    }

    @Override
    public Perm build() {
        return new BukkitPerm(PermFactory.permInfo, this.name, this.description, this.children, this.permissionDefault, this.parents, this.baseName);
    }
}
