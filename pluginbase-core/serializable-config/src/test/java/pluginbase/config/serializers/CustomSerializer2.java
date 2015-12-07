package pluginbase.config.serializers;

import pluginbase.config.SerializableConfig;
import pluginbase.config.examples.Custom;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.config.examples.Custom.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomSerializer2 implements Serializer<Custom> {

    @Nullable
    @Override
    public Object serialize(@Nullable Custom object, @NotNull SerializerSet serializerSet) {
        if (object == null) {
            return null;
        } else {
            Map<String, Object> result = new HashMap<String, Object>(1);
            result.put("name", object.name.toUpperCase());
            result.put("data", SerializableConfig.serialize(object.data));
            return result;
        }
    }

    @Nullable
    @Override
    public Custom deserialize(@Nullable Object serialized, @NotNull Class wantedType, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        if (serialized instanceof Map) {
            Custom custom = new Custom(((Map) serialized).get("name").toString().toLowerCase());
            List<?> data = ((List<?>)((Map)((Map) serialized).get("data")).get("array"));
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
