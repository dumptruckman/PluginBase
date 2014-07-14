package pluginbase.config.serializers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public enum Serializers {
    ;

    private static final Map<Class<? extends Serializer>, Serializer> SERIALIZER_MAP = new HashMap<Class<? extends Serializer>, Serializer>();

    public static <S extends Serializer> S getSerializer(Class<S> serializerClass) {
        if (SERIALIZER_MAP.containsKey(serializerClass)) {
            return (S) SERIALIZER_MAP.get(serializerClass);
        }
        try {
            Constructor<S> constructor = serializerClass.getDeclaredConstructor();
            boolean accessible = constructor.isAccessible();
            if (!accessible) {
                constructor.setAccessible(true);
            }
            S serializer = constructor.newInstance();
            registerSerializerInstance(serializerClass, serializer);
            if (!accessible) {
                constructor.setAccessible(false);
            }
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

    public static Serializer getDefaultSerializer() {
        return getSerializer(DefaultSerializer.class);
    }

    public static void setDefaultSerializer(Serializer<Object> serializer) {
        SERIALIZER_MAP.put(DefaultSerializer.class, serializer);
    }

    public static <T extends Serializer> void registerSerializerInstance(Class<T> serializerClass, T serializer) {
        SERIALIZER_MAP.put(serializerClass, serializer);
    }
}
