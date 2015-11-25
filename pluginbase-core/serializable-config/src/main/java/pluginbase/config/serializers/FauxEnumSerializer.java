package pluginbase.config.serializers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

class FauxEnumSerializer implements Serializer {

    @Nullable
    @Override
    public Object serialize(@Nullable Object object, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        if (object == null) {
            return null;
        }
        try {
            Method method = object.getClass().getMethod("name");
            return method.invoke(object);
        } catch (Exception e) {
            throw new IllegalArgumentException("The class " + object.getClass() + " is annotated as a FauxEnum but is lacking the required name method.");
        }
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public Object deserialize(@Nullable Object serialized, @NotNull Class wantedType, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        if (serialized == null) {
            return null;
        }
        try {
            Method valueOfMethod = wantedType.getDeclaredMethod("valueOf", String.class);
            return valueOfMethod.invoke(null, serialized);
        } catch (Exception e) {
            throw new IllegalArgumentException("The class " + wantedType + " is annotated as a FauxEnum but is lacking the required valueOf method.");
        }
    }
}
