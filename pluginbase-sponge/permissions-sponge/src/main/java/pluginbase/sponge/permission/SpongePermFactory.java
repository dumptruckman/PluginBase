/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.sponge.permission;

import pluginbase.permission.PermFactory;

/**
 * Use this to create new SpongePerm objects.
 * <br>
 * If you are attempting to abstract your code from Sponge, you can use {@link pluginbase.permission.PermFactory} instead.
 * @deprecated This class is not currently required by the Sponge implementation.
 */
public class SpongePermFactory extends PermFactory<SpongePermFactory, SpongePerm> {

    /**
     * Creates a builder object for creating new {@link SpongePerm}s.
     *
     * @param permName The name of the permission, generally without top level namespaces.
     * @return A new PermFactory object used for building a new {@link SpongePerm}.
     */
    public static SpongePermFactory newSpongePerm(final Class pluginClass, final String permName) {
        if (!hasFactory()) {
            registerPermissionFactory(SpongePermFactory.class);
        }
        return new SpongePermFactory(pluginClass, permName);
    }

    SpongePermFactory(final Class pluginClass, final String name) {
        super(pluginClass, name);
    }

    /** {@inheritDoc} */
    @Override
    public SpongePerm build() {
        return new SpongePerm(this.pluginClass, this.name, this.description, this.children, this.permissionDefault, this.parents, this.baseName, this.specificOnly);
    }
}
