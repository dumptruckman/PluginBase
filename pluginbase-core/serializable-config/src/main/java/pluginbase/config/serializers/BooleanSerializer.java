package pluginbase.config.serializers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class BooleanSerializer implements Serializer<Boolean> {

    @Nullable
    @Override
    public Object serialize(@Nullable Boolean object, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        return object;
    }

    @Nullable
    @Override
    public Boolean deserialize(@Nullable Object serialized, @NotNull Class wantedType, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        if (serialized == null) {
            return null;
        }
        return serialized instanceof Boolean ? (Boolean) serialized : Boolean.valueOf(String.valueOf(serialized));
    }
}
