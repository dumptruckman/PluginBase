/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.properties;

import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class AbstractProperties implements Properties {

    protected final Entries entries;

    private final Map<Class, PropertySerializer> propertySerializerMap = new HashMap<Class, PropertySerializer>();
    private final Map<ValueProperty, PropertyValidator> propertyValidatorMap = new HashMap<ValueProperty, PropertyValidator>();

    protected AbstractProperties(Class... classes) {
        this.entries = new Entries(classes);
        registerSerializers();
    }

    protected void registerSerializers() { }

    protected boolean hasPropertySerializer(final Class type) {
        return propertySerializerMap.containsKey(type);
    }

    protected <T> PropertySerializer<T> getPropertySerializer(final Class<T> type) {
        final PropertySerializer serializer = propertySerializerMap.get(type);
        if (serializer == null) {
            Logging.severe("There is no serializer for %s!" + type);
        }
        return serializer;
    }

    public <T> boolean isValid(final ValueProperty<T> property, final T value) {
        if (propertyValidatorMap.containsKey(property)) {
            return propertyValidatorMap.get(property).isValid(value);
        }
        return property.isValid(value);
    }

    protected <T> void setPropertySerializer(final Class<T> type, final PropertySerializer<T> serializer) {
        propertySerializerMap.put(type, serializer);
    }

    public <T> void setPropertyValidator(final ValueProperty<T> property, final PropertyValidator<T> validator) {
        propertyValidatorMap.put(property, validator);
    }

    protected void changed(final ValueProperty property) {
        notifyObservers(property);
    }

    protected final boolean isInConfig(Property property) {
        return entries.properties.contains(property);
    }

    protected static final class Entries {

        protected final Set<Property> properties = new CopyOnWriteArraySet<Property>();

        private Entries(Class... configClasses) {
            final Set<Class> classes = new LinkedHashSet<Class>(10);
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
        }
    }

    private final CopyOnWriteArraySet<Observer> observers = new CopyOnWriteArraySet<Observer>();

    @Override
    public final boolean addObserver(@NotNull final Observer observer) {
        return observers.add(observer);
    }

    @Override
    public final boolean deleteObserver(@NotNull final Observer observer) {
        return observers.remove(observer);
    }

    protected final void notifyObservers(@NotNull final ValueProperty property) {
        for (final Observer observer : observers) {
            observer.update(this, property);
        }
    }
}
