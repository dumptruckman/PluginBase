package com.dumptruckman.minecraft.pluginbase.config.field;

import com.dumptruckman.minecraft.pluginbase.config.ConfigSerializer;
import com.dumptruckman.minecraft.pluginbase.config.SerializationRegistrar;
import com.dumptruckman.minecraft.pluginbase.config.Serializer;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class DefaultSerializer implements Serializer<Object> {

    private static final Map<Class, Class> primitiveToWrapperMap;

    static {
        Map<Class, Class> map = new HashMap<Class, Class>();
        map.put(int.class, Integer.class);
        map.put(boolean.class, Boolean.class);
        map.put(long.class, Long.class);
        map.put(double.class, Double.class);
        map.put(float.class, Float.class);
        map.put(byte.class, Byte.class);
        map.put(short.class, Short.class);
        primitiveToWrapperMap = Collections.unmodifiableMap(map);
    }

    @Override
    public Object serialize(Object from) {
        if (SerializationRegistrar.isClassRegistered(from.getClass())) {
            return ConfigSerializer.serialize(from);
        } else {
            return String.valueOf(from);
        }
    }

    @Override
    public Object deserialize(Object serialized, @NotNull Class<Object> wantedType) throws IllegalArgumentException {
        try {
            if (String.class.isAssignableFrom(wantedType)) {
                return wantedType.cast(serialized);
            }

            if (SerializationRegistrar.isClassRegistered(wantedType)) {
                if (wantedType.isInstance(serialized)) {
                    return wantedType.cast(serialized);
                } else {
                    return wantedType.cast(ConfigSerializer.deserialize((Map<String, Object>) serialized));
                }
            } else if (Iterable.class.isAssignableFrom(wantedType) && (serialized instanceof Iterable)) {
                return wantedType.cast(serialized);
            }

            Class<?> type;
            if (primitiveToWrapperMap.containsKey(wantedType)) {
                type = primitiveToWrapperMap.get(wantedType);
            } else {
                type = wantedType;
            }

            // try valueOf
            Method valueOf = type.getMethod("valueOf", String.class);
            return valueOf.invoke(null, serialized);
        } catch (Exception e) {
            throw new RuntimeException(e);
            // TODO throw new IllegalPropertyValueException(e);
        }
    }
}
