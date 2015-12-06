package pluginbase.config.datasource.serializers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.config.serializers.Serializer;
import pluginbase.config.serializers.SerializerSet;

public class DoubleAsStringSerializer implements Serializer<Double> {

    @Nullable
    @Override
    public Object serialize(@Nullable Double object, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        return object != null ? object.toString() : null;
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public Double deserialize(@Nullable Object serialized, @NotNull Class wantedType, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        try {
            return Double.valueOf(String.valueOf(serialized));
        } catch (Exception e) {
            throw new RuntimeException("There was a problem deserializing a primitive number: " + serialized, e);
        }
    }
}
