/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties;

import java.util.List;
import java.util.Map;

public interface Properties {

    /**
     * Convenience method for saving the config to disk.
     */
    void save();

    void reload() throws Exception;

    <T> T get(SimpleProperty<T> entry) throws IllegalArgumentException;

    <T> List<T> get(ListProperty<T> entry) throws IllegalArgumentException;

    <T> Map<String, T> get(MappedProperty<T> entry) throws IllegalArgumentException;

    <T> T get(MappedProperty<T> entry, String key) throws IllegalArgumentException;

    <T> boolean set(SimpleProperty<T> entry, T value) throws IllegalArgumentException;

    <T> boolean set(ListProperty<T> entry, List<T> value) throws IllegalArgumentException;

    <T> boolean set(MappedProperty<T> entry, Map<String, T> value) throws IllegalArgumentException;

    <T> boolean set(MappedProperty<T> entry, String key, T value) throws IllegalArgumentException;
}
