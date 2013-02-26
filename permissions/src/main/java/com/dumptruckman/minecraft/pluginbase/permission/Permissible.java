/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.permission;

import org.jetbrains.annotations.NotNull;

public interface Permissible {

    /**
     * Gets the value of the specified permission, if set.
     * <p/>
     * If a permission override is not set on this object, the default value of the permission will be returned.
     *
     * @param perm permission to get.
     * @return value of permission.
     */
    boolean hasPermission(@NotNull final String perm);

    public void hasPerm(@NotNull final Perm perm);

    public void hasPerm(@NotNull final Perm perm, @NotNull final String specific);
}
