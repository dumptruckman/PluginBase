package pluginbase.config.serializers;

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
            Map<String, Object> result = new HashMap<String, Object>(1);
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
            List<Integer> data = ((List<Integer>)((Map) serialized).get("data"));
            custom.data.array = data.toArray(new Object[data.size()]);
            return custom;
        } else {
            throw new IllegalArgumentException("serialized form must be a map");
        }
    }
}
