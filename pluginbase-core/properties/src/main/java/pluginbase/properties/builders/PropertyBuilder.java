/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.properties.builders;

import pluginbase.properties.NestedProperties;
import pluginbase.properties.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Used to construct new {@link Property} objects.
 * <br>
 * An implementation of this will be returned by the {@link pluginbase.properties.PropertyFactory}.
 * Look there for all your Property building needs!
 *
 * @param <T> The type of the property.
 */
public abstract class PropertyBuilder<T> {

    /** The key of the property.  Used for identification. */
    @NotNull
    protected final String key;
    /** The type of the property. */
    @NotNull
    protected final Class<T> type;
    /** Comments for the property. */
    @NotNull
    protected final List<String> comments = new ArrayList<String>();

    /** Do not use this constructor!  It is only for a special internal inheritance case. */
    protected PropertyBuilder() {
        throw new AssertionError();
    }

    /**
     * Constructs a new PropertyBuilder with the given type and name (key).
     *
     * @param type the type of the property.
     * @param name the key for the property. Used for identification.
     */
    protected PropertyBuilder(@NotNull final Class<T> type, @NotNull final String name) {
        this.key = name;
        this.type = type;
    }

    /**
     * Adds a comment to the comments of this property.
     * <br>
     * This can be called multiple times.  Each time generally adds a comment on a new line though this ultimately
     * depends on the implementation of {@link pluginbase.properties.Properties} for actual
     * usage.
     *
     * @param comment the comment to add.
     * @return this PropertyBuilder for method chaining.
     */
    @NotNull
    public PropertyBuilder<T> comment(@NotNull final String comment) {
        comments.add(comment);
        return this;
    }

    /**
     * Finalizes the settings declared in this PropertyBuilder and creates the {@link Property} object.
     *
     * @return a custom built Property object.
     */
    @NotNull
    public abstract Property<T> build();

    @NotNull
    protected static NullPropertyBuilder newNullPropertyBuilder(@NotNull final String name) {
        return new NullPropertyBuilder(name);
    }

    @NotNull
    protected static <T> SimplePropertyBuilder<T> newSimplePropertyBuilder(@NotNull final Class<T> type,
                                                                           @NotNull final String name,
                                                                           @Nullable final T def) {
        return new SimplePropertyBuilder<T>(type, name, def);
    }

    @NotNull
    protected static <T> ListPropertyBuilder<T> newListPropertyBuilder(@NotNull final Class<T> type,
                                                                       @NotNull final String name) {
        return new ListPropertyBuilder<T>(type, name);
    }

    @NotNull
    protected static <T> ListPropertyBuilder<T> newListPropertyBuilder(@NotNull final Class<T> type,
                                                                       @NotNull final String name,
                                                                       @NotNull final List<T> def) {
        return new ListPropertyBuilder<T>(type, name, def);
    }

    @NotNull
    protected static <T> ListPropertyBuilder<T> newListPropertyBuilder(@NotNull final Class<T> type,
                                                                       @NotNull final String name,
                                                                       @NotNull final Class<? extends List> listType) {
        return new ListPropertyBuilder<T>(type, name, listType);
    }

    @NotNull
    protected static <T> MappedPropertyBuilder<T> newMappedPropertyBuilder(@NotNull final Class<T> type,
                                                                           @NotNull final String name) {
        return new MappedPropertyBuilder<T>(type, name);
    }

    @NotNull
    protected static <T> MappedPropertyBuilder<T> newMappedPropertyBuilder(@NotNull final Class<T> type,
                                                                           @NotNull final String name,
                                                                           @NotNull final Class<? extends Map> mapType) {
        return new MappedPropertyBuilder<T>(type, name, mapType);
    }

    @NotNull
    protected static <T> MappedPropertyBuilder<T> newMappedPropertyBuilder(@NotNull final Class<T> type,
                                                                           @NotNull final String name,
                                                                           @NotNull final Map<String, T> def) {
        return new MappedPropertyBuilder<T>(type, name, def);
    }

    @NotNull
    protected static <T extends NestedProperties> NestedPropertyBuilder<T> newNestedPropertyBuilder(@NotNull final Class<T> type,
                                                                                                    @NotNull final String name) {
        return new NestedPropertyBuilder<T>(type, name);
    }
}
