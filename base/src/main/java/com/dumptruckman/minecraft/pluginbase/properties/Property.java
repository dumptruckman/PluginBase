/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties;

import java.util.List;

interface Property<T> {

    /**
     * Retrieves the path for a config option.
     *
     * @return The path for a config option.
     */
    String getName();

    /**
     * Retrieves the type of entry this is.
     *
     * @return the type of entry this is.
     */
    Class<T> getType();

    /**
     * Retrieves the comment for a config path.
     *
     * @return The comments for a config path.
     */
    List<String> getComments();
}
