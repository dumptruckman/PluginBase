/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties;

import java.util.List;
import java.util.Map;
import java.util.Observer;

public interface Properties {

    void flush();

    void reload() throws Exception;

    <T> T get(SimpleProperty<T> entry);

    <T> List<T> get(ListProperty<T> entry);

    <T> Map<String, T> get(MappedProperty<T> entry);

    <T> T get(MappedProperty<T> entry, String key);

    NestedProperties get(NestedProperty entry);

    <T> boolean set(SimpleProperty<T> entry, T value);

    <T> boolean set(ListProperty<T> entry, List<T> value);

    <T> boolean set(MappedProperty<T> entry, Map<String, T> value);

    <T> boolean set(MappedProperty<T> entry, String key, T value);

    <T> void setPropertyValidator(final ValueProperty<T> property, final PropertyValidator<T> validator);

    <T> boolean isValid(final ValueProperty<T> property, final T value);

    void addObserver(Observer observer);
}
