/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.properties.serializers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A extremely basic "serializer".
 * <br>
 * Performs no actual serialization.  Simply uses the values as-is.
 *
 * @param <T> the type of the value.
 */
public class DefaultSerializer<T> implements PropertySerializer<T> {

    @NotNull
    private final Class<T> type;

    /**
     * Creates a new "serializer" for the given type.
     *
     * @param type the type to serialize.
     */
    public DefaultSerializer(@NotNull final Class<T> type) {
        this.type = type;
    }

    @NotNull
    private Class<T> getType() {
        return this.type;
    }

    /** {@inheritDoc} */
    @Nullable
    @Override
    public T deserialize(@Nullable final Object obj) {
        return getType().cast(obj);
    }

    /** {@inheritDoc} */
    @Nullable
    @Override
    public Object serialize(@Nullable final T t) {
        return t;
    }
}
