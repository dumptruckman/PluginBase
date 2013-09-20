package com.dumptruckman.minecraft.pluginbase.config.field;

import com.dumptruckman.minecraft.pluginbase.config.SerializationRegistrar;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FieldMapper {

    private static final Map<Class, FieldMap> compiledFieldMaps = new HashMap<Class, FieldMap>();

    private final Class clazz;

    public static FieldMap getFieldMap(@NotNull Class clazz) {
        if (compiledFieldMaps.containsKey(clazz)) {
            return compiledFieldMaps.get(clazz);
        }
        FieldMapper fieldMapper = new FieldMapper(clazz);
        FieldMap fieldMap = new FieldMap(fieldMapper.mapFields());
        compiledFieldMaps.put(clazz, fieldMap);
        return fieldMap;
    }

    private FieldMapper(@NotNull Class clazz) {
        if (!SerializationRegistrar.isClassRegistered(clazz)) {
            throw new IllegalArgumentException(clazz + " is not registered for serialization/deserialization.");
        }
        this.clazz = clazz;
    }

    private Map<String, Field> mapFields() {
        java.lang.reflect.Field[] allFields = collectAllFieldsForClass(clazz);
        Map<String, Field> resultMap = new LinkedHashMap<String, Field>(allFields.length);
        for (java.lang.reflect.Field field : allFields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                Field localField;
                if (SerializationRegistrar.isClassRegistered(field.getType())) {
                    if (field.getType().equals(clazz)) {
                        throw new IllegalStateException("Mapping fields for " + clazz + " would result in infinite recursion due self containment.");
                    }
                    localField = new Field(field, getFieldMap(field.getType()));
                } else {
                    localField = new Field(field);
                }
                resultMap.put(field.getName().toLowerCase(), localField);
            }
        }
        return resultMap;
    }

    private java.lang.reflect.Field[] collectAllFieldsForClass(Class clazz) {
        java.lang.reflect.Field[] declaredFields = clazz.getDeclaredFields();
        Class superClass = clazz.getSuperclass();
        if (superClass != null) {
            java.lang.reflect.Field[] superFields = collectAllFieldsForClass(superClass);
            int length = declaredFields.length;
            declaredFields = Arrays.copyOf(declaredFields, declaredFields.length + superFields.length);
            System.arraycopy(superFields, 0, declaredFields, length, superFields.length);
        }
        return declaredFields;
    }
}
