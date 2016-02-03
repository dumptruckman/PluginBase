package pluginbase.config.serializers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.config.SerializableConfig;
import pluginbase.config.annotation.FauxEnum;
import pluginbase.config.annotation.SerializeWith;
import pluginbase.config.serializers.NumberSerializer.AtomicIntegerSerializer;
import pluginbase.config.serializers.NumberSerializer.AtomicLongSerializer;
import pluginbase.config.serializers.NumberSerializer.BigNumberSerializer;
import pluginbase.config.util.PrimitivesUtil;
import pluginbase.logging.Logging;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A defined set of serializers used internally to lookup the appropriate serializer for various object types.
 * <p/>
 * Custom serializer set's can be created and passed to the various {@link SerializableConfig} methods
 * in order to use custom serializers in cases where {@link pluginbase.config.annotation.SerializeWith} is not feasible.
 * <p/>
 * <strong>Note:</strong> Serializer sets are immutable once created.
 * <p/>
 * Serializers have a general order of priority in which they should be used. See {@link #getClassSerializer(Class)}
 * for details.
 *
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
    private final Map<Class, Supplier<Serializer>> serializers;
    @NotNull
    private final Map<Class, Supplier<Serializer>> overrideSerializers;
    @NotNull
    private final Supplier<Serializer> fallbackSerializer;
    @NotNull
    private final Map<Class<? extends Serializer>, Supplier<Serializer>> serializeWithSerializers;
    @NotNull
    private final Map<Predicate<Class<?>>, Class> classReplacements;

    /**
     * A builder class used to construct an immutable SerializerSet.
     */
    public static class Builder {

        @NotNull
        private final Map<Class, Supplier<Serializer>> serializers = new HashMap<>();
        @NotNull
        private final Map<Class, Supplier<Serializer>> overrideSerializers = new HashMap<>();
        @NotNull
        private Supplier<Serializer> fallbackSerializer;
        @NotNull
        private final Map<Class<? extends Serializer>, Supplier<Serializer>> serializeWithSerializers;
        @NotNull
        private final Map<Predicate<Class<?>>, Class> classReplacements;

        private Builder(@NotNull SerializerSet setToCopy) {
            serializers.putAll(setToCopy.serializers);
            overrideSerializers.putAll(setToCopy.overrideSerializers);
            fallbackSerializer = setToCopy.fallbackSerializer;
            serializeWithSerializers = new HashMap<>(setToCopy.serializeWithSerializers);
            classReplacements = new LinkedHashMap<>(setToCopy.classReplacements);
        }

        /**
         * Specifies a standard serializer to be included in the SerializerSet built by the Builder object.
         * <p/>
         * This serializers will be used after any override serializers specified by {@link #addOverrideSerializer(Class, Supplier)}
         * and any serializer specified by {@link pluginbase.config.annotation.SerializeWith}.
         *
         * @param clazz the class that the given serializer is responsible for serializing/deserializing.
         * @param serializer A supplier that provides the instance of the serializer to be used for the given class type.
         * @param <T> the class type that the give serializer is for.
         * @return this builder object.
         */
        @NotNull
        public <T> Builder addSerializer(@NotNull Class<T> clazz, @NotNull Supplier<Serializer<T>> serializer) {
            serializers.put(clazz, serializer::get);
            return this;
        }

        // Override serializers are very specific, and will not apply to subclasses.
        /**
         * Specifies an override serializer to be included in the SerializerSet built by the Builder object.
         * <p/>
         * If a serializer is defined here it will always be used for the given class except in the special case
         * illustrated in {@link #getClassSerializer(Class)}.
         *
         * @param clazz the class that the given serializer is responsible for serializing/deserializing.
         * @param serializer A supplier that provides the instance of the serializer to be used for the given class type.
         * @param <T> the class type that the give serializer is for.
         * @return this builder object.
         */
        @NotNull
        public <T> Builder addOverrideSerializer(@NotNull Class<T> clazz, @NotNull Supplier<Serializer<T>> serializer) {
            overrideSerializers.put(clazz, serializer::get);
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
         * @param fallbackSerializer A supplier that provides the new fallback serializer to use in the SerializerSet
         *                           built by this builder object.
         * @return this builder object.
         */
        public Builder setFallbackSerializer(@NotNull Supplier<Serializer<Object>> fallbackSerializer) {
            this.fallbackSerializer = fallbackSerializer::get;
            return this;
        }

        /**
         * Registers an instance of a given Serializer class for use with {@link SerializeWith}.
         * <p/>
         * For a class to be serialized by the serializer specified in {@link SerializeWith} the serializer must have a
         * registered instance. If the serializer has a 0-arg constructor, an instance will be created automatically.
         * This method allows for the registering of serializer instances for serializers without 0-arg constructors
         * and also to replace previous instances should that be needed.
         * <p/>
         * If the given serializer class has already been registered this method will <strong>replace</strong> the previous
         * instance with the given instance.
         *
         * @param serializerClass the serializer class to register an instance of.
         * @param serializer A supplier that provides the instance of the serializer class to use.
         * @param <T> the serializer class type.
         * @return this builder object.
         */
        public <T extends Serializer> Builder registerSerializeWithInstance(@NotNull Class<T> serializerClass, @NotNull Supplier<T> serializer) {
            serializeWithSerializers.put(serializerClass, serializer::get);
            return this;
        }

        /**
         * Registers a {@link Predicate} that can be used to check if a class about to be serialized should be treated as the
         * given replacement class for serialization purposes.
         * <p/>
         * A very common example for this is serializing all classes that extend a certain class. This can be done
         * like so <code>registerClassReplacement(SomeClass.class::isAssignableFrom, SomeClass.class);</code>
         * <p/>
         * It is recommended that the overriden {@link Predicate#test(Object)} method be as fast as possible sine it
         * will potentially be checked very frequently.
         * <p/>
         * Predicates will be checked in the order they are registered and the first one that matches will be used.
         * There are a few default predicates that are registered in the following order:
         * <ol>
         *     <li>@FauxEnum annotation present (replaces with FauxEnum.class)</li>
         *     <li>Instance of Collection (replaces with Collection.class)</li>
         *     <li>Instance of Map (replaces with Map.class)</li>
         *     <li>Instance of Enum (replaces with Enum.class)</li>
         *     <li>An array (replaces with Array.class)</li>
         * </ol>
         *
         * @param checker the Predicate used to check if a given class should be treated like the replacementClass for
         *                serialization purposes.
         * @param replacementClass the replacement class to use when the checker returns true.
         * @return this builder object.
         */
        public Builder registerClassReplacement(@NotNull Predicate<Class<?>> checker, @NotNull Class replacementClass) {
            classReplacements.put(checker, replacementClass);
            return this;
        }

        /**
         * Unregisters all {@link Predicate}s that will cause a replacement with the given replacementClass.
         * <p/>
         * This may be useful if the order of checks needs to be altered or one of the default predicates needs to be
         * replaced.
         *
         * @param replacementClass The replacement class to unregister all predicates for.
         * @return this builder object.
         */
        public Builder unregisterClassReplacement(@NotNull Class replacementClass) {
            Iterator<Entry<Predicate<Class<?>>, Class>> replacementsIterator = classReplacements.entrySet().iterator();
            while (replacementsIterator.hasNext()) {
                if (replacementsIterator.next().getValue().equals(replacementClass)) {
                    replacementsIterator.remove();
                }
            }
            return this;
        }

        /**
         * Constructs the immutable serializer set defined by this builder's methods.
         *
         * @return the immutable serializer set defined by this builder's methods.
         */
        @NotNull
        public SerializerSet build() {
            return new SerializerSet(serializers, overrideSerializers, fallbackSerializer, serializeWithSerializers, classReplacements);
        }
    }

    private SerializerSet(@NotNull Map<Class, Supplier<Serializer>> serializers, @NotNull Map<Class, Supplier<Serializer>> overrideSerializers,
                          @NotNull Supplier<Serializer> fallbackSerializer, @NotNull Map<Class<? extends Serializer>, Supplier<Serializer>> serializeWithSerializers,
                          @NotNull Map<Predicate<Class<?>>, Class> classReplacements) {
        this.serializers = Collections.unmodifiableMap(new HashMap<>(serializers));
        this.overrideSerializers = Collections.unmodifiableMap(new HashMap<>(overrideSerializers));
        this.fallbackSerializer = fallbackSerializer;
        this.serializeWithSerializers = new ConcurrentHashMap<>(serializeWithSerializers);
        this.classReplacements = Collections.unmodifiableMap(new LinkedHashMap<>(classReplacements));
    }

    /**
     * Retrieves the most appropriate serializer instance capable of serializing/deserializing the given class.
     * <p/>
     * This is the primary method for obtaining a serializer instance to serialize a given class in nearly all
     * scenarios. It will ensure that the most appropriate serializer is obtained.
     * <p/>
     * Special treatment is given to classes that extend {@link Collection} and {@link Map} and classes annotated by
     * {@link FauxEnum}. When looking for a <em>non-override</em> serializer for these classes, if one is not found for
     * the specific class, the serializer for Collection, Map, or FauxEnum, respectively, will be used instead. For
     * other types, serializers are only returned for the exact type given.
     * <p/>
     * What serializer is returned is determined by the following priority order:
     * <ol>
     *     <li>An override serializer specified in the given SerializerSet</li>
     *     <li>The serializer specified by @SerializeWith</li>
     *     <li>A standard serializer specified in the given SerializerSet</li>
     *     <li>The serializer set's fallback serializer</li>
     * </ol>
     * <strong>Note:</strong> An exception to this order is the case of a field (rather than a type) annotated by
     * {@link SerializeWith}. The <em>default</em> fallback serializer will prefer the annotated field's
     * {@link SerializeWith} serializer to all others. This may or may not hold true if a custom serializer is used on
     * the object containing the field or the serializer set has a custom fallback serializer.
     *
     * @param clazz the class to get a serializer for.
     * @return the most appropriate serializer for the given class.
     */
    @NotNull
    public <T> Serializer<T> getClassSerializer(@NotNull Class<T> clazz) {
        clazz = PrimitivesUtil.switchForWrapper(clazz);

        Serializer<T> serializer = getOverrideSerializer(clazz);
        if (serializer != null) {
            return serializer;
        }

        SerializeWith serializeWith = clazz.getAnnotation(SerializeWith.class);
        if (serializeWith != null) {
            try {
                return getSerializerInstance(serializeWith.value());
            } catch (Exception e) {
                Logging.warning("Class %s is annotated with SerializeWith and specified the serializer class %s but "
                                + "could not obtain an instance of that serializer. Consider registering the serializer "
                                + "manually.",
                        clazz, serializeWith.value());
            }
        }

        serializer = getSerializer(clazz);
        if (serializer != null) {
            return serializer;
        }

        for (Entry<Predicate<Class<?>>, Class> entry : classReplacements.entrySet()) {
            if (entry.getKey().test(clazz)) {
                serializer = getSerializer(entry.getValue());
                break;
            }
        }

        if (serializer != null) {
            return serializer;
        }

        return getFallbackSerializer();
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
     * Retrieves the global instance for the given serializer class.
     * <p/>
     * If an instance has not be previously registered, the serializer will be instantiated and registered before being
     * returned. If the serializer has not been previously registered and does not have a 0-arg constructor an exception
     * will be thrown.
     *
     * @param serializerClass The serializer class to get the global instance for.
     * @param <S> The serializer class type.
     * @return The global instance of the given serializer class.
     * @throws IllegalArgumentException thrown when a previously unregistered serializer class is given that does not
     * have a 0-arg constructor.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    public <S extends Serializer> S getSerializerInstance(@NotNull Class<S> serializerClass) throws IllegalArgumentException {
        if (serializeWithSerializers.containsKey(serializerClass)) {
            return (S) serializeWithSerializers.get(serializerClass).get();
        }
        try {
            S serializer = createInstance(serializerClass);
            registerSerializerInstance(serializerClass, serializer);
            return serializer;
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not instantiate " + serializerClass + ". Does it have a 0-arg constructor?", e);
        }
    }

    /**
     * Retrieves the standard serializer for the specific given type. This is the last serializer choice checked before
     * the fallback serializer is used.
     *
     * @see #getClassSerializer(Class)
     *
     * @param serializableClass the class to get the serializer for.
     * @param <T> the type of the class.
     * @return the standard serializer for the given type or null if non-existent.
     */
    @Nullable
    @SuppressWarnings("unchecked")
    private <T> Serializer<T> getSerializer(Class<T> serializableClass) {
        Supplier<Serializer> serializer = serializers.get(serializableClass);
        return serializer != null ? serializer.get() : null;
    }

    /**
     * Retrieves the override serializer for the specific given type. This is the first serializer choice for a given
     * class except in rare circumstances.
     *
     * @see #getClassSerializer(Class)
     *
     * @param serializableClass the class to get the serializer for.
     * @param <T> the type of the class.
     * @return the override serializer for the given type or null if non-existent.
     */
    @Nullable
    @SuppressWarnings("unchecked")
    private <T> Serializer<T> getOverrideSerializer(Class<T> serializableClass) {
        Supplier<Serializer> serializer = overrideSerializers.get(serializableClass);
        return serializer != null ? serializer.get() : null;
    }

    /**
     * Retrieves the fallback serializer, which is used when no other appropriate serializer exists.
     *
     * @return the fallback serializer for this SerializerSet.
     */
    @NotNull
    public Serializer getFallbackSerializer() {
        return fallbackSerializer.get();
    }

    /**
     * Tries to create and store an instance of the given serializer class. This will only work for serializers with a
     * 0-arg constructor. If there is not a 0-arg constructor, nothing happens. Generally, {@link #registerSerializerInstance(Class, Serializer)}
     * should be used instead.
     */
    private <S extends Serializer> void tryRegisterSerializer(@NotNull Class<S> serializerClass) {
        try {
            S serializer = createInstance(serializerClass);
            registerSerializerInstance(serializerClass, serializer);
        } catch (Exception ignore) { }
    }

    @NotNull
    private <S extends Serializer> S createInstance(@NotNull Class<S> serializerClass) throws Exception {
        Constructor<S> constructor = serializerClass.getDeclaredConstructor();
        boolean accessible = constructor.isAccessible();
        if (!accessible) {
            constructor.setAccessible(true);
        }
        S serializer = constructor.newInstance();
        registerSerializerInstance(serializerClass, serializer);
        if (!accessible) {
            constructor.setAccessible(false);
        }
        return serializer;
    }

    /**
     * Registers a global instance of a given Serializer class for use with {@link SerializeWith}.
     * <p/>
     * For a class to be serialized by the serializer specified in {@link SerializeWith} the serializer must have a
     * registered global instance. If the serializer has a 0-arg constructor, a global instance will be created
     * automatically. This method allows for the registering of serializer instances for serializers without 0-arg
     * constructors.
     * <p/>
     * If the given serializer class has already been registered this method will <strong>replace</strong> the previous
     * global instance with the given instance.
     *
     * @param serializerClass the serializer class to register a global instance of.
     * @param serializer the instance of the serializer class to use as the global instance.
     * @param <T> the serializer class type.
     */
    private <T extends Serializer> void registerSerializerInstance(@NotNull Class<T> serializerClass, @NotNull T serializer) {
        serializeWithSerializers.put(serializerClass, () -> serializer);
    }

    private static final Serializer DEFAULT_SERIALIZER = new DefaultSerializer();
    private static final SerializerSet DEFAULT_SET;

    static {
        Map<Class, Supplier<Serializer>> serializers = new HashMap<>();

        // serializer;

        final Serializer numberSerializer = new NumberSerializer();
        serializers.put(Integer.class, () -> numberSerializer);
        serializers.put(Long.class, () -> numberSerializer);
        serializers.put(Double.class, () -> numberSerializer);
        serializers.put(Float.class, () -> numberSerializer);
        serializers.put(Byte.class, () -> numberSerializer);
        serializers.put(Short.class, () -> numberSerializer);

        final Serializer bigNumberSerializer = new BigNumberSerializer();
        serializers.put(BigInteger.class, () -> bigNumberSerializer);
        serializers.put(BigDecimal.class, () -> bigNumberSerializer);

        final Serializer atomicIntegerSerializer = new AtomicIntegerSerializer();
        serializers.put(AtomicInteger.class, () -> atomicIntegerSerializer);

        final Serializer atomicLongSerializer = new AtomicLongSerializer();
        serializers.put(AtomicLong.class, () -> atomicLongSerializer);

        final Serializer booleanSerializer = new BooleanSerializer();
        serializers.put(Boolean.class, () -> booleanSerializer);

        final Serializer characterSerializer = new CharacterSerializer();
        serializers.put(Character.class, () -> characterSerializer);

        final Serializer stringSerializer = new StringSerializer();
        serializers.put(String.class, () -> stringSerializer);

        final Serializer enumSerializer = new EnumSerializer();
        serializers.put(Enum.class, () -> enumSerializer);

        final Serializer uuidSerializer = new UUIDSerializer();
        serializers.put(UUID.class, () -> uuidSerializer);

        final Serializer fauxEnumSerializer = new FauxEnumSerializer();
        serializers.put(FauxEnum.class, () -> fauxEnumSerializer);

        final Serializer collectionSerializer = new CollectionSerializer();
        serializers.put(Collection.class, () -> collectionSerializer);

        final Serializer mapSerializer = new MapSerializer();
        serializers.put(Map.class, () -> mapSerializer);

        final Serializer localeSerializer = new LocaleSerializer();
        serializers.put(Locale.class, () -> localeSerializer);

        final Serializer arraySerializer = new ArraySerializer();
        serializers.put(Array.class, () -> arraySerializer);

        Map<Predicate<Class<?>>, Class> inheritanceReplacements = new LinkedHashMap<>(3);
        inheritanceReplacements.put(c -> c.isAnnotationPresent(FauxEnum.class), FauxEnum.class);
        inheritanceReplacements.put(Collection.class::isAssignableFrom, Collection.class);
        inheritanceReplacements.put(Map.class::isAssignableFrom, Map.class);
        inheritanceReplacements.put(Enum.class::isAssignableFrom, Enum.class);
        inheritanceReplacements.put(Class::isArray, Array.class);

        DEFAULT_SET = new SerializerSet(serializers, new HashMap<>(), () -> DEFAULT_SERIALIZER, new HashMap<>(), inheritanceReplacements);
    }
}
