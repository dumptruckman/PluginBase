/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.properties.builders;

import pluginbase.messages.Message;
import pluginbase.messages.Messages;
import pluginbase.properties.PropertyValidator;
import pluginbase.properties.ValueProperty;
import pluginbase.properties.serializers.PropertySerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.Set;

abstract class ValuePropertyBuilder<T> extends PropertyBuilder<T> {

    /** Whether the property should supply a default value if no value present. */
    protected final boolean defaultIfMissing;
    /** The set of aliases for a property. */
    @NotNull
    protected final Set<String> aliases = new LinkedHashSet<String>();

    /** The property serializer. */
    @Nullable
    protected PropertySerializer<T> serializer = null;
    /** The property validator. */
    @NotNull
    protected PropertyValidator<T> validator = new DefaultValidator<T>();
    /** The property description. */
    @NotNull
    protected Message description = Messages.BLANK;
    /** Whether or not the property is deprecated */
    protected boolean deprecated = false;

    public ValuePropertyBuilder(@NotNull final Class<T> type, @NotNull final String name, boolean allowNull) {
        super(type, name);
        this.defaultIfMissing = !allowNull;
    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public ValuePropertyBuilder<T> comment(@NotNull final String comment) {
        return (ValuePropertyBuilder<T>) super.comment(comment);
    }

    /**
     * Sets the default serializer for this property.
     * <br>
     * This is what will be used by default if no serializer is defined for the Property in the
     * {@link pluginbase.properties.Properties} object.
     * <br>
     * If no serializer defined in either place, here or the Properties object, the property will be the raw value and
     * not be serialized.
     *
     * @param customSerializer the serializer to use when none defined in the Properties object.
     * @return this PropertyBuilder for method chaining.
     */
    @NotNull
    public ValuePropertyBuilder<T> serializer(@NotNull final PropertySerializer<T> customSerializer) {
        serializer = customSerializer;
        return this;
    }

    /**
     * Sets the default validator for this property.
     * <br>
     * This is what will be used by default if no validator is defined for the Property in the
     * {@link pluginbase.properties.Properties} object.
     * <br>
     * If no serializer defined in either place, here or the Properties object, a default validator will be used
     * which always validates true.
     *
     * @param validator the validator to use when none defined in the Properties object.
     * @return this PropertyBuilder for method chaining.
     */
    @NotNull
    public ValuePropertyBuilder<T> validator(@NotNull final PropertyValidator<T> validator) {
        this.validator = validator;
        return this;
    }

    /**
     * Sets the description for this property.
     *
     * @param message the description for this property.
     * @return this PropertyBuilder for method chaining.
     */
    @NotNull
    public ValuePropertyBuilder<T> description(@NotNull final Message message) {
        description = message;
        return this;
    }

    /**
     * Marks this property as deprecated.
     * <br>
     * Typically when you use this you will also want to use java's {@link Deprecated} on the field that the property
     * this builds is stored in.
     * <br>
     * This will indicate to the Properties object that some special handle may need to occur since this Property
     * is deprecated.
     *
     * @return this PropertyBuilder for method chaining.
     */
    @NotNull
    public ValuePropertyBuilder<T> deprecated() {
        deprecated = true;
        return this;
    }

    /**
     * Adds an alias to the Property.
     * <br>
     * These are alternate names that can be used to refer to the Property.  What this means is up to the implementer
     * of the Property.
     * <br>
     * Call this multiple times to add multiple aliases.
     *
     * @param alias an alias to add to the property.
     * @return this PropertyBuilder for method chaining.
     */
    @NotNull
    public ValuePropertyBuilder<T> alias(@NotNull final String alias) {
        this.aliases.add(alias);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    @NotNull
    public abstract ValueProperty<T> build();
}
