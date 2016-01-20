package pluginbase.config.serializers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.config.SerializableConfig;
import pluginbase.config.annotation.NoTypeKey;
import pluginbase.config.annotation.SerializableAs;
import pluginbase.config.field.Field;
import pluginbase.config.field.FieldMap;
import pluginbase.config.field.FieldMapper;
import pluginbase.config.field.PropertyVetoException;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

class DefaultSerializer implements Serializer<Object> {

    @Nullable
    @Override
    public Object serialize(@Nullable Object object, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        if (object == null) {
            return null;
        }
        FieldMap fieldMap = FieldMapper.getFieldMap(object.getClass());
        Map<String, Object> serializedMap = new LinkedHashMap<>(fieldMap.size() + 1);
        if (!(object.getClass().isAnnotationPresent(NoTypeKey.class) && Modifier.isFinal(object.getClass().getModifiers()))) {
            serializedMap.put(SerializableConfig.SERIALIZED_TYPE_KEY, getAlias(object.getClass()));
        }
        for (Field field : fieldMap) {
            if (field.isPersistable()) {
                serializedMap.put(field.getName(), serializeField(object, field, serializerSet));
            }
        }
        return serializedMap;
    }

    @NotNull
    private String getAlias(@NotNull Class clazz) {
        SerializableAs alias = (SerializableAs) clazz.getAnnotation(SerializableAs.class);

        if ((alias != null) && (alias.value() != null)) {
            return alias.value();
        }

        return clazz.getName();
    }


    @Nullable
    @SuppressWarnings("unchecked")
    protected Object serializeField(@NotNull Object object, @NotNull Field field, @NotNull SerializerSet serializerSet) {
        Object value = field.getValue(object);
        if (value == null) {
            return null;
        }
        return field.getSerializer(serializerSet).serialize(value, serializerSet);
    }


    @Nullable
    @Override
    public Object deserialize(@Nullable Object serialized, @NotNull Class wantedType, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        if (serialized == null) {
            return null;
        }
        if (wantedType.isAssignableFrom(serialized.getClass())) {
            // Already deserialized
            return serialized;
        }
        if (!(serialized instanceof Map)) {
            throw new IllegalArgumentException("Serialized value must be a map to be deserialized as an object");
        }
        Map data = (Map) serialized;
        Object typeInstance;
        if (wantedType.isEnum()) {
            Object name = data.get("name");
            if (name != null) {
                try {
                    typeInstance = Enum.valueOf(wantedType, name.toString());
                } catch (IllegalArgumentException e) {
                    typeInstance = Enum.valueOf(wantedType, name.toString().toUpperCase());;
                }
            } else {
                throw new IllegalArgumentException("The serialized enum does not contain a name which is required for deserialization");
            }
        } else if (Modifier.isFinal(wantedType.getModifiers())) {
            typeInstance = InstanceUtil.createInstance(wantedType);
        } else {
            Class clazz = SerializableConfig.getClassFromSerializedData(data);
            if (clazz != null) {
                typeInstance = InstanceUtil.createInstance(clazz);
            } else {
                try {
                    typeInstance = InstanceUtil.createInstance(wantedType);
                } catch (RuntimeException e) {
                    throw new IllegalArgumentException("The serialized form does not contain enough information to deserialize", e);
                }
            }
        }
        return deserializeToObject(data, typeInstance, serializerSet);
    }

    public Object deserializeToObject(@NotNull Map data, @NotNull Object object, @NotNull SerializerSet serializerSet) {
        FieldMap fieldMap = FieldMapper.getFieldMap(object.getClass());
        for (Object key : data.keySet()) {
            if (key.equals(SerializableConfig.SERIALIZED_TYPE_KEY)) {
                continue;
            }
            Field field = fieldMap.getField(key.toString());
            if (field != null) {
                Object fieldValue = field.getValue(object);
                Object serializedFieldData = data.get(key);
                if (serializedFieldData == null) {
                    fieldValue = null;
                } else {
                    Class asClass = fieldValue != null ? fieldValue.getClass() : field.getType();
                    if (Collection.class.isAssignableFrom(field.getType()) && serializedFieldData instanceof Collection) {
                        fieldValue = deserializeCollection(field, (Collection<?>) serializedFieldData, asClass, serializerSet);
                    } else if (Map.class.isAssignableFrom(field.getType()) && serializedFieldData instanceof Map) {
                        fieldValue = deserializeMap(field, (Map<?, ?>) serializedFieldData, asClass, serializerSet);
                    } else if (fieldValue != null && serializedFieldData instanceof Map) {
                        fieldValue = deserializeFieldAs(field, serializedFieldData, fieldValue.getClass(), serializerSet);
                    } else {
                        fieldValue = deserializeFieldAs(field, serializedFieldData, field.getType(), serializerSet);
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

    protected Collection<?> deserializeCollection(@NotNull Field field, @NotNull Collection<?> data, @NotNull Class asClass, @NotNull SerializerSet serializerSet) {
        Collection collection = CollectionSerializer.createCollection(asClass, data.size());
        for (Object object : data) {
            Class collectionType = field.getCollectionType();
            if (collectionType != null && !collectionType.equals(Object.class)) {
                collection.add(SerializableConfig.deserializeAs(object, field.getCollectionType(), serializerSet));
            } else {
                collection.add(SerializableConfig.deserialize(object, serializerSet));
            }
        }
        return collection;
    }

    protected Map<?, ?> deserializeMap(@NotNull Field field, @NotNull Map<?, ?> data, @NotNull Class asClass, @NotNull SerializerSet serializerSet) {
        Map map = MapSerializer.createMap(asClass, data.size());
        for (Map.Entry entry : data.entrySet()) {
            Class mapType = field.getMapType();
            if (mapType != null && !mapType.equals(Object.class)) {
                map.put(SerializableConfig.deserialize(entry.getKey(), serializerSet), SerializableConfig.deserializeAs(entry.getValue(), mapType, serializerSet));
            } else {
                map.put(SerializableConfig.deserialize(entry.getKey(), serializerSet), SerializableConfig.deserialize(entry.getValue(), serializerSet));
            }
        }
        return map;
    }

    protected Object deserializeFieldAs(@NotNull Field field, @NotNull Object data, @NotNull Class asClass, @NotNull SerializerSet serializerSet) {
        try {
            return field.getSerializer(serializerSet).deserialize(data, asClass, serializerSet);
        } catch (Exception e) {
            throw new RuntimeException("Exception while deserializing field '" + field + "' as class '" + asClass + "'", e);
        }
    }
}
