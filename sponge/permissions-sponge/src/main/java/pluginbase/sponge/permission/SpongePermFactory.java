/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.sponge.permission;

import pluginbase.permission.Perm;
import pluginbase.permission.PermDefault;
import pluginbase.permission.PermFactory;

/**
 * Use this to create new SpongePerm objects.
 * <p/>
 * If you are attempting to abstract your code from Sponge, you can use {@link pluginbase.permission.PermFactory} instead.
 * @deprecated This class is not currently required by the Sponge implementation.
 */
public class SpongePermFactory extends PermFactory {

    /**
     * Creates a builder object for creating new {@link SpongePerm}s.
     *
     * @param permName The name of the permission, generally without top level namespaces.
     * @return A new PermFactory object used for building a new {@link SpongePerm}.
     */
    public static SpongePermFactory newBukkitPerm(final Class pluginClass, final String permName) {
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
    public SpongePermFactory desc(String description) {
        return (SpongePermFactory) super.desc(description);
    }

    /** {@inheritDoc} */
    @Override
    public SpongePermFactory child(Perm perm) {
        return (SpongePermFactory) super.child(perm);
    }

    /** {@inheritDoc} */
    @Override
    public SpongePermFactory child(String name) {
        return (SpongePermFactory) super.child(name);
    }

    /** {@inheritDoc} */
    @Override
    public SpongePermFactory child(Perm perm, boolean state) {
        return (SpongePermFactory) super.child(perm, state);
    }

    /** {@inheritDoc} */
    @Override
    public SpongePermFactory child(String name, boolean state) {
        return (SpongePermFactory) super.child(name, state);
    }

    /** {@inheritDoc} */
    @Override
    public SpongePermFactory parent(Perm perm) {
        return (SpongePermFactory) super.parent(perm);
    }

    /** {@inheritDoc} */
    @Override
    public SpongePermFactory parent(String name) {
        return (SpongePermFactory) super.parent(name);
    }

    /** {@inheritDoc} */
    @Override
    public SpongePermFactory parent(Perm perm, boolean state) {
        return (SpongePermFactory) super.parent(perm, state);
    }

    /** {@inheritDoc} */
    @Override
    public SpongePermFactory parent(String name, boolean state) {
        return (SpongePermFactory) super.parent(name, state);
    }

    /** {@inheritDoc} */
    @Override
    public SpongePermFactory addToAll() {
        return (SpongePermFactory) super.addToAll();
    }

    /** {@inheritDoc} */
    @Override
    public SpongePermFactory commandPermission() {
        return (SpongePermFactory) super.commandPermission();
    }

    /** {@inheritDoc} */
    @Override
    public SpongePermFactory def(PermDefault permissionDefault) {
        return (SpongePermFactory) super.def(permissionDefault);
    }

    /** {@inheritDoc} */
    @Override
    public SpongePermFactory usePluginName() {
        return (SpongePermFactory) super.usePluginName();
    }

    /** {@inheritDoc} */
    @Override
    public SpongePermFactory specificOnly() {
        return (SpongePermFactory) super.specificOnly();
    }

    /** {@inheritDoc} */
    @Override
    public SpongePerm build() {
        return new SpongePerm(this.pluginClass, this.name, this.description, this.children, this.permissionDefault, this.parents, this.baseName, this.specificOnly);
    }
}
