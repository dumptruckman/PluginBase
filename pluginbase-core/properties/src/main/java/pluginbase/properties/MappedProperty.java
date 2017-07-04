/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.properties;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * A {@link ValueProperty} that represents a map of objects of some type with String keys.
 * <br>
 * See {@link pluginbase.properties.PropertyFactory} for creating properties.
 *
 * @param <T> the type of the value objects in the map.
 */
public interface MappedProperty<T> extends ValueProperty<T> {

    /**
     * Creates a new map of the type defined by {@link T}.
     *
     * @return a new map of the type defined by {@link T}.
     */
    @NotNull
    Map<String, T> getNewTypeMap();

    /** {@inheritDoc} */
    @Nullable
    Map<String, T> getDefault();
}
