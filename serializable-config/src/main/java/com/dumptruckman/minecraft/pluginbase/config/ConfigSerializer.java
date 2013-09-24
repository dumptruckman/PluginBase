package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.config.annotation.SerializeWith;
import com.dumptruckman.minecraft.pluginbase.config.serializers.Serializer;
import com.dumptruckman.minecraft.pluginbase.config.serializers.Serializers;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public enum ConfigSerializer {
    ;

    public static final String SERIALIZED_TYPE_KEY = "=$$=";

    public static Map<String, Object> serialize(@NotNull Object object) {
        if (!SerializationRegistrar.isClassRegistered(object.getClass())) {
            throw new IllegalArgumentException(object.getClass() + " is not registered for serialization.");
        }
        return getSerializer(object.getClass()).serializeRegisteredType(object);
    }

    public static Object deserialize(@NotNull Map data) {
        Class<?> clazz = getClassFromSerializedData(data);
        if (clazz == null) {
            throw new IllegalArgumentException("The given data is not valid for type " + data.get(SERIALIZED_TYPE_KEY));
        }
        return getSerializer(clazz).deserialize(data, clazz);
    }

    public static Object deserializeToObject(@NotNull Map data, @NotNull Object object) {
        Class<?> clazz = object.getClass();
        if (!SerializationRegistrar.isClassRegistered(clazz)) {
            throw new IllegalArgumentException(object.getClass() + " is not registered for serialization.");
        }
        return getSerializer(clazz).deserializeToObject(data, object);
    }

    private static Serializer getSerializer(Class<?> clazz) {
        SerializeWith serializeWith = clazz.getAnnotation(SerializeWith.class);
        if (serializeWith != null) {
            return Serializers.getSerializer(serializeWith.value());
        } else {
            return Serializers.getDefaultSerializer();
        }
    }

    public static Class getClassFromSerializedData(Map data) {
        Object object = data.get(SERIALIZED_TYPE_KEY);
        if (object == null || !(object instanceof String)) {
            return null;
        }
        return SerializationRegistrar.getClassByAlias(object.toString());
    }
}
