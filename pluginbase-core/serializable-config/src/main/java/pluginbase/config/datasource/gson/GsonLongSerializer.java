package pluginbase.config.datasource.gson;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.config.serializers.Serializer;
import pluginbase.config.serializers.SerializerSet;

public class GsonLongSerializer implements Serializer<Long> {

    @Nullable
    @Override
    public Object serialize(@Nullable Long object, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        return object.toString();
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public Long deserialize(@Nullable Object serialized, @NotNull Class wantedType, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        try {
            return Long.valueOf(String.valueOf(serialized));
        } catch (Exception e) {
            throw new RuntimeException("There was a problem deserializing a primitive number: " + serialized, e);
        }
    }
}
