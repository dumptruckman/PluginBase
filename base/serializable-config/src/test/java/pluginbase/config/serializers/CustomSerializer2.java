package pluginbase.config.serializers;

import pluginbase.config.examples.Custom;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CustomSerializer2 implements Serializer<Custom> {

    @Nullable
    @Override
    public Object serialize(@Nullable Custom object) {
        if (object == null) {
            return null;
        } else {
            Map<String, Object> result = new HashMap<String, Object>(1);
            result.put("name", object.name);
            return result;
        }
    }

    @Nullable
    @Override
    public Custom deserialize(@Nullable Object serialized, @NotNull Class wantedType) throws IllegalArgumentException {
        if (serialized instanceof Map) {
            return new Custom(((Map) serialized).get("name").toString());
        } else {
            throw new IllegalArgumentException("serialized form must be a map");
        }
    }
}
