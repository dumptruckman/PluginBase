/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties.builders;

import com.dumptruckman.minecraft.pluginbase.messages.Message;
import com.dumptruckman.minecraft.pluginbase.properties.Null;
import com.dumptruckman.minecraft.pluginbase.properties.NullProperty;
import com.dumptruckman.minecraft.pluginbase.properties.PropertyValidator;
import com.dumptruckman.minecraft.pluginbase.properties.serializers.PropertySerializer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Used in the construction of a {@link NullProperty}.
 * <p/>
 * See {@link com.dumptruckman.minecraft.pluginbase.properties.PropertyFactory} to get started.
 */
public class NullPropertyBuilder extends ValuePropertyBuilder<Null> {

    NullPropertyBuilder(@NotNull final String name) {
        super(Null.class, name, true);
    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public NullPropertyBuilder comment(@NotNull final String comment) {
        return (NullPropertyBuilder) super.comment(comment);
    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public NullPropertyBuilder description(@NotNull final Message message) {
        return (NullPropertyBuilder) super.description(message);
    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public NullPropertyBuilder deprecated() {
        return (NullPropertyBuilder) super.deprecated();
    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public NullPropertyBuilder alias(@NotNull final String alias) {
        return (NullPropertyBuilder) super.alias(alias);
    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public NullPropertyBuilder serializer(@NotNull final PropertySerializer<Null> customSerializer) {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public NullPropertyBuilder validator(@NotNull final PropertyValidator<Null> validator) {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public NullProperty build() {
        return new DefaultNullProperty(key, comments, new ArrayList<String>(aliases), serializer, validator, description, deprecated, defaultIfMissing);
    }
}
