/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.permission;

import java.util.Map;

public abstract class Perm {

    protected static final String SEPARATOR = ".";

    /**
     * ALL plugin permissions.
     */
    public static final Perm ALL = PermFactory.newPerm("*").usePluginName().build();

    /**
     * ALL commmand permissions.
     */
    public static final Perm ALL_CMD = PermFactory.newPerm("cmd.*").usePluginName().addToAll().build();

    protected final String name;
    protected final String description;
    protected final Map<String, Boolean> children;
    protected final PermDefault permDefault;
    protected final Map<String, Boolean> parents;

    Perm(final PermInfo permInfo, final String name, final String description, final Map<String, Boolean> children,
         final PermDefault permDefault, final Map<String, Boolean> parents, final boolean baseName) {
        if (baseName && permInfo != null) {
            this.name = permInfo.getPermissionName().toLowerCase() + SEPARATOR + name.toLowerCase();
        } else {
            this.name = name.toLowerCase();
        }
        this.description = description;
        this.children = children;
        this.permDefault = permDefault;
        this.parents = parents;
    }

    public final String getName() {
        return name;
    }

    public String getDescription() {
        return this.description;
    }

    public Map<String, Boolean> getChildren() {
        return this.children;
    }

    public Map<String, Boolean> getParents() {
        return this.parents;
    }

    public PermDefault getPermDefault() {
        return this.permDefault;
    }

    /**
     * Checks if the sender has the node in question.
     *
     * @param permissible Permissible to check permission for.
     * @return True if sender has the permission.
     */
    public final boolean hasPermission(final Permissible permissible) {
        verify(getName());
        return permissible.hasPermission(getName());
    }

    public final boolean hasPermission(final Permissible permissible, final String specific) {
        final String fullName = getName() + SEPARATOR + specific;
        verify(fullName);
        return permissible.hasPermission(fullName);
    }

    protected abstract void verify(final String name);
}
