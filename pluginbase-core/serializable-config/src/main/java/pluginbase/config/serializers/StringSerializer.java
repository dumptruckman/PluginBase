package pluginbase.config.serializers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class StringSerializer implements Serializer<String> {

    @Nullable
    @Override
    public Object serialize(@Nullable String object, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        return object;
    }

    @Nullable
    @Override
    public String deserialize(@Nullable Object serialized, @NotNull Class wantedType, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        return serialized != null ? String.valueOf(serialized) : null;
    }
}
