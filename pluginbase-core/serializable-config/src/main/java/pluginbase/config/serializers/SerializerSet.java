package pluginbase.config.serializers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.config.SerializableConfig;
import pluginbase.config.annotation.FauxEnum;
import pluginbase.config.serializers.NumberSerializer.AtomicIntegerSerializer;
import pluginbase.config.serializers.NumberSerializer.AtomicLongSerializer;
import pluginbase.config.serializers.NumberSerializer.BigNumberSerializer;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A defined set of serializers used internally to lookup the appropriate serializer for various object types.
 * <p/>
 * Custom serializer set's can be created and passed to the various {@link SerializableConfig} methods
 * in order to use custom serializers in cases where {@link pluginbase.config.annotation.SerializeWith} is not feasible.
 * <p/>
 * <strong>Note:</strong> Serializer sets are immutable once created.
 *
 * @see Serializers
 * @see SerializableConfig
 */
public final class SerializerSet {

    /**
     * Returns the default set of built in serializers used by Serializable-Config.
     * <p/>
     * The default set includes serializers for the following types:
     * <ul>
     *     <li>All primitive type wrapper classes *</li>
     *     <li>{@link BigInteger}</li>
     *     <li>{@link BigDecimal}</li>
     *     <li>{@link AtomicInteger}</li>
     *     <li>{@link AtomicLong}</li>
     *     <li>{@link String}</li>
     *     <li>{@link Locale}</li>
     *     <li>All {@link Enum} types</li>
     *     <li>All array types</li>
     *     <li>Classes annotated with {@link FauxEnum}</li>
     *     <li>Classes implementing {@link Collection}</li>
     *     <li>Classes implementing {@link Map}</li>
     * </ul>
     * * Primitive types can be converted to their wrapper type via {@link pluginbase.config.util.PrimitivesUtil#switchForWrapper(Class)}.
     * <p/>
     * All custom serializer sets start out containing a copy of the serializers found in the default set.
     *
     * @return the default set of built in serializers used by Serializable-Config.
     */
    public static SerializerSet defaultSet() {
        return DEFAULT_SET;
    }

    /**
     * Creates a Builder object to construct an immutable SerializerSet.
     * <p/>
     * By default, standard serializers include all those found in the {@link #defaultSet()} but can be replaced as
     * necessary.
     *
     * @return a Builder object to construct an immutable SerializerSet.
     */
    @NotNull
    public static Builder builder() {
        return builder(defaultSet());
    }

    /**
     * Creates a Builder object to construct an immutable SerializerSet.
     * <p/>
     * This set will begin with all serializers that exist in the given copy set.
     *
     * @param setToCopy the set of serializers to use as the base for the new serializer set.
     * @return a Builder object to construct an immutable SerializerSet.
     */
    public static Builder builder(@NotNull SerializerSet setToCopy) {
        return new Builder(setToCopy);
    }

    @NotNull
    private final Map<Class, Serializer> serializers;
    @NotNull
    private final Map<Class, Serializer> overrideSerializers;
    @NotNull
    private final Serializer fallbackSerializer;

    /**
     * A builder class used to construct an immutable SerializerSet.
     */
    public static class Builder {

        @NotNull
        private final Map<Class, Serializer> serializers = new HashMap<>();
        @NotNull
        private final Map<Class, Serializer> overrideSerializers = new HashMap<>();
        @NotNull
        private Serializer fallbackSerializer;

        private Builder(@NotNull SerializerSet setToCopy) {
            serializers.putAll(setToCopy.serializers);
            overrideSerializers.putAll(setToCopy.overrideSerializers);
            fallbackSerializer = setToCopy.fallbackSerializer;
        }

        /**
         * Specifies a standard serializer to be included in the SerializerSet built by the Builder object.
         * <p/>
         * This serializers will be used after any override serializers specified by {@link #addOverrideSerializer(Class, Serializer)}
         * and any serializer specified by {@link pluginbase.config.annotation.SerializeWith}.
         *
         * @param clazz the class that the given serializer is responsible for serializing/deserializing.
         * @param serializer the instance of the serializer to be used for the given class type.
         * @param <T> the class type that the give serializer is for.
         * @return this builder object.
         */
        @NotNull
        public <T> Builder addSerializer(@NotNull Class<T> clazz, @NotNull Serializer<T> serializer) {
            serializers.put(clazz, serializer);
            return this;
        }

        // Override serializers are very specific, and will not apply to subclasses.
        /**
         * Specifies an override serializer to be included in the SerializerSet built by the Builder object.
         * <p/>
         * If a serializer is defined here it will always be used for the given class except in the special case
         * illustrated in {@link Serializers#getClassSerializer(Class, SerializerSet)}.
         *
         * @param clazz the class that the given serializer is responsible for serializing/deserializing.
         * @param serializer the instance of the serializer to be used for the given class type.
         * @param <T> the class type that the give serializer is for.
         * @return this builder object.
         */
        @NotNull
        public <T> Builder addOverrideSerializer(@NotNull Class<T> clazz, @NotNull Serializer<T> serializer) {
            overrideSerializers.put(clazz, serializer);
            return this;
        }

