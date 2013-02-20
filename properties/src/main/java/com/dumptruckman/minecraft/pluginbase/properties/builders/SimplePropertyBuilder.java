/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties.builders;

import com.dumptruckman.minecraft.pluginbase.messages.Message;
import com.dumptruckman.minecraft.pluginbase.properties.PropertyValidator;
import com.dumptruckman.minecraft.pluginbase.properties.SimpleProperty;
import com.dumptruckman.minecraft.pluginbase.properties.serializers.PropertySerializer;

import java.util.ArrayList;

public class SimplePropertyBuilder<T> extends ValuePropertyBuilder<T> {

    private final T def;

    SimplePropertyBuilder(Class<T> type, String name, T def) {
        super(type, name, def == null);
        this.def = def;
    }

    public SimplePropertyBuilder<T> comment(String comment) {
        return (SimplePropertyBuilder<T>) super.comment(comment);
    }

    public SimplePropertyBuilder<T> serializer(PropertySerializer<T> customSerializer) {
        return (SimplePropertyBuilder<T>) super.serializer(customSerializer);
    }

    public SimplePropertyBuilder<T> validator(PropertyValidator<T> validator) {
        return (SimplePropertyBuilder<T>) super.validator(validator);
    }

    public SimplePropertyBuilder<T> description(Message message) {
        return (SimplePropertyBuilder<T>) super.description(message);
    }

    public SimplePropertyBuilder<T> deprecated() {
        return (SimplePropertyBuilder<T>) super.deprecated();
    }

    public SimplePropertyBuilder<T> alias(String alias) {
        return (SimplePropertyBuilder<T>) super.alias(alias);
    }

    public SimpleProperty<T> build() {
        return new DefaultSimpleProperty<T>(type, path, def, comments, new ArrayList<String>(aliases), serializer, validator, description, deprecated, defaultIfMissing);
    }
}
