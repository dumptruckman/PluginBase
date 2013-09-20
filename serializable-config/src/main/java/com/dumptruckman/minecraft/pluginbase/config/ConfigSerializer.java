package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.config.field.Field;
import com.dumptruckman.minecraft.pluginbase.config.field.FieldMap;
import com.dumptruckman.minecraft.pluginbase.config.field.FieldMapper;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigSerializer<T> {

    public static final String SERIALIZED_KEY = "!!!!";

    private T object;
    Map<String, Object> serializedMap;

    public static Map<String, Object> serialize(@NotNull Object object) {
        ConfigSerializer serializer = new ConfigSerializer(object);
        return serializer.serialize();
    }

    public static Object deserialize(@NotNull Map<String, Object> data) {
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

    public static <T> T deserializeToObject(@NotNull Map<String, Object> data, @NotNull T object) {
        Class clazz = SerializationRegistrar.getClassByAlias(getClassName(data));
        if (!object.getClass().equals(clazz)) {
            throw new IllegalArgumentException("The given data is not appropriate for " + object.getClass());
        }
        ConfigSerializer<T> serializer = new ConfigSerializer<T>(object);
        return serializer._deserialize(data);
    }

    private static String getClassName(Map<String, Object> data) {
        Object object = data.get(SERIALIZED_KEY);
        if (object == null || !(object instanceof String)) {
            return null;
        }
        return object.toString();
    }

    private ConfigSerializer(T object) {
        this.object = object;
    }

    private Map<String, Object> serialize() {
        FieldMap fieldMap = FieldMapper.getFieldMap(object.getClass());
        serializedMap = new LinkedHashMap<String, Object>(fieldMap.size() + 1);
        serializedMap.put(SERIALIZED_KEY, SerializationRegistrar.getAlias(object.getClass()));
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

    private T _deserialize(Map<String, Object> data) {
        FieldMap fieldMap = FieldMapper.getFieldMap(object.getClass());
        for (String key : data.keySet()) {
            if (key.equals(SERIALIZED_KEY)) {
                continue;
            }
            Field field = fieldMap.getField(key);
            if (field != null) {
                field.setValue(object, field.getSerializer().deserialize(data.get(key), field.getType()));
            } else {
                // TODO fail silently or no?
            }
        }
        return object;
    }
}
