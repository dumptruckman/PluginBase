package pluginbase.config.field;

import pluginbase.config.annotation.FauxEnum;
import pluginbase.config.annotation.IgnoreSuperFields;
import org.jetbrains.annotations.NotNull;
import pluginbase.config.serializers.SerializerSet;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FieldMapper {

    private static final Map<Class, FieldMap> compiledFieldMaps = new HashMap<Class, FieldMap>();

    private final Class clazz;

    public static <T> T mapFields(T source, T destination) {
        FieldMap sourceMap = getFieldMap(source.getClass());
        destination = mapFields(sourceMap, source, destination);
        return destination;
    }

    private static <T> T mapFields(FieldMap sourceMap, T source, T destination) {
        for (Field field : sourceMap) {
            if (field.hasChildFields()) {
                mapFields(field, field.getValue(source), field.getValue(destination));
            } else {
                try {
                    field.setValue(destination, field.getValue(source));
                } catch (PropertyVetoException e) {
                    e.printStackTrace();
                }
            }
        }
        return destination;
    }

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
        this.clazz = clazz;
    }

    private Map<String, Field> mapFields() {
        java.lang.reflect.Field[] allFields = collectAllFieldsForClass(clazz);
        Map<String, Field> resultMap = new LinkedHashMap<String, Field>(allFields.length);
        for (java.lang.reflect.Field field : allFields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                Field localField;
                if (Map.class.isAssignableFrom(field.getType())
                        || Collection.class.isAssignableFrom(field.getType())
                        || field.getType().isAnnotationPresent(FauxEnum.class)
                        || SerializerSet.defaultSet().hasSerializerForClass(field.getType())) {
                    localField = new Field(field);
                } else {
                    if (field.getType().equals(clazz)) {
                        throw new IllegalStateException("Mapping fields for " + clazz + " would result in infinite recursion due self containment.");
                    }
                    localField = new Field(field, getFieldMap(field.getType()));
                }
                resultMap.put(localField.getName().toLowerCase(), localField);
            }
        }
        return resultMap;
    }

    private java.lang.reflect.Field[] collectAllFieldsForClass(Class clazz) {
        java.lang.reflect.Field[] declaredFields = clazz.getDeclaredFields();
        if (clazz.getAnnotation(IgnoreSuperFields.class) != null) {
            return declaredFields;
        }
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
