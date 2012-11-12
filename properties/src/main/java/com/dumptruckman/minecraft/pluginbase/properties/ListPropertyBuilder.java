/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties;

import com.dumptruckman.minecraft.pluginbase.messaging.Message;

import java.util.ArrayList;
import java.util.List;

public class ListPropertyBuilder<T> extends ValuePropertyBuilder<T> {

    private final List<T> def;
    private final Class<? extends List> listClass;

    ListPropertyBuilder(Class<T> type, String name) {
        this(type, name, null, ArrayList.class);
    }

    ListPropertyBuilder(Class<T> type, String name, List<T> def) {
        this(type, name, def, def != null ? def.getClass() : ArrayList.class);
    }

    ListPropertyBuilder(Class<T> type, String name, Class<? extends List> listClass) {
        this(type, name, null, listClass);
    }

    private ListPropertyBuilder(Class<T> type, String name, List<T> def, Class<? extends List> listClass) {
        super(type, name, false);
        this.listClass = listClass != null ? listClass : ArrayList.class;
        if (def == null) {
            try {
                this.def = listClass.newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } else {
            this.def = def;
        }
    }

    public ListPropertyBuilder<T> comment(String comment) {
        return (ListPropertyBuilder<T>) super.comment(comment);
    }

    public ListPropertyBuilder<T> serializer(PropertySerializer<T> customSerializer) {
        return (ListPropertyBuilder<T>) super.serializer(customSerializer);
    }

    public ListPropertyBuilder<T> validator(PropertyValidator<T> validator) {
        return (ListPropertyBuilder<T>) super.validator(validator);
    }

    public ListPropertyBuilder<T> description(Message message) {
        return (ListPropertyBuilder<T>) super.description(message);
    }

    public ListPropertyBuilder<T> deprecated() {
        return (ListPropertyBuilder<T>) super.deprecated();
    }

    public ListPropertyBuilder<T> alias(String alias) {
        return (ListPropertyBuilder<T>) super.alias(alias);
    }

    public ListProperty<T> build() {
        return new DefaultListProperty<T>(type, path, def, comments, new ArrayList<String>(aliases), serializer, validator, description, deprecated, defaultIfMissing, listClass);
    }
}
