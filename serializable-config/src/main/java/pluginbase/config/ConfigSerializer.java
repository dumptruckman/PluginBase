package pluginbase.config;

import pluginbase.config.annotation.SerializeWith;
import pluginbase.config.serializers.Serializer;
import pluginbase.config.serializers.Serializers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public enum ConfigSerializer {
    ;

    public static final String SERIALIZED_TYPE_KEY = "=$$=";

    @Nullable
    public static Object serialize(@NotNull Object object) {
        if (!SerializationRegistrar.isClassRegistered(object.getClass())) {
            throw new IllegalArgumentException(object.getClass() + " is not registered for serialization.");
        }
        return getSerializer(object.getClass()).serialize(object);
    }

    @Nullable
    public static Object deserialize(@Nullable Object data) {
        if (data instanceof Map) {
            Map map = (Map) data;
            Class<?> clazz = getClassFromSerializedData(map);
            if (clazz == null) {
                throw new IllegalArgumentException("The given data is not valid for type " + map.get(SERIALIZED_TYPE_KEY) + ".  Was the type registered?");
            }
            return deserializeAs(data, clazz);
        } else {
            throw new IllegalArgumentException("The given data must be a Map");
        }
    }

    @NotNull
    public static <T> T deserializeAs(@NotNull Object data, @NotNull Class<T> clazz) {
        if (!SerializationRegistrar.isClassRegistered(clazz)) {
            throw new IllegalArgumentException(clazz + " is not registered for serialization.");
        }
        return (T) getSerializer(clazz).deserialize(data, clazz);
    }

    @NotNull
    private static Serializer getSerializer(Class<?> clazz) {
        SerializeWith serializeWith = clazz.getAnnotation(SerializeWith.class);
        if (serializeWith != null) {
            return Serializers.getSerializer(serializeWith.value());
        } else {
            return Serializers.getDefaultSerializer();
        }
    }

    @Nullable
    public static Class getClassFromSerializedData(Map data) {
        Object object = data.get(SERIALIZED_TYPE_KEY);
        if (object == null || !(object instanceof String)) {
            return null;
        }
        return SerializationRegistrar.getClassByAlias(object.toString());
    }
}
