package pluginbase.config.serializers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

class NumberSerializer<N extends Number> implements Serializer<N> {

    @Nullable
    @Override
    public Object serialize(@Nullable N object, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        return object;
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public N deserialize(@Nullable Object serialized, @NotNull Class wantedType, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        try {
            Method valueOf = wantedType.getMethod("valueOf", String.class);
            return (N) valueOf.invoke(null, String.valueOf(serialized));
        } catch (Exception e) {
            throw new RuntimeException("There was a problem deserializing a primitive value.", e);
        }
    }
}
