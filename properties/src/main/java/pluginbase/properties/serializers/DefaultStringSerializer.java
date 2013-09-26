/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.properties.serializers;

import pluginbase.logging.Logging;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * A simple serializer that uses java's built in string methods to serialize/deserialize.
 * <p/>
 * To use this serializer, the type in question <b>must</b> have a {@code public static T valueOf({@link String})} method
 * declared as part of the class.
 *
 * @param <T> the type to serialize for.
 */
public class DefaultStringSerializer<T> implements PropertySerializer<T> {

    private final Method valueOfMethod;
    private final Class<T> clazz;
    private final boolean isEnum;

    /**
     * Constructs a new string serializer for the given class.
     *
     * @param clazz this class object <b>must</b> have a {@code public static T valueOf({@link String})} method
     *              declaration.
     */
    public DefaultStringSerializer(@NotNull final Class<T> clazz) {
        this.clazz = clazz;
        try {
            valueOfMethod = clazz.getMethod("valueOf", String.class);
            valueOfMethod.setAccessible(true);
            if (!valueOfMethod.getReturnType().equals(clazz) || !Modifier.isStatic(valueOfMethod.getModifiers())) {
                throw new IllegalArgumentException(clazz.getName() + " has no static valueOf(String) method!");
            }
            isEnum = Enum.class.isAssignableFrom(clazz);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(clazz.getName() + " has no static valueOf(String) method!");
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
            return clazz.cast(valueOfMethod.invoke(null, !isEnum ? obj.toString() : obj.toString().toUpperCase()));
        } catch (IllegalAccessException e) {
            Logging.severe(this.clazz.getName() + " has no accessible static valueOf(String) method!");
        } catch (InvocationTargetException e) {
            Logging.severe(this.clazz.getName() + ".valueOf(String) is throwing an exception:");
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
