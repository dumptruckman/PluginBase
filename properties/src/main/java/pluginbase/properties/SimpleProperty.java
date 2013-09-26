/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.properties;

import org.jetbrains.annotations.Nullable;

/**
 * This represents the simplest of Properties and should represent a key : value pair where the value is not
 * a list or a map.
 * <p/>
 * This property does not store the value, merely identifies it.
 * <p/>
 * See {@link pluginbase.properties.PropertyFactory} for creating properties.
 *
 * @param <T> the type of the property.
 */
public interface SimpleProperty<T> extends ValueProperty<T> {

    /** {@inheritDoc} */
    @Nullable
    T getDefault();
}
