/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.permission;

import org.jetbrains.annotations.NotNull;

/**
 * Represents an object that is capable of having permissions.
 */
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

    /**
     * Gets the value of the specified permission, if set.
     * <p/>
     * If a permission override is not set on this object, the default value of the permission will be returned.
     *
     * @param permission permission to get.
     * @return value of permission.
     */
    public boolean hasPerm(@NotNull final Permission permission);

    /**
     * Gets the value of the specified permission, if set.
     * <p/>
     * If a permission override is not set on this object, the default value of the permission will be returned.
     *
     * @param permission permission to get.
     * @return value of permission.
     */
    public boolean hasPerm(@NotNull final Permission permission, @NotNull final String specific);
}
