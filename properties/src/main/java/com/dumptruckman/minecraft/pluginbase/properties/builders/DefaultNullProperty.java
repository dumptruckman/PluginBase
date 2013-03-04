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
import org.jetbrains.annotations.Nullable;

import java.util.List;

class DefaultNullProperty extends DefaultValueProperty<Null> implements NullProperty {

    public DefaultNullProperty(@NotNull final String path, @NotNull final List<String> comments,
                               @NotNull final List<String> aliases, @Nullable final PropertySerializer<Null> serializer,
                               @NotNull final PropertyValidator<Null> validator, @NotNull final Message description,
                               boolean deprecated, boolean defaultIfMissing) {
        super(Null.class, path, comments, aliases, serializer, validator, description, deprecated, defaultIfMissing);
    }

    @Nullable
    @Override
    public Object getDefault() {
        return null;
    }
}
