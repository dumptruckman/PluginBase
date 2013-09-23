package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.config.annotation.SerializeWith;
import com.dumptruckman.minecraft.pluginbase.config.field.Field;
import com.dumptruckman.minecraft.pluginbase.config.field.FieldMap;
import com.dumptruckman.minecraft.pluginbase.config.field.FieldMapper;
import com.dumptruckman.minecraft.pluginbase.config.field.Serializer;
import com.dumptruckman.minecraft.pluginbase.config.field.Serializers;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigSerializer<T> {

    public static final String SERIALIZED_TYPE_KEY = "=$$=";

    private T object;
    Map<String, Object> serializedMap;
    private Serializer serializer = null;

    public static Map<String, Object> serialize(@NotNull Object object) {
        ConfigSerializer serializer = new ConfigSerializer(object);
        return serializer.serialize();
    }

    public static Object deserialize(@NotNull Map data) {
        Class clazz = SerializationRegistrar.getClassByAlias(getClassName(data));
        if (clazz == null) {
            throw new IllegalArgumentException("The given data is does not contain valid serialized data");
        }
        try {
            Constructor constructor = clazz.getDeclaredConstructor();
            boolean accessible = constructor.isAccessible();
            if (!accessible) {
                constructor.setAccessible(true);
            }
            ConfigSerializer serializer = new ConfigSerializer(constructor.newInstance());
            Object deserialized = serializer._deserialize(data);
            if (!accessible) {
                constructor.setAccessible(false);
            }
            return deserialized;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T deserializeToObject(@NotNull Map data, @NotNull T object) {
        Class clazz = SerializationRegistrar.getClassByAlias(getClassName(data));
        if (!object.getClass().equals(clazz)) {
            throw new IllegalArgumentException("The given data is not appropriate for " + object.getClass());
        }
        ConfigSerializer<T> serializer = new ConfigSerializer<T>(object);
        return serializer._deserialize(data);
    }

    private static String getClassName(Map data) {
        Object object = data.get(SERIALIZED_TYPE_KEY);
        if (object == null || !(object instanceof String)) {
            return null;
        }
        return object.toString();
    }

    private ConfigSerializer(T object) {
        this.object = object;
        SerializeWith serializeWith = object.getClass().getAnnotation(SerializeWith.class);
        if (serializeWith != null) {
            serializer = Serializers.getSerializer(serializeWith.value());
        }
    }

    private Map<String, Object> serialize() {
        if (serializer != null) {
            try {
                return (Map<String, Object>) serializer.serialize(object);
            } catch (ClassCastException e) {
                throw new RuntimeException("The serializer " + serializer.getClass() + " must serialize to a Map<String, Object> when annotated on a class.", e);
            }
        }
        FieldMap fieldMap = FieldMapper.getFieldMap(object.getClass());
        serializedMap = new LinkedHashMap<String, Object>(fieldMap.size() + 1);
        serializedMap.put(SERIALIZED_TYPE_KEY, SerializationRegistrar.getAlias(object.getClass()));
        for (Field field : fieldMap) {
            if (field.isPersistable()) {
                serializedMap.put(field.getName(), serializeField(field));
            }
        }
        return serializedMap;
    }

    private Object serializeField(Field field) {
        Object value = field.getValue(object);
        return field.getSerializer().serialize(value);
    }

    private T _deserialize(Map data) {
        if (serializer != null) {
            return (T) serializer.deserialize(data, object.getClass());
        }
        FieldMap fieldMap = FieldMapper.getFieldMap(object.getClass());
        for (Object key : data.keySet()) {
            if (key.equals(SERIALIZED_TYPE_KEY)) {
                continue;
            }
            Field field = fieldMap.getField(key.toString());
            if (field != null) {
                Object currentValue = field.getValue(object);
                Object newData = data.get(key);
                if (currentValue != null && !(currentValue instanceof Map) && newData instanceof Map) {
                    deserializeToObject((Map) newData, currentValue);
                } else {
                    field.setValue(object, field.getSerializer().deserialize(data.get(key), field.getType()));
                }
            } else {
                // TODO fail silently or no?
            }
        }
        return object;
    }
}
