/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties.builders;

import com.dumptruckman.minecraft.pluginbase.messages.Message;
import com.dumptruckman.minecraft.pluginbase.properties.MappedProperty;
import com.dumptruckman.minecraft.pluginbase.properties.PropertyValidator;
import com.dumptruckman.minecraft.pluginbase.properties.serializers.PropertySerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Used in the construction of a {@link MappedProperty}.
 * <p/>
 * See {@link com.dumptruckman.minecraft.pluginbase.properties.PropertyFactory} to get started.
 *
 * @param <T> the type for the value of the map.
 */
public class MappedPropertyBuilder<T> extends ValuePropertyBuilder<T> {

    @NotNull
    private final Map<String, T> def;
    @NotNull
    private final Class<? extends Map> mapClass;

    MappedPropertyBuilder(@NotNull final Class<T> type, @NotNull final String name) {
        this(type, name, null, HashMap.class);
    }

    MappedPropertyBuilder(@NotNull final Class<T> type, @NotNull final String name,
                          @NotNull final Map<String, T> def) {
        this(type, name, def, def.getClass());
    }

    MappedPropertyBuilder(@NotNull final Class<T> type, @NotNull final String name,
                          @NotNull final Class<? extends Map> mapClass) {
        this(type, name, null, mapClass);
    }

    private MappedPropertyBuilder(@NotNull final Class<T> type,
                                  @NotNull final String name,
                                  @SuppressWarnings("all") @Nullable final Map<String, T> def,
                                  @NotNull final Class<? extends Map> mapClass) {
        super(type, name, false);
        this.mapClass = mapClass;
        if (def == null) {
            try {
                @SuppressWarnings({"unchecked", "UnnecessaryLocalVariable"})
                final Map<String, T> map = mapClass.newInstance();
                this.def = map;
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } else {
            this.def = def;
        }
    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public MappedPropertyBuilder<T> comment(@NotNull final String comment) {
        return (MappedPropertyBuilder<T>) super.comment(comment);
    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public MappedPropertyBuilder<T> serializer(@NotNull final PropertySerializer<T> customSerializer) {
        return (MappedPropertyBuilder<T>) super.serializer(customSerializer);
    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public MappedPropertyBuilder<T> validator(@NotNull final PropertyValidator<T> validator) {
        return (MappedPropertyBuilder<T>) super.validator(validator);
    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public MappedPropertyBuilder<T> description(@NotNull final Message message) {
        return (MappedPropertyBuilder<T>) super.description(message);
    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public MappedPropertyBuilder<T> deprecated() {
        return (MappedPropertyBuilder<T>) super.deprecated();
    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public MappedPropertyBuilder<T> alias(@NotNull final String alias) {
        return (MappedPropertyBuilder<T>) super.alias(alias);
    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public MappedProperty<T> build() {
        return new DefaultMappedProperty<T>(type, key, def, comments, new ArrayList<String>(aliases), serializer, validator, description, deprecated, defaultIfMissing, mapClass);
    }
}
