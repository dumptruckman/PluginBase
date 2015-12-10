package pluginbase.config.serializers;

import pluginbase.config.SerializableConfig;
import pluginbase.config.examples.Custom;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomSerializer implements Serializer<Custom> {

    @Nullable
    @Override
    public Object serialize(@Nullable Custom object, @NotNull SerializerSet serializerSet) {
        if (object == null) {
            return null;
        } else {
            Map<String, Object> result = new HashMap<>(3);
            result.put(SerializableConfig.SERIALIZED_TYPE_KEY, Custom.class.getName());
            result.put("name", object.name);
            result.put("data", Arrays.asList(object.data.array));
            return result;
        }
    }

    @Nullable
    @Override
    public Custom deserialize(@Nullable Object serialized, @NotNull Class wantedType, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        if (serialized instanceof Map) {
            Custom custom = new Custom(((Map) serialized).get("name").toString());
            List<?> data = ((List<?>)((Map) serialized).get("data"));
            custom.data.array = data.toArray(new Object[data.size()]);
            for (int i = 0; i < data.size(); i++) {
                custom.data.array[i] = Integer.valueOf(String.valueOf(data.get(i)));
            }
            return custom;
        } else {
            throw new IllegalArgumentException("serialized form must be a map");
        }
    }
}
