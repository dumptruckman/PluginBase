/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties;

import java.util.List;
import java.util.Map;

public final class PropertyFactory {

    private PropertyFactory() {
        throw new AssertionError();
    }

    public static NullPropertyBuilder newNullProperty(final String name) {
        return new NullPropertyBuilder(name);
    }

    public static <T> SimplePropertyBuilder<T> newProperty(final Class<T> type, final String name, final T def) {
        return new SimplePropertyBuilder<T>(type, name, def);
    }

    public static <T> ListPropertyBuilder<T> newListProperty(final Class<T> type, final String name) {
        return new ListPropertyBuilder<T>(type, name);
    }

    public static <T> ListPropertyBuilder<T> newListProperty(final Class<T> type, final String name,
                                                             final List<T> def) {
        return new ListPropertyBuilder<T>(type, name, def);
    }

    public static <T> ListPropertyBuilder<T> newListProperty(final Class<T> type, final String name,
                                                             final Class<? extends List> listType) {
        return new ListPropertyBuilder<T>(type, name, listType);
    }

    public static <T> MappedPropertyBuilder<T> newMappedProperty(final Class<T> type, final String name) {
        return new MappedPropertyBuilder<T>(type, name);
    }

    public static <T> MappedPropertyBuilder<T> newMappedProperty(final Class<T> type, final String name,
                                                                 final Class<? extends Map> mapType) {
        return new MappedPropertyBuilder<T>(type, name, mapType);
    }

    public static <T> MappedPropertyBuilder<T> newMappedProperty(final Class<T> type, final String name,
                                                                 final Map<String, T> def) {
        return new MappedPropertyBuilder<T>(type, name, def);
    }

    public static <T extends NestedProperties> NestedPropertyBuilder<T> newNestedProperty(final Class<T> type,
                                                                                          final String name) {
        return new NestedPropertyBuilder<T>(type, name);
    }
}
