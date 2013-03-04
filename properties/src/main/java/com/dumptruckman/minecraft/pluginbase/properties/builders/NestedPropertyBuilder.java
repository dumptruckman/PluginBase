/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties.builders;

import com.dumptruckman.minecraft.pluginbase.properties.NestedProperties;
import com.dumptruckman.minecraft.pluginbase.properties.NestedProperty;
import org.jetbrains.annotations.NotNull;

/**
 * Used in the construction of a {@link NestedProperty}.
 * <p/>
 * See {@link com.dumptruckman.minecraft.pluginbase.properties.PropertyFactory} to get started.
 *
 * @param <T> the type that represents the class holding the properties for the NestedProperty.
 */
public class NestedPropertyBuilder<T extends NestedProperties> extends PropertyBuilder<T> {

    public NestedPropertyBuilder(@NotNull final Class<T> type, @NotNull final String name) {
        super(type, name);
    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public NestedPropertyBuilder<T> comment(@NotNull final String comment) {
        return (NestedPropertyBuilder<T>) super.comment(comment);
    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public NestedProperty<T> build() {
        return new DefaultNestedProperty<T>(type, key, comments);
    }
}
