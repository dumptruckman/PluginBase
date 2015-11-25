package pluginbase.config.serializers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class EnumSerializer implements Serializer<Enum> {

    @Nullable
    @Override
    public Object serialize(@Nullable Enum object, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        return object != null ? object.name() : null;
    }

    @Nullable
    @Override
    public Enum deserialize(@Nullable Object serialized, @NotNull Class wantedType, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        return serialized == null ? null : Enum.valueOf(wantedType, serialized.toString());
    }
}
