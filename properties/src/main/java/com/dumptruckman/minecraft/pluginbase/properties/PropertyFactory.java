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
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * Assists with the creation of all kinds of {@link Property} objects.
 */
public final class PropertyFactory extends PropertyBuilder {

    private PropertyFactory() {
        throw new AssertionError();
    }

    /**
     * Creates a new builder for a new NullProperty which has no value and typically just represents a namespace that can be commented.
     *
     * @param name the name of the property.  Used for identification with persistence.
     * @return a property builder to continue building the new property.
     */
    public static NullPropertyBuilder newNullProperty(@NotNull final String name) {
        return newNullPropertyBuilder(name);
    }

    /**
     * Creates a new builder for a new SimplyProperty which typically has a non-Collection value.
     *
     * @param type the class for the property value type.
     * @param name the name of the property.  Used for identification with persistence.
     * @param def the default value.
     * @param <T> the type for the property value.
     * @return a property builder to continue building the new property.
     */
    public static <T> SimplePropertyBuilder<T> newProperty(@NotNull final Class<T> type,
                                                           @NotNull final String name,
                                                           @NotNull final T def) {
        return newSimplePropertyBuilder(type, name, def);
    }

    /**
     * Creates a new builder for a new ListProperty.
     *
     * @param type the class for the list property value type. For instance, for a {@code List<String>} you should use
     *             String.class.
     * @param name the name of the property.  Used for identification with persistence.
     * @param <T> the type for the property value.
     * @return a property builder to continue building the new property.
     */
    public static <T> ListPropertyBuilder<T> newListProperty(@NotNull final Class<T> type,
                                                             @NotNull final String name) {
        return newListPropertyBuilder(type, name);
    }

    /**
     * Creates a new builder for a new ListProperty.
     *
     * @param type the class for the list property value type. For instance, for a {@code List<String>} you should use
     *             String.class.
     * @param name the name of the property.  Used for identification with persistence.
     * @param def the default value.
     * @param <T> the type for the property value.
     * @return a property builder to continue building the new property.
     */
    public static <T> ListPropertyBuilder<T> newListProperty(@NotNull final Class<T> type,
                                                             @NotNull final String name,
                                                             @NotNull final List<T> def) {
        return newListPropertyBuilder(type, name, def);
    }

    /**
     * Creates a new builder for a new ListProperty.
     *
     * @param type the class for the list property value type. For instance, for a {@code List<String>} you should use
     *             String.class.
     * @param name the name of the property.  Used for identification with persistence.
     * @param listType allows you define the type of list to use.
     * @param <T> the type for the property value.
     * @return a property builder to continue building the new property.
     */
    public static <T> ListPropertyBuilder<T> newListProperty(@NotNull final Class<T> type,
                                                             @NotNull final String name,
                                                             @NotNull final Class<? extends List> listType) {
        return newListPropertyBuilder(type, name, listType);
    }

    /**
     * Creates a new builder for a new MappedProperty.
     * <p/>
     * All MappedPropertys represent a {@code Map<String, T>}
     *
     * @param type the class for the map property value type. For instance, for a {@code Map<String, Boolean>} you should use
     *             Boolean.class.
     * @param name the name of the property.  Used for identification with persistence.
     * @param <T> the type for the property value.
     * @return a property builder to continue building the new property.
     */
    public static <T> MappedPropertyBuilder<T> newMappedProperty(@NotNull final Class<T> type,
                                                                 @NotNull final String name) {
        return newMappedPropertyBuilder(type, name);
    }

    /**
     * Creates a new builder for a new MappedProperty.
     * <p/>
     * All MappedPropertys represent a {@code Map<String, T>}
     *
     * @param type the class for the map property value type. For instance, for a {@code Map<String, Boolean>} you should use
     *             Boolean.class.
     * @param name the name of the property.  Used for identification with persistence.
     * @param mapType allows you define the type of map to use.
     * @param <T> the type for the property value.
     * @return a property builder to continue building the new property.
     */
    public static <T> MappedPropertyBuilder<T> newMappedProperty(@NotNull final Class<T> type,
                                                                 @NotNull final String name,
                                                                 @NotNull final Class<? extends Map> mapType) {
        return newMappedPropertyBuilder(type, name, mapType);
    }

    /**
     * Creates a new builder for a new MappedProperty.
     * <p/>
     * All MappedPropertys represent a {@code Map<String, T>}
     *
     * @param type the class for the map property value type. For instance, for a {@code Map<String, Boolean>} you should use
     *             Boolean.class.
     * @param name the name of the property.  Used for identification with persistence.
     * @param def the default value.
     * @param <T> the type for the property value.
     * @return a property builder to continue building the new property.
     */
    public static <T> MappedPropertyBuilder<T> newMappedProperty(@NotNull final Class<T> type,
                                                                 @NotNull final String name,
                                                                 @NotNull final Map<String, T> def) {
        return newMappedPropertyBuilder(type, name, def);
    }

    /**
     * Creates a new builder for a new NestedProperty.
     *
     * @param type the class that represents the nested properties.
     * @param name the name of this new nested property.
     * @param <T> the type that represents the nested properties.
     * @return a property builder to continue building the new property.
     */
    public static <T extends NestedProperties> NestedPropertyBuilder<T> newNestedProperty(@NotNull final Class<T> type,
                                                                                          @NotNull final String name) {
        return newNestedPropertyBuilder(type, name);
    }

    /**
     * This method is not supported for this class and doesn't matter because you can't instantiate this class anyway!
     *
     * @return nothing!
     */
    @NotNull
    @Override
    public Property build() {
        throw new UnsupportedOperationException();
    }
}
