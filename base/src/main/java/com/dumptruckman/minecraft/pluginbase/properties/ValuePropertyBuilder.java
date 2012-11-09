/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties;

import com.dumptruckman.minecraft.pluginbase.locale.Message;

import java.util.LinkedHashSet;
import java.util.Set;

abstract class ValuePropertyBuilder<T> extends PropertyBuilder<T> {

    protected final boolean defaultIfMissing;
    protected final Set<String> aliases = new LinkedHashSet<String>();

    protected PropertySerializer<T> serializer = null;
    protected PropertyValidator validator;
    protected Message description = null;
    protected boolean deprecated = false;

    public ValuePropertyBuilder(Class<T> type, String name, boolean allowNull) {
        super(type, name);
        this.defaultIfMissing = !allowNull;
        this.validator = new DefaultValidator();
    }

    public ValuePropertyBuilder<T> comment(String comment) {
        return (ValuePropertyBuilder<T>) super.comment(comment);
    }

    public ValuePropertyBuilder<T> serializer(PropertySerializer<T> customSerializer) {
        serializer = customSerializer;
        return this;
    }

    public ValuePropertyBuilder<T> validator(PropertyValidator validator) {
        this.validator = validator;
        return this;
    }

    public ValuePropertyBuilder<T> description(Message message) {
        description = message;
        return this;
    }

    public ValuePropertyBuilder<T> deprecated() {
        deprecated = true;
        return this;
    }

    public ValuePropertyBuilder<T> alias(String alias) {
        this.aliases.add(alias);
        return this;
    }

    public abstract ValueProperty<T> build();
}
