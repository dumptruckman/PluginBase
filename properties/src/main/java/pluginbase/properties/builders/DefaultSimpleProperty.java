/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.properties.builders;

import pluginbase.messages.Message;
import pluginbase.properties.PropertyValidator;
import pluginbase.properties.SimpleProperty;
import pluginbase.properties.serializers.PropertySerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

class DefaultSimpleProperty<T> extends DefaultValueProperty<T> implements SimpleProperty<T> {

    @Nullable
    private final T def;

    public DefaultSimpleProperty(@NotNull final Class<T> type, @NotNull final String path, @Nullable final T def,
                                 @NotNull final List<String> comments, @NotNull final List<String> aliases,
                                 @Nullable final PropertySerializer<T> serializer,
                                 @NotNull final PropertyValidator<T> validator, @NotNull final Message description,
                                 boolean deprecated, boolean defaultIfMissing) {
        super(type, path, comments, aliases, serializer, validator, description, deprecated, defaultIfMissing);
        this.def = def;
    }
    /**
     * Retrieves the default value for a config key.
     *
     * @return The default value for a config key.
     */
    @Nullable
    @Override
    public T getDefault() {
        return this.def;
    }
}
