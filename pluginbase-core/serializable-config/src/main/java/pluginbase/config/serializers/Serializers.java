package pluginbase.config.serializers;

import org.jetbrains.annotations.NotNull;
import pluginbase.config.annotation.FauxEnum;
import pluginbase.config.annotation.SerializeWith;
import pluginbase.config.util.PrimitivesUtil;
import pluginbase.logging.Logging;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class provides methods for obtaining serializer instances and registering serializer instances for usage by
 * {@link SerializeWith}.
 * </p>
 * Serializers have a general order of priority in which they should be used. See {@link #getClassSerializer(Class, SerializerSet)}
 * for details.
 */
public enum Serializers {
    ;

    static final Serializer DEFAULT_SERIALIZER = new DefaultSerializer();
    private static final Map<Class<? extends Serializer>, Serializer> SERIALIZER_MAP = new ConcurrentHashMap<>();

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
     * @param serializerSet a set of serializers to look for appropriate serializers in.
     * @return the most appropriate serializer for the given class.
     */
    @NotNull
    public static Serializer getClassSerializer(@NotNull Class<?> clazz, @NotNull SerializerSet serializerSet) {
        clazz = PrimitivesUtil.switchForWrapper(clazz);

        Serializer serializer = serializerSet.getOverrideSerializer(clazz);
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

        serializer = serializerSet.getSerializer(clazz);
        if (serializer != null) {
            return serializer;
        }

        if (clazz.isAnnotationPresent(FauxEnum.class)) {
            serializer = serializerSet.getSerializer(FauxEnum.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            serializer = serializerSet.getSerializer(Collection.class);
        } else if (Map.class.isAssignableFrom(clazz)) {
            serializer = serializerSet.getSerializer(Map.class);
        } else if (Enum.class.isAssignableFrom(clazz)) {
            serializer = serializerSet.getSerializer(Enum.class);
        } else if (clazz.isArray()) {
            serializer = serializerSet.getSerializer(Array.class);
        }

        if (serializer != null) {
            return serializer;
        }

        return serializerSet.getFallbackSerializer();
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
    public static <T extends Serializer> void registerSerializerInstance(@NotNull Class<T> serializerClass, @NotNull T serializer) {
        SERIALIZER_MAP.put(serializerClass, serializer);
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
    public static <S extends Serializer> S getSerializerInstance(@NotNull Class<S> serializerClass) throws IllegalArgumentException {
        if (SERIALIZER_MAP.containsKey(serializerClass)) {
            return (S) SERIALIZER_MAP.get(serializerClass);
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
     * Tries to create and store an instance of the given serializer class. This will only work for serializers with a
     * 0-arg constructor. If there is not a 0-arg constructor, nothing happens. Generally, {@link #registerSerializerInstance(Class, Serializer)}
     * should be used instead.
     */
    public static <S extends Serializer> void tryRegisterSerializer(@NotNull Class<S> serializerClass) {
        try {
            S serializer = createInstance(serializerClass);
            registerSerializerInstance(serializerClass, serializer);
        } catch (Exception ignore) { }
    }

    @NotNull
    private static <S extends Serializer> S createInstance(@NotNull Class<S> serializerClass) throws Exception {
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
}
