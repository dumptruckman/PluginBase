package com.dumptruckman.minecraft.pluginbase.config;

import org.jetbrains.annotations.NotNull;

/**
 * Implemented by helper classes that are used to serialize objects.
 * <p>
 * @param <T> The type of the object to be serialized.
 */
public interface Serializer<T> {

    /**
     * Transforms the specified object of type {@code T} to a type recognized by a configuration.
     * <p/>
     * Types typically recognized by configuration include:
     * <ul>
     *     <li>all primitives and their wrapper classes</li>
     *     <li>{@link String}</li>
     *     <li>{@link java.util.List} of primitives or {@link String}</li>
     *     <li>{@link java.util.Map} of primitives or {@link String}</li>
     *     <li>arrays of primitives or {@link String}</li>
     * </ul>
     *
     * @param from the object to serialize.
     * @return the serialized form of the object.
     */
    Object serialize(T from);

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
    T deserialize(Object serialized, @NotNull Class<T> wantedType) throws IllegalArgumentException;
}
