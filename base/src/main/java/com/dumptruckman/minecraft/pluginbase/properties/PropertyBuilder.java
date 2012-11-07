/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties;

import com.dumptruckman.minecraft.pluginbase.locale.Message;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

abstract class PropertyBuilder<T> {

    protected final String path;
    protected final Class<T> type;
    protected final boolean defaultIfMissing;

    protected PropertySerializer<T> serializer;
    protected PropertyValidator validator;
    protected List<String> comments = new ArrayList<String>();
    protected Set<String> aliases = new LinkedHashSet<String>();
    protected Message description = null;
    protected boolean deprecated = false;

    public PropertyBuilder(Class<T> type, String name, boolean allowNull) {
        this.path = name;
        this.type = type;
        this.defaultIfMissing = !allowNull;
        if (type.equals(String.class)) {
            this.serializer = new StringStringSerializer<T>(type);
        } else {
            try {
                this.serializer = new DefaultStringSerializer<T>(type);
            } catch (IllegalArgumentException e) {
                this.serializer = new DefaultSerializer<T>(type);
            }
        }
        this.validator = new DefaultValidator();
    }

    public PropertyBuilder<T> comment(String comment) {
        comments.add(comment);
        return this;
    }

    public PropertyBuilder<T> serializer(PropertySerializer<T> customSerializer) {
        serializer = customSerializer;
        return this;
    }

    public PropertyBuilder<T> validator(PropertyValidator validator) {
        this.validator = validator;
        return this;
    }

    public PropertyBuilder<T> description(Message message) {
        description = message;
        return this;
    }

    public PropertyBuilder<T> deprecated() {
        deprecated = true;
        return this;
    }

    public PropertyBuilder<T> alias(String alias) {
        this.aliases.add(alias);
        return this;
    }

    public abstract Property<T> build();
}
