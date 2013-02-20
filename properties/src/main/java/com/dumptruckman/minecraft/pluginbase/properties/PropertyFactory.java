/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties;

import com.dumptruckman.minecraft.pluginbase.properties.builders.ListPropertyBuilder;
import com.dumptruckman.minecraft.pluginbase.properties.builders.MappedPropertyBuilder;
import com.dumptruckman.minecraft.pluginbase.properties.builders.NestedPropertyBuilder;
import com.dumptruckman.minecraft.pluginbase.properties.builders.NullPropertyBuilder;
import com.dumptruckman.minecraft.pluginbase.properties.builders.PropertyBuilder;
import com.dumptruckman.minecraft.pluginbase.properties.builders.SimplePropertyBuilder;

import java.util.List;
import java.util.Map;

public final class PropertyFactory extends PropertyBuilder {

    private PropertyFactory() {
        throw new AssertionError();
    }

    public static NullPropertyBuilder newNullProperty(final String name) {
        return newNullPropertyBuilder(name);
    }

    public static <T> SimplePropertyBuilder<T> newProperty(final Class<T> type, final String name, final T def) {
        return newSimplePropertyBuilder(type, name, def);
    }

    public static <T> ListPropertyBuilder<T> newListProperty(final Class<T> type, final String name) {
        return newListPropertyBuilder(type, name);
    }

    public static <T> ListPropertyBuilder<T> newListProperty(final Class<T> type, final String name,
                                                             final List<T> def) {
        return newListPropertyBuilder(type, name, def);
    }

    public static <T> ListPropertyBuilder<T> newListProperty(final Class<T> type, final String name,
                                                             final Class<? extends List> listType) {
        return newListPropertyBuilder(type, name, listType);
    }

    public static <T> MappedPropertyBuilder<T> newMappedProperty(final Class<T> type, final String name) {
        return newMappedPropertyBuilder(type, name);
    }

    public static <T> MappedPropertyBuilder<T> newMappedProperty(final Class<T> type, final String name,
                                                                 final Class<? extends Map> mapType) {
        return newMappedPropertyBuilder(type, name, mapType);
    }

    public static <T> MappedPropertyBuilder<T> newMappedProperty(final Class<T> type, final String name,
                                                                 final Map<String, T> def) {
        return newMappedPropertyBuilder(type, name, def);
    }

    public static <T extends NestedProperties> NestedPropertyBuilder<T> newNestedProperty(final Class<T> type,
                                                                                          final String name) {
        return newNestedPropertyBuilder(type, name);
    }

    @Override
    public Property build() {
        throw new UnsupportedOperationException();
    }
}
