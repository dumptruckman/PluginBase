package com.dumptruckman.minecraft.pluginbase.properties;

import com.dumptruckman.minecraft.logging.Logging;

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

    protected AbstractProperties(Class... classes) {
        this.entries = new Entries(classes);
    }

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

    protected <T> void setPropertySerializer(final Class<T> type, final PropertySerializer<T> serializer) {
        propertySerializerMap.put(type, serializer);
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
}
