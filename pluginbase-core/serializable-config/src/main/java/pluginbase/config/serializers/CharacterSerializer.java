package pluginbase.config.serializers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class CharacterSerializer implements Serializer<Character> {

    @Nullable
    @Override
    public Object serialize(@Nullable Character object, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        if (object == null) {
            return null;
        }
        return object;
    }

    @Nullable
    @Override
    public Character deserialize(@Nullable Object serialized, @NotNull Class wantedType, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        if (serialized == null) {
            return null;
        }
        String s = serialized.toString();
        if (s.isEmpty()) {
            return null;
        }
        return s.charAt(0);
    }
}
