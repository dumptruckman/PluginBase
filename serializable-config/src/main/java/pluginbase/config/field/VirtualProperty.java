package pluginbase.config.field;

/**
 * Implemented by helper classes that should represent a <i>virtual</i> property.
 * {@link SerializationConfig} does <b>not</b> save virtual properties, however
 * you can use {@link pluginbase.config.serializers.Serializer}s, {@link Validator}s and all the other
 * awesome features of {@link SerializationConfig} with them.
 * @param <T>
 */
public interface VirtualProperty<T> {
    /**
     * Called to get this {@link VirtualProperty}'s value.
     * @return This {@link VirtualProperty}'s value.
     */
    T get();

    /**
     * Called to set this {@link VirtualProperty}'s value.
     * @param newValue The new value.
     */
    void set(T newValue);
}
