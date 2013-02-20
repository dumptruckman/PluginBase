/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties.builders;

import com.dumptruckman.minecraft.pluginbase.properties.NestedProperties;
import com.dumptruckman.minecraft.pluginbase.properties.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class PropertyBuilder<T> {

    protected final String path;
    protected final Class<T> type;
    protected final List<String> comments = new ArrayList<String>();

    protected PropertyBuilder() {
        throw new AssertionError();
    }

    protected PropertyBuilder(Class<T> type, String name) {
        this.path = name;
        this.type = type;
    }

    protected PropertyBuilder<T> comment(String comment) {
        comments.add(comment);
        return this;
    }

    public abstract Property<T> build();

    protected static NullPropertyBuilder newNullPropertyBuilder(final String name) {
        return new NullPropertyBuilder(name);
    }

    protected static <T> SimplePropertyBuilder<T> newSimplePropertyBuilder(final Class<T> type, final String name, final T def) {
        return new SimplePropertyBuilder<T>(type, name, def);
    }

    protected static <T> ListPropertyBuilder<T> newListPropertyBuilder(final Class<T> type, final String name) {
        return new ListPropertyBuilder<T>(type, name);
    }

    protected static <T> ListPropertyBuilder<T> newListPropertyBuilder(final Class<T> type, final String name,
                                                             final List<T> def) {
        return new ListPropertyBuilder<T>(type, name, def);
    }

    protected static <T> ListPropertyBuilder<T> newListPropertyBuilder(final Class<T> type, final String name,
                                                             final Class<? extends List> listType) {
        return new ListPropertyBuilder<T>(type, name, listType);
    }

    protected static <T> MappedPropertyBuilder<T> newMappedPropertyBuilder(final Class<T> type, final String name) {
        return new MappedPropertyBuilder<T>(type, name);
    }

    protected static <T> MappedPropertyBuilder<T> newMappedPropertyBuilder(final Class<T> type, final String name,
                                                                 final Class<? extends Map> mapType) {
        return new MappedPropertyBuilder<T>(type, name, mapType);
    }

    protected static <T> MappedPropertyBuilder<T> newMappedPropertyBuilder(final Class<T> type, final String name,
                                                                 final Map<String, T> def) {
        return new MappedPropertyBuilder<T>(type, name, def);
    }

    protected static <T extends NestedProperties> NestedPropertyBuilder<T> newNestedPropertyBuilder(final Class<T> type,
                                                                                          final String name) {
        return new NestedPropertyBuilder<T>(type, name);
    }
}
