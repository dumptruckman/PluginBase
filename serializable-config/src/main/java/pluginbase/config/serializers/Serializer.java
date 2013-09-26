package pluginbase.config.serializers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Implemented by helper classes that are used to serialize objects.
 * <p>
 * @param <T> The type of the object to be serialized.
 */
public interface Serializer<T> {

    @Nullable
    Object serialize(@Nullable Object object);

    @NotNull
    Map<String, Object> serializeRegisteredType(@NotNull T object);

    /**
     * Transforms the specified object to the type specified by wantedType.
     *
     * @param serialized The object to transform.
     * <p/>
     * The type of the serialized object <em>should</em> be one of the following:
     * <ul>
     *     <li>all primitives and their wrapper classes</li>
     *     <li>{@link String}</li>
     *     <li>{@link java.util.List} of primitives or {@link String}</li>
     *     <li>{@link java.util.Map} of primitives or {@link String}</li>
     *     <li>arrays of primitives or {@link String}</li>
     * </ul>
     * @param wantedType The {@link Class} of the object that should be returned.
     * @return The deserialized form of the serialized object.
     * @throws IllegalArgumentException if the serialized object's type is not deserializable.
     */
    @NotNull
    T deserialize(@NotNull Object serialized, @NotNull Class wantedType) throws IllegalArgumentException;

    T deserializeToObject(@NotNull Map data, @NotNull T object);
}
