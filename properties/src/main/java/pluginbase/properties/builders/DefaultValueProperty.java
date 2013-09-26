/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.properties.builders;

import pluginbase.messages.Message;
import pluginbase.properties.PropertyValidator;
import pluginbase.properties.ValueProperty;
import pluginbase.properties.serializers.PropertySerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

abstract class DefaultValueProperty<T> extends DefaultProperty<T> implements ValueProperty<T> {

    @Nullable
    private final PropertySerializer<T> serializer;
    @NotNull
    private final PropertyValidator<T> validator;
    @NotNull
    private final Message description;
    private final boolean deprecated;
    private final boolean defaultIfMissing;
    @NotNull
    private final List<String> aliases;

    DefaultValueProperty(@NotNull final Class<T> type, @NotNull final String path,
                         @NotNull final List<String> comments, @NotNull final List<String> aliases,
                         @Nullable final PropertySerializer<T> serializer,
                         @NotNull final PropertyValidator<T> validator,
                         @NotNull Message description, boolean deprecated, boolean defaultIfMissing) {
        super(type, path, comments);
        this.aliases = Collections.unmodifiableList(aliases);
        this.serializer = serializer;
        this.validator = validator;
        this.description = description;
        this.deprecated = deprecated;
        this.defaultIfMissing = defaultIfMissing;
    }

    @NotNull
    @Override
    public List<String> getAliases() {
        return this.aliases;
    }

    public boolean isValid(@Nullable final T obj) {
        return validator.isValid(obj);
    }

    @Override
    public boolean isDeprecated() {
        return deprecated;
    }

    @Override
    public boolean shouldDefaultIfMissing() {
        return defaultIfMissing;
    }

    @NotNull
    @Override
    public Message getDescription() {
        return description;
    }

    @Override
    public PropertySerializer<T> getDefaultSerializer() {
        return serializer;
    }
}
