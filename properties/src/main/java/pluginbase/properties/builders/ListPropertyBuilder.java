/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.properties.builders;

import pluginbase.messages.Message;
import pluginbase.properties.ListProperty;
import pluginbase.properties.PropertyValidator;
import pluginbase.properties.serializers.PropertySerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Used in the construction of a {@link ListProperty}.
 * <p/>
 * See {@link pluginbase.properties.PropertyFactory} to get started.
 *
 * @param <T> the type for the list.
 */
public class ListPropertyBuilder<T> extends ValuePropertyBuilder<T> {

    @NotNull
    private final List<T> def;
    @NotNull
    private final Class<? extends List> listClass;

    ListPropertyBuilder(@NotNull final Class<T> type, @NotNull final String name) {
        this(type, name, null, ArrayList.class);
    }

    ListPropertyBuilder(@NotNull final Class<T> type, @NotNull final String name, @NotNull final List<T> def) {
        this(type, name, def, def.getClass());
    }

    ListPropertyBuilder(@NotNull final Class<T> type, @NotNull final String name,
                        @NotNull final Class<? extends List> listClass) {
        this(type, name, null, listClass);
    }

    private ListPropertyBuilder(@NotNull final Class<T> type,
                                @NotNull final String name,
                                @SuppressWarnings("all") @Nullable final List<T> def,
                                @NotNull final Class<? extends List> listClass) {
        super(type, name, false);
        this.listClass = listClass;
        if (def == null) {
            try {
                @SuppressWarnings({"unchecked", "UnnecessaryLocalVariable"})
                final List<T> list = listClass.newInstance();
                this.def = list;
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } else {
            this.def = def;
        }
    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public ListPropertyBuilder<T> comment(@NotNull final String comment) {
        return (ListPropertyBuilder<T>) super.comment(comment);
    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public ListPropertyBuilder<T> serializer(@NotNull final PropertySerializer<T> customSerializer) {
        return (ListPropertyBuilder<T>) super.serializer(customSerializer);
    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public ListPropertyBuilder<T> validator(@NotNull final PropertyValidator<T> validator) {
        return (ListPropertyBuilder<T>) super.validator(validator);
    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public ListPropertyBuilder<T> description(@NotNull final Message message) {
        return (ListPropertyBuilder<T>) super.description(message);
    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public ListPropertyBuilder<T> deprecated() {
        return (ListPropertyBuilder<T>) super.deprecated();
    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public ListPropertyBuilder<T> alias(@NotNull final String alias) {
        return (ListPropertyBuilder<T>) super.alias(alias);
    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public ListProperty<T> build() {
        return new DefaultListProperty<T>(type, key, def, comments, new ArrayList<String>(aliases), serializer, validator, description, deprecated, defaultIfMissing, listClass);
    }
}