        /**
         * Specifies a new fallback serializer to use instead of the default.
         * <p/>
         * This should not be used unless you're sure you know what you're doing!
         * <p/>
         * The fallback serializer will handle all serialization that is not handled by a more specific serializer.
         * It should be capable of serializing any kind of object. It should also be thread safe.
         *
         * @param fallbackSerializer the new fallback serializer to use in the SerializerSet built by this builder object.
         * @return this builder object.
         */
        public Builder setFallbackSerializer(@NotNull Serializer<Object> fallbackSerializer) {
            this.fallbackSerializer = fallbackSerializer;
            return this;
        }

        /**
         * Constructs the immutable serializer set defined by this builder's methods.
         *
         * @return the immutable serializer set defined by this builder's methods.
         */
        @NotNull
        public SerializerSet build() {
            return new SerializerSet(serializers, overrideSerializers, fallbackSerializer);
        }
    }

    private SerializerSet(@NotNull Map<Class, Serializer> serializers, @NotNull Map<Class, Serializer> overrideSerializers,
                          @NotNull Serializer fallbackSerializer) {
        this.serializers = Collections.unmodifiableMap(new HashMap<>(serializers));
        this.overrideSerializers = Collections.unmodifiableMap(new HashMap<>(overrideSerializers));
        this.fallbackSerializer = fallbackSerializer;
    }

    /**
     * Checks if this set contains a serializer for the specific given type. The fallback serializer does not count.
     *
     * @param serializableClass the class to check for the existence of a serializer for.
     * @return true if this serializer set contains a serializer for the exact type given.
     */
    public boolean hasSerializerForClass(@NotNull Class serializableClass) {
        return serializers.containsKey(serializableClass) || overrideSerializers.containsKey(serializableClass);
    }

    /**
     * Retrieves the standard serializer for the specific given type. This is the last serializer choice checked before
     * the fallback serializer is used.
     *
     * @see Serializers#getClassSerializer(Class, SerializerSet)
     *
     * @param serializableClass the class to get the serializer for.
     * @param <T> the type of the class.
     * @return the standard serializer for the given type or null if non-existent.
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public <T> Serializer<T> getSerializer(Class<T> serializableClass) {
        return serializers.get(serializableClass);
    }

    /**
     * Retrieves the override serializer for the specific given type. This is the first serializer choice for a given
     * class except in rare circumstances.
     *
     * @see Serializers#getClassSerializer(Class, SerializerSet)
     *
     * @param serializableClass the class to get the serializer for.
     * @param <T> the type of the class.
     * @return the override serializer for the given type or null if non-existent.
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public <T> Serializer<T> getOverrideSerializer(Class<T> serializableClass) {
        return overrideSerializers.get(serializableClass);
    }

    @NotNull
    public Serializer getFallbackSerializer() {
        return fallbackSerializer;
    }

    private static final SerializerSet DEFAULT_SET;

    static {
        Map<Class, Serializer> serializers = new HashMap<>();

        Serializer serializer;

        serializer = new NumberSerializer();
        serializers.put(Integer.class, serializer);
        serializers.put(Long.class, serializer);
        serializers.put(Double.class, serializer);
        serializers.put(Float.class, serializer);
        serializers.put(Byte.class, serializer);
        serializers.put(Short.class, serializer);

        serializer = new BigNumberSerializer();
        serializers.put(BigInteger.class, serializer);
        serializers.put(BigDecimal.class, serializer);

        serializer = new AtomicIntegerSerializer();
        serializers.put(AtomicInteger.class, serializer);

        serializer = new AtomicLongSerializer();
        serializers.put(AtomicLong.class, serializer);

        serializer = new BooleanSerializer();
        serializers.put(Boolean.class, serializer);

        serializer = new CharacterSerializer();
        serializers.put(Character.class, serializer);

        serializer = new StringSerializer();
        serializers.put(String.class, serializer);

        serializer = new EnumSerializer();
        serializers.put(Enum.class, serializer);

        serializer = new UUIDSerializer();
        serializers.put(UUID.class, serializer);

        serializer = new FauxEnumSerializer();
        serializers.put(FauxEnum.class, serializer);

        serializer = new CollectionSerializer();
        serializers.put(Collection.class, serializer);

        serializer = new MapSerializer();
        serializers.put(Map.class, serializer);

        serializer = new LocaleSerializer();
        serializers.put(Locale.class, serializer);

        serializer = new ArraySerializer();
        serializers.put(Array.class, serializer);

        DEFAULT_SET = new SerializerSet(serializers, new HashMap<>(), Serializers.DEFAULT_SERIALIZER);
    }
}
