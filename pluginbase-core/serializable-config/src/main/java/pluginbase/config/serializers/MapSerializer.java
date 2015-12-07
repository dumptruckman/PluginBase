package pluginbase.config.serializers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.config.SerializableConfig;

import java.util.LinkedHashMap;
import java.util.Map;

class MapSerializer implements Serializer<Map<?, ?>> {

    @Nullable
    @Override
    public Object serialize(@Nullable Map<?, ?> object, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        if (object == null) {
            return null;
        }
        Map<String, Object> result = new LinkedHashMap<>(object.size());
        for (Map.Entry entry : object.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (key != null && value != null) {
                result.put(key.toString(), SerializableConfig.serialize(entry.getValue(), serializerSet));
            }
        }
        return result;
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public Map<?, ?> deserialize(@Nullable Object serialized, @NotNull Class wantedType, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        if (serialized == null) {
            return null;
        }
        if (!(serialized instanceof Map)) {
            throw new IllegalArgumentException("Serialized value must be a map to be deserialized as a map");
        }
        Map<?, ?> data = (Map<?, ?>) serialized;
        Map map = createMap(wantedType, data.size());
        for (Map.Entry entry : data.entrySet()) {
            map.put(SerializableConfig.deserialize(entry.getKey(), serializerSet), SerializableConfig.deserialize(entry.getValue(), serializerSet));
        }
        return map;
    }

    @NotNull
    protected static Map createMap(@NotNull Class<? extends Map> wantedType, int size) {
        if (wantedType.isInterface()) {
            return new LinkedHashMap(size);
        } else {
            try {
                Object[] paramValues = new Object[] { size };
                return InstanceUtil.createInstance(wantedType, InstanceUtil.SIZE_PARAM_TYPE_ARRAY, paramValues);
            } catch (RuntimeException e) {
                if (e.getCause() instanceof NoSuchMethodException) {
                    try {
                        return InstanceUtil.createInstance(wantedType);
                    } catch (RuntimeException e1) {
                        if (e1.getCause() instanceof NoSuchMethodException) {
                            return new LinkedHashMap(size);
                        } else {
                            throw e;
                        }
                    }
                } else {
                    throw e;
                }
            }
        }
    }
}
