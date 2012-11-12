/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties;

import java.util.List;
import java.util.Map;

public interface Properties {

    void flush();

    void reload() throws Exception;

    <T> void addPropertyChangeListener(ValueProperty<T> property, PropertyChangeListener<T> listener);

    <T> T get(SimpleProperty<T> entry);

    <T> List<T> get(ListProperty<T> entry);

    <T> Map<String, T> get(MappedProperty<T> entry);

    <T> T get(MappedProperty<T> entry, String key);

    NestedProperties get(NestedProperty entry);

    <T> boolean set(SimpleProperty<T> entry, T value) throws PropertyValueException;

    <T> boolean set(ListProperty<T> entry, List<T> value) throws PropertyValueException;

    <T> boolean set(MappedProperty<T> entry, Map<String, T> value) throws PropertyValueException;

    <T> boolean set(MappedProperty<T> entry, String key, T value) throws PropertyValueException;
}
