package com.dumptruckman.minecraft.pluginbase.config.serializers;

import com.dumptruckman.minecraft.pluginbase.config.ConfigSerializer;
import com.dumptruckman.minecraft.pluginbase.config.SerializationRegistrar;
import com.dumptruckman.minecraft.pluginbase.config.field.Field;
import com.dumptruckman.minecraft.pluginbase.config.field.FieldMap;
import com.dumptruckman.minecraft.pluginbase.config.field.FieldMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class DefaultSerializer implements Serializer<Object> {

    private static final Class[] EMPTY_PARAM_TYPE_ARRAY = new Class[0];
    private static final Object[] EMPTY_PARAM_VALUE_ARRAY = new Object[0];
    private static final Class[] SIZE_PARAM_TYPE_ARRAY = new Class[] {Integer.class};
    private static final Map<Class, Class> PRIMITIVE_WRAPPER_MAP;

    static {
        Map<Class, Class> map = new HashMap<Class, Class>();
        map.put(int.class, Integer.class);
        map.put(boolean.class, Boolean.class);
        map.put(long.class, Long.class);
        map.put(double.class, Double.class);
        map.put(float.class, Float.class);
        map.put(byte.class, Byte.class);
        map.put(short.class, Short.class);
        PRIMITIVE_WRAPPER_MAP = Collections.unmodifiableMap(map);
    }

    @NotNull
    @Override
    public Object serialize(@Nullable Object object) {
        if (object != null && SerializationRegistrar.isClassRegistered(object.getClass())) {
            return serializeRegisteredType(object);
        } else if (object instanceof Map) {
            return serializeMap((Map) object);
        } else if (object instanceof Collection) {
            return serializeCollection((Collection) object);
        } else if (object instanceof Iterable) {
            return serializeIterable((Iterable) object);
        } else {
            return String.valueOf(object);
        }
    }

    @NotNull
    public Map<String, Object> serializeRegisteredType(@NotNull Object object) {
        if (!SerializationRegistrar.isClassRegistered(object.getClass())) {
            throw new IllegalArgumentException(object.getClass() + " is not registered for serialization.");
        }
        FieldMap fieldMap = FieldMapper.getFieldMap(object.getClass());
        Map<String, Object> serializedMap = new LinkedHashMap<String, Object>(fieldMap.size() + 1);
        serializedMap.put(ConfigSerializer.SERIALIZED_TYPE_KEY, SerializationRegistrar.getAlias(object.getClass()));
        for (Field field : fieldMap) {
            if (field.isPersistable()) {
                serializedMap.put(field.getName(), serializeField(object, field));
            }
        }
        return serializedMap;
    }

    @NotNull
    protected Object serializeField(@NotNull Object object, @NotNull Field field) {
        Object value = field.getValue(object);
        return field.getSerializer().serialize(value);
    }

    @NotNull
    protected Map<String, Object> serializeMap(@NotNull Map<?, ?> map) {
        Map<String, Object> result = new LinkedHashMap<String, Object>(map.size());
        for (Map.Entry entry : map.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (key != null && value != null) {
                result.put(key.toString(), serialize(entry.getValue()));
            }
        }
        return result;
    }

    @NotNull
    protected List<Object> serializeCollection(@NotNull Collection collection) {
        return new CopyOnWriteArrayList<Object>(collection);
    }

    @NotNull
    protected List<Object> serializeIterable(@NotNull Iterable iterable) {
        List<Object> result = new ArrayList<Object>();
        for (Object object : iterable) {
            result.add(object);
        }
        return result;
    }

    @NotNull
    @Override
    public Object deserialize(@NotNull Object serialized, @NotNull Class wantedType) throws IllegalArgumentException {
        System.out.println("Deserializing " + serialized.getClass() + " to " + wantedType);
        if (PRIMITIVE_WRAPPER_MAP.containsKey(wantedType)) {
            wantedType = PRIMITIVE_WRAPPER_MAP.get(wantedType);
        }
        if (wantedType.isInstance(serialized)) {
            return serialized;
        }
        if (SerializationRegistrar.isClassRegistered(wantedType)) {
            if (serialized instanceof Map) {
                return deserializeRegisteredType((Map) serialized, wantedType);
            } else {
                throw new IllegalArgumentException("Deserializing a registered type requires serialized data to be a Map");
            }
        }
        if (serialized instanceof Collection && Collection.class.isAssignableFrom(wantedType)) {
            return deserializeCollection((Collection) serialized, (Class<? extends Collection>) wantedType);
        }
        if (serialized instanceof Map && Map.class.isAssignableFrom(wantedType)) {
            return deserializeMap((Map) serialized, (Class<? extends Map>) wantedType);
        }
        try {
            Method valueOf = wantedType.getMethod("valueOf", String.class);
            return valueOf.invoke(null, serialized);
        } catch (Exception e) {
            return deserializeUnknownType(serialized);
        }
    }

    protected Object deserializeRegisteredType(@NotNull Map data, @NotNull Class wantedType) {
        Object typeInstance;
        if (Modifier.isFinal(wantedType.getModifiers())) {
            typeInstance = createInstance(wantedType);
        } else {
            Class clazz = ConfigSerializer.getClassFromSerializedData(data);
            if (clazz == null) {
                throw new IllegalArgumentException("The given data is does not contain valid serialized data");
            }
            typeInstance = createInstance(clazz);
        }
        return deserializeToObject(data, typeInstance);
    }

    protected <T> T createInstance(Class<T> wantedType) {
        return createInstance(wantedType, EMPTY_PARAM_TYPE_ARRAY, EMPTY_PARAM_VALUE_ARRAY);
    }

    protected <T> T createInstance(Class<T> wantedType, Class[] parameterTypes, Object[] parameterValues) {
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
                if (isEligibleForDeserializationToObject(serializedFieldData, fieldValue)) {
                    fieldValue = deserializeFieldToObject(field, serializedFieldData, fieldValue);
                } else {
                    fieldValue = deserializeField(field, serializedFieldData);
                }
                field.setValue(object, fieldValue);
            } else {
                // TODO fail silently or no?
            }
        }
        return object;
    }

    protected boolean isEligibleForDeserializationToObject(@NotNull Object data, @Nullable Object object) {
        return object != null && SerializationRegistrar.isClassRegistered(object.getClass()) && data instanceof Map;
    }

    protected Object deserializeFieldToObject(@NotNull Field field, @NotNull Object data, @NotNull Object object) {
        System.out.println("Deserializing " + field + " to " + object + " with data " + data);
        return field.getSerializer().deserializeToObject((Map) data, object);
    }

    protected Object deserializeField(Field field, Object data) {
        System.out.println("Deserializing " + field + " with data " + data);
        return field.getSerializer().deserialize(data, field.getType());
    }

    protected Object deserializeCollection(@NotNull Collection data, @NotNull Class<? extends Collection> wantedType) {
        Collection collection;
        try {
            Object[] paramValues = new Object[] {data.size()};
            collection = createInstance(wantedType, SIZE_PARAM_TYPE_ARRAY, paramValues);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof NoSuchMethodException) {
                collection = createInstance(wantedType);
            } else {
                throw e;
            }
        }
        System.out.println("Deserializing collection " + data + " to " + collection.getClass());
        for (Object object : data) {
            collection.add(deserializeUnknownType(object));
        }
        return collection;
    }

    protected Object deserializeUnknownType(Object object) {
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

    protected Object deserializeMap(@NotNull Map<?, ?> data, @NotNull Class<? extends Map> wantedType) {
        if (data.containsKey(ConfigSerializer.SERIALIZED_TYPE_KEY)) {
            return ConfigSerializer.deserialize(data);
        }
        Map map;
        try {
            Object[] paramValues = new Object[] {data.size()};
            map = createInstance(wantedType, SIZE_PARAM_TYPE_ARRAY, paramValues);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof NoSuchMethodException) {
                map = createInstance(wantedType);
            } else {
                throw e;
            }
        }
        for (Map.Entry entry : data.entrySet()) {
            map.put(deserializeUnknownType(entry.getKey()), deserializeUnknownType(entry.getValue()));
        }
        return map;
    }
}
