/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties;

import com.dumptruckman.minecraft.pluginbase.locale.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MappedPropertyBuilder<T> extends ValuePropertyBuilder<T> {

    private final Map<String, T> def;
    private final Class<? extends Map> mapClass;

    MappedPropertyBuilder(Class<T> type, String name) {
        this(type, name, null, HashMap.class);
    }

    MappedPropertyBuilder(Class<T> type, String name, Map<String, T> def) {
        this(type, name, def, def != null ? def.getClass() : HashMap.class);
    }

    MappedPropertyBuilder(Class<T> type, String name, Class<? extends Map> mapClass) {
        this(type, name, null, mapClass);
    }

    private MappedPropertyBuilder(Class<T> type, String name, Map<String, T> def, Class<? extends Map> mapClass) {
        super(type, name, false);
        this.mapClass = mapClass != null ? mapClass : HashMap.class;
        if (def == null) {
            try {
                this.def = mapClass.newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } else {
            this.def = def;
        }
    }

    public MappedPropertyBuilder<T> comment(String comment) {
        return (MappedPropertyBuilder<T>) super.comment(comment);
    }

    public MappedPropertyBuilder<T> serializer(PropertySerializer<T> customSerializer) {
        return (MappedPropertyBuilder<T>) super.serializer(customSerializer);
    }

    public MappedPropertyBuilder<T> validator(PropertyValidator validator) {
        return (MappedPropertyBuilder<T>) super.validator(validator);
    }

    public MappedPropertyBuilder<T> description(Message message) {
        return (MappedPropertyBuilder<T>) super.description(message);
    }

    public MappedPropertyBuilder<T> deprecated() {
        return (MappedPropertyBuilder<T>) super.deprecated();
    }

    public MappedPropertyBuilder<T> alias(String alias) {
        return (MappedPropertyBuilder<T>) super.alias(alias);
    }

    public MappedProperty<T> build() {
        return new DefaultMappedProperty<T>(type, path, def, comments, new ArrayList<String>(aliases), serializer, validator, description, deprecated, defaultIfMissing, mapClass);
    }
}
