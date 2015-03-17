package pluginbase.config.serializers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.config.ConfigSerializer;
import pluginbase.config.SerializationRegistrar;
import pluginbase.config.annotation.FauxEnum;
import pluginbase.config.annotation.NoTypeKey;
import pluginbase.config.annotation.SerializeWith;
import pluginbase.config.field.Field;
import pluginbase.config.field.FieldMap;
import pluginbase.config.field.FieldMapper;
import pluginbase.config.field.PropertyVetoException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class handles the majority of typical serialization and deserialization.
 * <p/>
 * At times you may have broader serialization needs than what a simple implementation of {@link pluginbase.config.serializers.Serializer}
 * can get you, such as wanting ALMOST all of what default serialization includes except for one specific part.  This
 * abstract class offers you the ability to override those specific parts of the default serialization process to suit
 * your specific needs.
 * <p/>
 * When overriding methods in this class it is important to note that the serialization and deserialization should work
 * with the formats specified in {@link Serializer#serialize(Object)} and {@link Serializer#deserialize(Object, Class)}.
 */
public abstract class AbstractSerializer implements Serializer<Object> {

    private static final Class[] EMPTY_PARAM_TYPE_ARRAY = new Class[0];
    private static final Object[] EMPTY_PARAM_VALUE_ARRAY = new Object[0];
    private static final Class[] SIZE_PARAM_TYPE_ARRAY = new Class[] {Integer.class};

    /** Contains a mapping of primitive classes to their object forms. */
    protected static final Map<Class, Class> PRIMITIVE_WRAPPER_MAP;

    static {
        Map<Class, Class> map = new HashMap<>();
        map.put(int.class, Integer.class);
        map.put(boolean.class, Boolean.class);
        map.put(long.class, Long.class);
        map.put(double.class, Double.class);
        map.put(float.class, Float.class);
        map.put(byte.class, Byte.class);
        map.put(short.class, Short.class);
        PRIMITIVE_WRAPPER_MAP = Collections.unmodifiableMap(map);
    }

    /**
     * Retrieves the {@link pluginbase.config.serializers.Serializer} for the given field.
     * <p/>
     * If no serializer has been specified with the {@link pluginbase.config.annotation.SerializeWith} annotation then
     * then default serializer will be returned.
     *
     * @param field The field to get the serializer for.
     * @return The serializer for the field.
     */
    @NotNull
    protected Serializer getSerializer(@NotNull Field field) {
        Serializer serializer = field.getSerializer();
        return serializer;
    }

    /**
     * Retrieves the {@link pluginbase.config.serializers.Serializer} for the given class.
     * <p/>
     * If no serializer has been specified with the {@link pluginbase.config.annotation.SerializeWith} annotation then
     * then default serializer will be returned.
     *
     * @param clazz The class to get the serializer for.
     * @return The serializer for the class.
     */
    @NotNull
    protected Serializer getSerializer(@NotNull Class clazz) {
        SerializeWith serializeWith = (SerializeWith) clazz.getAnnotation(SerializeWith.class);
        if (serializeWith != null) {
            return Serializers.getSerializer(serializeWith.value());
        }
        return this;
    }

    /** {@inheritDoc} */
    @Nullable
    @Override
    public final Object serialize(@Nullable Object object) {
        if (object == null) {
            return null;
        }

        Class clazz = object.getClass();

        Serializer otherSerializer = getSerializer(clazz);
        if (!(otherSerializer instanceof AbstractSerializer)) {
            return otherSerializer.serialize(object);
        }
        AbstractSerializer serializer = (AbstractSerializer) otherSerializer;

        if (SerializationRegistrar.isClassRegistered(clazz)) {
            if (clazz.isAnnotationPresent(FauxEnum.class)) {
                return serializer.serializeFauxEnum(object, clazz);
            } else {
                return serializer.serializeRegisteredType(object);
            }
        } else if (object instanceof Map) {
            return serializer.serializeMap((Map) object);
        } else if (object instanceof Collection) {
            return serializer.serializeCollection((Collection) object);
        } else if (object instanceof Enum) {
            return serializer.serializeEnum((Enum) object);
        } else if (object instanceof String) {
            return serializer.serializeString((String) object);
        } else if (object instanceof UUID) {
            return serializer.serializeUUID((UUID) object);
        } else if (PRIMITIVE_WRAPPER_MAP.containsValue(clazz) || PRIMITIVE_WRAPPER_MAP.containsKey(clazz)) {
            return serializer.serializePrimitive(object);
        } else {
            throw new IllegalArgumentException(clazz + " is not registered or not a valid type for serialization.");
        }
    }

    /**
     * Used to serialize enums.
     * <p/>
     * See the general statement in {@link pluginbase.config.serializers.AbstractSerializer}'s javadocs regarding what
     * is expected of the returned object.
     *
     * @param e The enum to serialize.
     * @return The serialized form of the enum.
     */
    @Nullable
    protected Object serializeEnum(@NotNull Enum e) {
        return e.name();
    }

    /**
     * Used to serialize strings.
     * <p/>
     * See the general statement in {@link pluginbase.config.serializers.AbstractSerializer}'s javadocs regarding what
     * is expected of the returned object.
     *
     * @param string The string to serialize.
     * @return The serialized form of the string.
     */
    @Nullable
    protected Object serializeString(@NotNull String string) {
        return string;
    }

    /**
     * Used to serialize UUIDs.
     * <p/>
     * See the general statement in {@link pluginbase.config.serializers.AbstractSerializer}'s javadocs regarding what
     * is expected of the returned object.
     *
     * @param uuid The UUID to serialize.
     * @return The serialized form of the UUID.
     */
    @Nullable
    protected Object serializeUUID(@NotNull UUID uuid) {
        return uuid.toString();
    }

    /**
     * Used to serialize primitives.
     * <p/>
     * See the general statement in {@link pluginbase.config.serializers.AbstractSerializer}'s javadocs regarding what
     * is expected of the returned object.
     *
     * @param primitive The primitive to serialize.
     * @return The serialized form of the primitive.
     */
    @Nullable
    protected Object serializePrimitive(@NotNull Object primitive) {
        return primitive;
    }

    /**
     * Used to serialize maps.
     * <p/>
     * See the general statement in {@link pluginbase.config.serializers.AbstractSerializer}'s javadocs regarding what
     * is expected of the returned object.
     *
     * @param map The map to serialize.
     * @return The serialized form of the map.
     */
    @Nullable
    protected Map<String, Object> serializeMap(@NotNull Map<?, ?> map) {
        Map<String, Object> result = new LinkedHashMap<>(map.size());
        for (Map.Entry entry : map.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (key != null && value != null) {
                result.put(key.toString(), serialize(entry.getValue()));
            }
        }
        return result;
    }

    /**
     * Used to serialize collections.
     * <p/>
     * See the general statement in {@link pluginbase.config.serializers.AbstractSerializer}'s javadocs regarding what
     * is expected of the returned object.
     *
     * @param collection The collection to serialize.
     * @return The serialized form of the collection.
     */
    @Nullable
    protected List<Object> serializeCollection(@NotNull Collection collection) {
        return new CopyOnWriteArrayList<Object>(collection);
    }

    /**
     * Used to serialize faux enum objects. See {@link pluginbase.config.annotation.FauxEnum} for details on such objects.
     * <p/>
     * See the general statement in {@link pluginbase.config.serializers.AbstractSerializer}'s javadocs regarding what
     * is expected of the returned object.
     *
     * @param fauxEnumValue The faux enum value to serialize.
     * @param fauxEnumClass The faux enum class for the value being serialized.
     * @return The serialized form of the faux enum value.
     */
    @Nullable
    protected String serializeFauxEnum(@NotNull Object fauxEnumValue, Class fauxEnumClass) {
        try {
            Method method = fauxEnumClass.getMethod("name");
            return (String) method.invoke(fauxEnumValue);
        } catch (Exception e) {
            throw new IllegalArgumentException("The class " + fauxEnumClass + " is annotated as a FauxEnum but is lacking the required name method.");
        }
    }

    /**
     * Used to serialize registered types.
     * <p/>
     * Only objects of a type registered via {@link SerializationRegistrar#registerClass(Class)} will be passed to this
     * method.
     * <p/>
     * See the general statement in {@link pluginbase.config.serializers.AbstractSerializer}'s javadocs regarding what
     * is expected of the returned object.
     *
     * @param object The object to serialize.
     * @return The serialized form of the object.
     */
    @Nullable
    protected Object serializeRegisteredType(@NotNull Object object) {
        if (!SerializationRegistrar.isClassRegistered(object.getClass())) {
            throw new IllegalArgumentException(object.getClass() + " is not registered for serialization.");
        }
        FieldMap fieldMap = FieldMapper.getFieldMap(object.getClass());
        Map<String, Object> serializedMap = new LinkedHashMap<String, Object>(fieldMap.size() + 1);
        if (!(object.getClass().isAnnotationPresent(NoTypeKey.class) && Modifier.isFinal(object.getClass().getModifiers()))) {
            serializedMap.put(ConfigSerializer.SERIALIZED_TYPE_KEY, SerializationRegistrar.getAlias(object.getClass()));
        }
        for (Field field : fieldMap) {
            if (field.isPersistable()) {
                serializedMap.put(field.getName(), serializeField(object, field));
            }
        }
        return serializedMap;
    }

    /**
     * Used to serialize the fields of a registered type.
     * <p/>
     * See {@link #serializeRegisteredType(Object)} regarding registered types.
     * <p/>
     * See the general statement in {@link pluginbase.config.serializers.AbstractSerializer}'s javadocs regarding what
     * is expected of the returned object.
     *
     * @param object The object that contains this field.
     * @param field The field to serialize.
     * @return The serialized form of the value of the given field from the given object.
     */
    @Nullable
    protected Object serializeField(@NotNull Object object, @NotNull Field field) {
        Object value = field.getValue(object);
        Serializer serializer = getSerializer(field);
        return serializer.serialize(value);
    }

    /** {@inheritDoc} */
    @Nullable
    @Override
    public final Object deserialize(@Nullable Object serialized, @NotNull Class wantedType) throws IllegalArgumentException {
        if (PRIMITIVE_WRAPPER_MAP.containsKey(wantedType)) {
            wantedType = PRIMITIVE_WRAPPER_MAP.get(wantedType);
        }
        if (serialized != null && wantedType.equals(serialized.getClass())) {
            return serialized;
        }

        Serializer otherSerializer = getSerializer(wantedType);
        if (!(otherSerializer instanceof AbstractSerializer)) {
            return otherSerializer.deserialize(serialized, wantedType);
        }
        AbstractSerializer serializer = (AbstractSerializer) otherSerializer;

        if (SerializationRegistrar.isClassRegistered(wantedType)) {
            if (serialized != null && wantedType.isAssignableFrom(serialized.getClass())) {
                // Already deserialized somehow...
                return serialized;
            } else if (wantedType.isAnnotationPresent(FauxEnum.class)) {
                return serializer.deserializeFauxEnum(serialized, wantedType);
            } else {
                return serializer.deserializeRegisteredType(serialized, wantedType);
            }
        } else if (Collection.class.isAssignableFrom(wantedType)) {
            return serializer.deserializeCollection(serialized, (Class<? extends Collection>) wantedType);
        } else if (Map.class.isAssignableFrom(wantedType)) {
            return serializer.deserializeMap(serialized, (Class<? extends Map>) wantedType);
        } else if (wantedType.equals(UUID.class)) {
            return serializer.deserializeUUID(serialized);
        } else if (Enum.class.isAssignableFrom(wantedType)) {
            return serializer.deserializeEnum(serialized, wantedType);
        } else if (wantedType.equals(String.class)) {
            return serializer.deserializeString(serialized);
        } else if (PRIMITIVE_WRAPPER_MAP.containsValue(wantedType) || PRIMITIVE_WRAPPER_MAP.containsKey(wantedType)) {
            return serializer.deserializePrimitive(serialized, wantedType);
        }
        try {
            Method valueOf = wantedType.getMethod("valueOf", String.class);
            return valueOf.invoke(null, serialized.toString());
        } catch (Exception e) {
            return serializer.deserializeUnknownType(serialized);
        }
    }

    /**
     * Deserializes an object as the given enum type.
     * <p/>
     * Generally if null is passed in as the serialized value, null will be returned.
     *
     * @param serialized the serialized enum value.
     * @param wantedType the enum type to deserialize as.
     * @return the deserialized enum value.
     */
    @Nullable
    protected Object deserializeEnum(@Nullable Object serialized, @NotNull Class wantedType) {
        return serialized == null ? null : Enum.valueOf(wantedType, serialized.toString());
    }

    /**
     * Deserializes an object as a string.
     * <p/>
     * Generally if null is passed in as the serialized value, null will be returned.
     *
     * @param serialized the serialized string value.
     * @return the deserialized string value.
     */
    @Nullable
    protected Object deserializeString(@Nullable Object serialized) {
        return serialized == null ? null : serialized.toString();
    }

    /**
     * Deserializes an object as a UUID.
     * <p/>
     * Generally if null is passed in as the serialized value, null will be returned.
     *
     * @param serialized the serialized UUID value.
     * @return the deserialized UUID value.
     */
    @Nullable
    protected Object deserializeUUID(@Nullable Object serialized) {
        return serialized == null ? null : UUID.fromString(serialized.toString());
    }

    @Nullable
    protected Object deserializePrimitive(@Nullable Object serialized, @NotNull Class wantedType) {
        if (serialized == null) {
            return null;
        }
        try {
            Method valueOf = wantedType.getMethod("valueOf", String.class);
            return valueOf.invoke(null, serialized.toString());
        } catch (Exception e) {
            throw new RuntimeException("There was a problem deserializing a primitive value.", e);
        }
    }

    @Nullable
    protected Object deserializeMap(@Nullable Object serialized, @NotNull Class<? extends Map> wantedType) {
        if (serialized == null) {
            return null;
        }
        if (!(serialized instanceof Map)) {
            throw new IllegalArgumentException("Serialized value must be a map to be deserialized as a map");
        }
        Map<?, ?> data = (Map<?, ?>) serialized;
        if (data.containsKey(ConfigSerializer.SERIALIZED_TYPE_KEY)) {
            return ConfigSerializer.deserialize(data);
        }
        Map map = createMap(wantedType, data.size());
        for (Map.Entry entry : data.entrySet()) {
            map.put(deserializeUnknownType(entry.getKey()), deserializeUnknownType(entry.getValue()));
        }
        return map;
    }

    @NotNull
    protected Map createMap(@NotNull Class<? extends Map> wantedType, int size) {
        if (wantedType.isInterface()) {
            return new LinkedHashMap(size);
        } else {
            try {
                Object[] paramValues = new Object[] { size };
                return createInstance(wantedType, SIZE_PARAM_TYPE_ARRAY, paramValues);
            } catch (RuntimeException e) {
                if (e.getCause() instanceof NoSuchMethodException) {
                    try {
                        return createInstance(wantedType);
                    } catch (RuntimeException e1) {
                        if (e1.getCause() instanceof NoSuchMethodException) {
                            return new LinkedHashMap(size);
                        } else {
                            throw e;
                        }
                    }
                } else {
                    throw e;
                }
            }
        }
    }

    @Nullable
    protected Object deserializeCollection(@Nullable Object serialized, @NotNull Class<? extends Collection> wantedType) {
        if (serialized == null) {
            return null;
        }
        if (!(serialized instanceof Collection)) {
            throw new IllegalArgumentException("Serialized value must be a collection to be deserialized as a collection");
        }
        Collection data = (Collection) serialized;
        Collection collection = createCollection(wantedType, data.size());
        for (Object object : data) {
            collection.add(deserializeUnknownType(object));
        }
        return collection;
    }

    @NotNull
    protected Collection createCollection(@NotNull Class<? extends Collection> wantedType, int size) {
        if (wantedType.isInterface()) {
            return new ArrayList(size);
        } else {
            try {
                Object[] paramValues = new Object[] { size };
                return createInstance(wantedType, SIZE_PARAM_TYPE_ARRAY, paramValues);
            } catch (RuntimeException e) {
                if (e.getCause() instanceof NoSuchMethodException) {
                    try {
                        return createInstance(wantedType);
                    } catch (RuntimeException e1) {
                        if (e1.getCause() instanceof NoSuchMethodException) {
                            return new ArrayList(size);
                        } else {
                            throw e;
                        }
                    }
                } else {
                    throw e;
                }
            }
        }
    }

    @Nullable
    protected Object deserializeFauxEnum(@Nullable Object value, @NotNull Class fauxEnumClass) {
        if (value == null) {
            return null;
        }
        try {
            Method valueOfMethod = fauxEnumClass.getDeclaredMethod("valueOf", String.class);
            return valueOfMethod.invoke(null, value);
        } catch (Exception e) {
            throw new IllegalArgumentException("The class " + fauxEnumClass + " is annotated as a FauxEnum but is lacking the required valueOf method.");
        }
    }

    @Nullable
    protected Object deserializeRegisteredType(@Nullable Object serialized, @NotNull Class wantedType) {
        if (serialized == null) {
            return null;
        }
        if (!(serialized instanceof Map)) {
            throw new IllegalArgumentException("Serialized value must be a map to be deserialized as a registered type");
        }
        Map data = (Map) serialized;
        Object typeInstance;
        if (Modifier.isFinal(wantedType.getModifiers())) {
            typeInstance = createInstance(wantedType);
        } else {
            Class clazz = ConfigSerializer.getClassFromSerializedData(data);
            if (clazz != null) {
                typeInstance = createInstance(clazz);
            } else {
                try {
                    typeInstance = createInstance(wantedType);
                } catch (RuntimeException e) {
                    throw new IllegalArgumentException("The serialized form does not contain enough information to deserialize", e);
                }
            }
        }
        return deserializeToObject(data, typeInstance);
    }

    @NotNull
    protected <T> T createInstance(@NotNull Class<T> wantedType) {
        return createInstance(wantedType, EMPTY_PARAM_TYPE_ARRAY, EMPTY_PARAM_VALUE_ARRAY);
    }

    @NotNull
    protected <T> T createInstance(@NotNull Class<T> wantedType, @NotNull Class[] parameterTypes, @NotNull Object[] parameterValues) {
        try {
            Constructor<T> constructor = wantedType.getDeclaredConstructor(parameterTypes);
            boolean accessible = constructor.isAccessible();
            if (!accessible) {
                constructor.setAccessible(true);
            }
            try {
                return constructor.newInstance(parameterValues);
            } finally {
                if (!accessible) {
                    constructor.setAccessible(false);
                }
            }
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public Object deserializeToObject(@NotNull Map data, @NotNull Object object) {
        if (!SerializationRegistrar.isClassRegistered(object.getClass())) {
            throw new IllegalArgumentException(object.getClass() + " is not registered for serialization.");
        }
        FieldMap fieldMap = FieldMapper.getFieldMap(object.getClass());
        for (Object key : data.keySet()) {
            if (key.equals(ConfigSerializer.SERIALIZED_TYPE_KEY)) {
                continue;
            }
            Field field = fieldMap.getField(key.toString());
            if (field != null) {
                Object fieldValue = field.getValue(object);
                Object serializedFieldData = data.get(key);
                if (serializedFieldData == null) {
                    fieldValue = null;
                } else {
                    if (isEligibleForDeserializationToObject(serializedFieldData, fieldValue)) {
                        fieldValue = deserializeFieldAs(field, serializedFieldData, fieldValue.getClass());
                    } else {
                        fieldValue = deserializeField(field, serializedFieldData);
                    }
                }
                try {
                    field.forceSet(object, fieldValue);
                } catch (PropertyVetoException e) {
                    e.printStackTrace();
                }
            } else {
                // TODO fail silently or no?
            }
        }
        return object;
    }

    protected boolean isEligibleForDeserializationToObject(@NotNull Object data, @Nullable Object object) {
        return object != null && SerializationRegistrar.isClassRegistered(object.getClass()) && data instanceof Map;
    }

    protected Object deserializeFieldAs(@NotNull Field field, @NotNull Object data, @NotNull Class asClass) {
        Serializer serializer = getSerializer(field);
        try {
            return serializer.deserialize(data, asClass);
        } catch (Exception e) {
            throw new RuntimeException("Exception while deserializing field '" + field + "' as class '" + asClass + "'", e);
        }
    }

    protected Object deserializeField(Field field, Object data) {
        Serializer serializer = getSerializer(field);
        try {
            return serializer.deserialize(data, field.getType());
        } catch (Exception e) {
            throw new RuntimeException("Exception while deserializing field: " + field, e);
        }
    }

    @Nullable
    protected Object deserializeUnknownType(@Nullable Object object) {
        if (object instanceof Map) {
            Map map = (Map) object;
            if (ConfigSerializer.getClassFromSerializedData(map) != null) {
                return ConfigSerializer.deserialize(map);
            } else {
                return deserializeMap(map, map.getClass());
            }
        } else if (object instanceof Collection) {
            Collection coll = (Collection) object;
            return deserializeCollection(coll, coll.getClass());
        } else {
            return object;
        }
    }
}
