/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties.builders;

import com.dumptruckman.minecraft.pluginbase.messages.Message;
import com.dumptruckman.minecraft.pluginbase.properties.PropertyValidator;
import com.dumptruckman.minecraft.pluginbase.properties.SimpleProperty;
import com.dumptruckman.minecraft.pluginbase.properties.serializers.PropertySerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * Used in the construction of a {@link SimpleProperty}.
 * <p/>
 * See {@link com.dumptruckman.minecraft.pluginbase.properties.PropertyFactory} to get started.
 *
 * @param <T> the type for the property.
 */
public class SimplePropertyBuilder<T> extends ValuePropertyBuilder<T> {

    @Nullable
    private final T def;

    SimplePropertyBuilder(@NotNull final Class<T> type, @NotNull final String name, @Nullable T def) {
        super(type, name, def == null);
        this.def = def;
    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public SimplePropertyBuilder<T> comment(@NotNull final String comment) {
        return (SimplePropertyBuilder<T>) super.comment(comment);
    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public SimplePropertyBuilder<T> serializer(@NotNull final PropertySerializer<T> customSerializer) {
        return (SimplePropertyBuilder<T>) super.serializer(customSerializer);
    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public SimplePropertyBuilder<T> validator(@NotNull final PropertyValidator<T> validator) {
        return (SimplePropertyBuilder<T>) super.validator(validator);
    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public SimplePropertyBuilder<T> description(@NotNull final Message message) {
        return (SimplePropertyBuilder<T>) super.description(message);
    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public SimplePropertyBuilder<T> deprecated() {
        return (SimplePropertyBuilder<T>) super.deprecated();
    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public SimplePropertyBuilder<T> alias(@NotNull final String alias) {
        return (SimplePropertyBuilder<T>) super.alias(alias);
    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public SimpleProperty<T> build() {
        return new DefaultSimpleProperty<T>(type, key, def, comments, new ArrayList<String>(aliases), serializer, validator, description, deprecated, defaultIfMissing);
    }
}
