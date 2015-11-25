package pluginbase.config.serializers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

class UUIDSerializer implements Serializer<UUID> {

    @Nullable
    @Override
    public Object serialize(@Nullable UUID object, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        return object != null ? object.toString() : null;
    }

    @Nullable
    @Override
    public UUID deserialize(@Nullable Object serialized, @NotNull Class wantedType, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        return serialized == null ? null : UUID.fromString(serialized.toString());
    }
}
