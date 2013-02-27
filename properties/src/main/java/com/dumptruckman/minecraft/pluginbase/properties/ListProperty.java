/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A {@link ValueProperty} that represents a list of objects of some type.
 * <p/>
 * See {@link com.dumptruckman.minecraft.pluginbase.properties.PropertyFactory} for creating properties.
 *
 * @param <T> the type of the objects in the List.
 */
public interface ListProperty<T> extends ValueProperty<T> {

    /**
     * Creates a new list of the type defined by {@link T}.
     *
     * @return a new list of the type defined by {@link T}.
     */
    @NotNull
    List<T> getNewTypeList();

    /** {@inheritDoc} */
    @Nullable
    List<T> getDefault();
}
