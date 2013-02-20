/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties.builders;

import com.dumptruckman.minecraft.pluginbase.messages.Message;
import com.dumptruckman.minecraft.pluginbase.properties.PropertyValidator;
import com.dumptruckman.minecraft.pluginbase.properties.SimpleProperty;
import com.dumptruckman.minecraft.pluginbase.properties.serializers.PropertySerializer;

import java.util.List;

class DefaultSimpleProperty<T> extends DefaultValueProperty<T> implements SimpleProperty<T> {

    private final T def;

    public DefaultSimpleProperty(Class<T> type, String path, T def, List<String> comments, List<String> aliases,
                                 PropertySerializer<T> serializer, PropertyValidator validator, Message description,
                                 boolean deprecated, boolean defaultIfMissing) {
        super(type, path, comments, aliases, serializer, validator, description, deprecated, defaultIfMissing);
        this.def = def;
    }
    /**
     * Retrieves the default value for a config path.
     *
     * @return The default value for a config path.
     */
    public T getDefault() {
        return this.def;
    }
}
