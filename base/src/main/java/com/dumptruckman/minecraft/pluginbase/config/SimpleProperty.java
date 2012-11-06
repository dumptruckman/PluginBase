/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.config;

/**
 * This represents the simplest of Properties Entries and should represent a key : value pair where the value is not
 * a list or a map.
 *
 * @param <T> The type for the value of this entry.
 */
public interface SimpleProperty<T> extends Property<T> {

    /**
     * Retrieves the default value for a config path.
     *
     * @return The default value for a config path.
     */
    T getDefault();
}
