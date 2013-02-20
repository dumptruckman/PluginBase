/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties.builders;

import com.dumptruckman.minecraft.pluginbase.messages.Message;
import com.dumptruckman.minecraft.pluginbase.properties.Null;
import com.dumptruckman.minecraft.pluginbase.properties.NullProperty;
import com.dumptruckman.minecraft.pluginbase.properties.PropertyValidator;
import com.dumptruckman.minecraft.pluginbase.properties.serializers.PropertySerializer;

import java.util.ArrayList;

public class NullPropertyBuilder extends ValuePropertyBuilder<Null> {

    NullPropertyBuilder(String name) {
        super(Null.class, name, true);
    }

    public NullPropertyBuilder comment(String comment) {
        return (NullPropertyBuilder) super.comment(comment);
    }

    public NullPropertyBuilder description(Message message) {
        return (NullPropertyBuilder) super.description(message);
    }

    public NullPropertyBuilder deprecated() {
        return (NullPropertyBuilder) super.deprecated();
    }

    public NullPropertyBuilder alias(String alias) {
        return (NullPropertyBuilder) super.alias(alias);
    }

    @Override
    public NullPropertyBuilder serializer(PropertySerializer<Null> customSerializer) {
        return this;
    }

    @Override
    public NullPropertyBuilder validator(PropertyValidator<Null> validator) {
        return this;
    }

    public NullProperty build() {
        return new DefaultNullProperty(path, comments, new ArrayList<String>(aliases), serializer, validator, description, deprecated, defaultIfMissing);
    }
}
