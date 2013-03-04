/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties.serializers;

import org.jetbrains.annotations.Nullable;

/**
 * Used to serialize and deserialize property values.
 *
 * @param <T> the type of value this deserializes to and serializes from.
 */
public interface PropertySerializer<T> {

    /**
     * Takes the object o and deserializes it to type {@link T}.
     * <p/>
     * The Object form could be anything but should typically be known by the implementer of the PropertySerializer and
     * the {@link com.dumptruckman.minecraft.pluginbase.properties.Properties}.
     * <p/>
     * A simple example is taking the string form of o and working from there.
     *
     * @param o the serialized value.
     * @return the deserialized value.
     */
    @Nullable
    T deserialize(@Nullable final Object o);

    /**
     * Takes the object t and serializes it to Object form.
     * <p/>
     * The serialized form can be anything but needs to work with the {@link com.dumptruckman.minecraft.pluginbase.properties.Properties}
     * that it is use with.
     * <p/>
     * A simple example is turning t into a string and returning that.
     *
     * @param t the deserialized form.
     * @return the serialized form.
     */
    @Nullable
    Object serialize(@Nullable final T t);
}
