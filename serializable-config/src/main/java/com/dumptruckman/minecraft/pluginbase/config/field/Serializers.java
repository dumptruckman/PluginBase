package com.dumptruckman.minecraft.pluginbase.config.field;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public enum Serializers {
    ;

    private static Map<Class<? extends Serializer>, Serializer> serializerMap = new HashMap<Class<? extends Serializer>, Serializer>();

    public static Serializer getSerializer(Class<? extends Serializer> serializerClass) {
        if (serializerMap.containsKey(serializerClass)) {
            return serializerMap.get(serializerClass);
        }
        try {
            Constructor constructor = serializerClass.getDeclaredConstructor();
            Serializer serializer = (Serializer) constructor.newInstance();
            serializerMap.put(serializerClass, serializer);
            return serializer;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
