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
        if (Modifier.isFinal(wantedType.getModifiers())) {
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
                    if (isEligibleForDeserializationToObject(serializedFieldData, fieldValue)) {
                        fieldValue = deserializeFieldAs(field, serializedFieldData, fieldValue.getClass(), serializerSet);
                    } else {
                        fieldValue = deserializeField(field, serializedFieldData, serializerSet);
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
        return object != null && data instanceof Map;
    }

    protected Object deserializeFieldAs(@NotNull Field field, @NotNull Object data, @NotNull Class asClass, @NotNull SerializerSet serializerSet) {
        try {
            return field.getSerializer(serializerSet).deserialize(data, asClass, serializerSet);
        } catch (Exception e) {
            throw new RuntimeException("Exception while deserializing field '" + field + "' as class '" + asClass + "'", e);
        }
    }

    protected Object deserializeField(Field field, Object data, @NotNull SerializerSet serializerSet) {
        try {
            return field.getSerializer(serializerSet).deserialize(data, field.getType(), serializerSet);
        } catch (Exception e) {
            throw new RuntimeException("Exception while deserializing field: " + field, e);
        }
    }
}
