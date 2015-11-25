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

    /**
     * Transforms the specified object of type {@code T} to a type recognized by a serialization format.
     * <p/>
     * The most common format for serialization is YAML.  Types typically recognized by YAML include:
     * <ul>
     *     <li>all primitives and their wrapper classes</li>
     *     <li>{@link String}</li>
     *     <li>{@link java.util.List} of primitives or {@link String}</li>
     *     <li>{@link java.util.Map} of primitives or {@link String}</li>
     *     <li>arrays of primitives or {@link String}</li>
     * </ul>
     *
     * @param object the object to serialize.
     * @param serializerSet the serializerSet that contains serializers that may need to be used to serialize child objects.
     * @return the serialized form of the object.
     * @throws IllegalArgumentException if the deserialized object's type is not serializable.
     */
    @Nullable
    Object serialize(@Nullable T object, @NotNull SerializerSet serializerSet) throws IllegalArgumentException;

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
     * However, it is possible that serialization could take other forms.<p/>
     * @param wantedType The {@link Class} of the object that should be returned.
     * @param serializerSet the serializerSet that contains serializers that may need to be used to deserialize child objects.
     * @return The deserialized form of the serialized object.
     * @throws IllegalArgumentException if the serialized object's type is not deserializable.
     */
    @Nullable
    T deserialize(@Nullable Object serialized, @NotNull Class wantedType, @NotNull SerializerSet serializerSet) throws IllegalArgumentException;
}
