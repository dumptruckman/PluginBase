/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties.serializers;

import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * A simple serializer that uses java's built in string methods to serialize/deserialize.
 * <p/>
 * Functions similarly to {@link DefaultStringSerializer} except relies on a public constructor that accepts a single
 * {@link String} argument.
 * <p/>
 * An example usage would be for the {@link String} class.
 *
 * @param <T> the type to serialize for.
 */
public class StringStringSerializer<T> implements PropertySerializer<T> {

    private Constructor<T> constructor;

    /**
     * Constructs a new string serializer for the given class.
     *
     * @param clazz this class object must have a {@code public T(String)} constructor declared.
     */
    public StringStringSerializer(@NotNull final Class<T> clazz) {
        try {
            constructor = clazz.getConstructor(String.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Class must be String.class!");
        }
    }

    /** {@inheritDoc} */
    @Nullable
    @Override
    public T deserialize(@Nullable final Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return constructor.newInstance(obj.toString());
        } catch (IllegalAccessException e) {
            Logging.severe("Invalid usage of StringStringSerializer!  Somehow used something other than String.class!");
        } catch (InvocationTargetException e) {
            Logging.severe("new String(String) is throwing an exception:");
            e.printStackTrace();
        } catch (InstantiationException e) {
            Logging.severe("new String(String) is throwing an exception:");
            e.printStackTrace();
        }
        throw new IllegalStateException(this.getClass().getName() + " was used illegally!  Contact dumptruckman!");
    }

    /** {@inheritDoc} */
    @Nullable
    @Override
    public Object serialize(@Nullable final T t) {
        return t == null ? null : t.toString();
    }
}
