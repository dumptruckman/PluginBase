/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties;

import com.dumptruckman.minecraft.pluginbase.properties.serializers.PropertySerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * A skeleton implementation of Properties to take care of several of the minor aspects of Properties as well as give
 * additional options for the implementer.
 * <p/>
 *
 */
public abstract class AbstractProperties implements Properties {

    private final Entries entries;

    private final Map<Class, PropertySerializer> propertySerializerMap = new HashMap<Class, PropertySerializer>();
    private final Map<ValueProperty, PropertyValidator> propertyValidatorMap = new HashMap<ValueProperty, PropertyValidator>();

    /**
     * Constructs a new AbstractProperties using the classes to indicate what {@link Property} objects this properties
     * deals with.
     *
     * @param classes a class containing definitions of {@link Property} objects that are available for setting/getting
     *                in this properties object.
     */
    protected AbstractProperties(@NotNull final Class... classes) {
        this.entries = new Entries(classes);
        registerSerializers();
    }

    /**
     * Any property serializers specific to this Properties object should be registered within this method.
     * <p/>
     * <b>If serialization will occur with this properties object, you MUST register serializers for each class type.</b>
     * <br/>
     * It is possible the default serializer {@link com.dumptruckman.minecraft.pluginbase.properties.ValueProperty#getDefaultSerializer()}
     * will be sufficient for your needs but you must register it with {@link #setPropertySerializer(Class, com.dumptruckman.minecraft.pluginbase.properties.serializers.PropertySerializer)}.
     * <p/>
     * Use {@link #setPropertySerializer(Class, com.dumptruckman.minecraft.pluginbase.properties.serializers.PropertySerializer)}
     * to register a serializer.
     */
    protected void registerSerializers() { }

    /**
     * Checks to see if there is a property serializer for the given class type.
     *
     * @param type the class type to check for registered serializers for.
     * @return true if a serializer has been registered for the given class type.
     */
    protected boolean hasPropertySerializer(@NotNull final Class type) {
        return propertySerializerMap.containsKey(type);
    }

    /**
     * Gets the property serializer for the given class type.
     *
     * @param type the class type to get the serializer for.
     * @param <T> the type.
     * @return the property serializer for the given class type.
     */
    @NotNull
    protected <T> PropertySerializer<T> getPropertySerializer(@NotNull final Class<T> type) {
        final PropertySerializer serializer = propertySerializerMap.get(type);
        if (serializer == null) {
            throw new IllegalStateException("There is no serializer for " + type + "!");
        }
        return serializer;
    }

    /** {@inheritDoc} */
    @Override
    public <T> boolean isValid(@NotNull final ValueProperty<T> property, @Nullable final T value) {
        if (propertyValidatorMap.containsKey(property)) {
            return propertyValidatorMap.get(property).isValid(value);
        }
        return property.isValid(value);
    }

    /**
     * Registers the given property serializer for the given class type.
     * <p/>
     * Any time the type is serialized or deserialized it should use this property serializer to handle those operations.
     * This should be managed by the specific implementations of the Properties.
     *
     * @param type the class type to set the serializer for.
     * @param serializer the property serializer to use for the given class type.
     * @param <T> the type.
     */
    protected <T> void setPropertySerializer(@NotNull final Class<T> type, @NotNull final PropertySerializer<T> serializer) {
        propertySerializerMap.put(type, serializer);
    }

    /** {@inheritDoc} */
    @Override
    public <T> void setPropertyValidator(@NotNull final ValueProperty<T> property, @NotNull final PropertyValidator<T> validator) {
        propertyValidatorMap.put(property, validator);
    }

    /**
     * Implementers should call this when the value of a property is changed in order to trigger notifications to property
     * observers.
     *
     * @param property the property whose value has changed.
     */
    protected final void changed(@NotNull final ValueProperty property) {
        notifyObservers(property);
    }

    /**
     * Determines if the given property is considered to be part of this set of properties.
     *
     * @param property the property to check.
     * @return true if this properties objects cares about the given property.
     */
    protected final boolean isInConfig(@NotNull final Property property) {
        return getProperties().contains(property);
    }

    /**
     * Retrieves the unmodifiable set of properties this properties objects cares about.
     *
     * @return the unmodifiable set of properties this properties objects cares about.
     */
    @NotNull
    protected final Set<Property> getProperties() {
        return entries.properties;
    }

    private static final class Entries {

        private final Set<Property> properties;

        private Entries(@NotNull final Class... configClasses) {
            final Set<Class> classes = new LinkedHashSet<Class>(configClasses.length * 2);
            final Set<Property> properties = new LinkedHashSet<Property>();
            for (Class configClass : configClasses) {
                classes.add(configClass);
                classes.addAll(Arrays.asList(configClass.getInterfaces()));
                if (configClass.getSuperclass() != null) {
                    classes.add(configClass.getSuperclass());
                }
            }
            for (Class clazz : classes) {
                final Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    if (!Modifier.isStatic(field.getModifiers())) {
                        continue;
                    }
                    field.setAccessible(true);
                    try {
                        if (Property.class.isInstance(field.get(null))) {
                            try {
                                properties.add((Property) field.get(null));
                            } catch(IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (IllegalArgumentException ignore) {
                    } catch (IllegalAccessException ignore) {
                    } catch (NullPointerException ignore) { }
                }
            }
            this.properties = Collections.unmodifiableSet(properties);
        }
    }

    private final Set<Observer> observers = new CopyOnWriteArraySet<Observer>();

    /** {@inheritDoc} */
    @Override
    public final boolean addObserver(@NotNull final Observer observer) {
        return observers.add(observer);
    }

    /** {@inheritDoc} */
    @Override
    public final boolean deleteObserver(@NotNull final Observer observer) {
        return observers.remove(observer);
    }

    private final void notifyObservers(@NotNull final ValueProperty property) {
        for (final Observer observer : observers) {
            observer.update(this, property);
        }
    }
}
