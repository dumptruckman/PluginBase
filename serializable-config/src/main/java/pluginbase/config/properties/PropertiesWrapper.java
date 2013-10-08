package pluginbase.config.properties;

import pluginbase.config.annotation.Immutable;
import pluginbase.config.field.Field;
import pluginbase.config.field.FieldInstance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.config.field.FieldMap;
import pluginbase.config.field.FieldMapper;
import pluginbase.config.field.PropertyVetoException;
import pluginbase.messages.Message;

import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class PropertiesWrapper implements Properties {

    public static void initializePropertyMessages() { }

    @Immutable
    @NotNull
    private final transient Object object;
    @NotNull
    @Immutable
    private final transient String separator;
    @NotNull
    @Immutable
    private final transient String separatorRegex;

    public static Properties wrapObject(@NotNull Object object, @NotNull String separator) {
        return new PropertiesWrapper(object, separator);
    }

    public static String[] getAllPropertyNames(Class clazz, @NotNull String separator) {
        return new PropertyNameExtractor(clazz, separator).extractPropertyNames();
    }

    private PropertiesWrapper(@NotNull Object object, @NotNull String separator) {
        this.object = object;
        this.separator = separator;
        this.separatorRegex = "\\Q" + separator + "\\E";
    }

    protected PropertiesWrapper(@NotNull String separator) {
        this.object = this;
        this.separator = separator;
        this.separatorRegex = "\\Q" + separator + "\\E";
    }

    @NotNull
    public String getNameSeparator() {
        return separator;
    }

    @NotNull
    @Override
    public String[] getAllPropertyNames() {
        return getAllPropertyNames(object.getClass(), separator);
    }

    @Nullable
    @Override
    public Object getProperty(@NotNull String name) throws NoSuchFieldException {
        FieldInstance field = getFieldInstance(name);
        return field.getValue();
    }

    @Override
    public void setProperty(@NotNull String name, @NotNull String value) throws IllegalAccessException, NoSuchFieldException, PropertyVetoException, IllegalArgumentException {
        FieldInstance field = getFieldInstanceForModify(name);
        if (!field.isAllowingSetProperty()) {
            throw new PropertyVetoException(Message.bundleMessage(CANNOT_SET_PROPERTY, name));
        }
        Object destringifiedValue = field.getStringifier().valueOf(value, field.getType());
        field.setValue(destringifiedValue);
    }

    @Override
    public void addProperty(@NotNull String name, @NotNull String value) throws IllegalAccessException, NoSuchFieldException, PropertyVetoException, IllegalArgumentException {
        FieldInstance field = getFieldInstanceForListModify(name);
        Class collectionType = field.getCollectionType();
        Collection collection = (Collection) field.getValue();
        Object destringifiedValue = field.getStringifier().valueOf(value, collectionType);
        if (destringifiedValue instanceof Collection && !Collection.class.isAssignableFrom(collectionType)) {
            collection.addAll((Collection) destringifiedValue);
        } else {
            collection.add(destringifiedValue);
        }
    }

    @Override
    public void removeProperty(@NotNull String name, @NotNull String value) throws IllegalAccessException, NoSuchFieldException, PropertyVetoException, IllegalArgumentException {
        FieldInstance field = getFieldInstanceForListModify(name);
        Class collectionType = field.getCollectionType();
        Collection collection = (Collection) field.getValue();
        Object destringifiedValue = field.getStringifier().valueOf(value, collectionType);
        if (destringifiedValue instanceof Collection && !Collection.class.isAssignableFrom(collectionType)) {
            collection.removeAll((Collection) destringifiedValue);
        } else {
            collection.remove(destringifiedValue);
        }
    }

    @Override
    public void clearProperty(@NotNull String name) throws IllegalAccessException, NoSuchFieldException, PropertyVetoException, IllegalArgumentException {
        FieldInstance field = getFieldInstanceForListModify(name);
        Collection collection = (Collection) field.getValue();
        collection.clear();
    }

    @Nullable
    @Override
    public Object getPropertyUnchecked(@NotNull String name) {
        try {
            return getProperty(name);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean setPropertyUnchecked(@NotNull String name, @NotNull String value) {
        try {
            setProperty(name, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean addPropertyUnchecked(@NotNull String name, @NotNull String value) {
        try {
            addProperty(name, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean removePropertyUnchecked(@NotNull String name, @NotNull String value) {
        try {
            removeProperty(name, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean clearPropertyUnchecked(@NotNull String name) {
        try {
            clearProperty(name);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private FieldInstance getFieldInstance(@NotNull String name) throws NoSuchFieldException {
        FieldInstance field = Field.getInstance(object, name.split(separatorRegex));
        if (field == null) {
            throw new NoSuchFieldException("No property by that name exists.");
        }
        return field;
    }

    private FieldInstance getFieldInstanceForModify(@NotNull String name) throws NoSuchFieldException, IllegalAccessException {
        FieldInstance field = getFieldInstance(name);
        if (field.isImmutable()) {
            throw new IllegalAccessException("You may not modify this property.");
        }
        return field;
    }

    private FieldInstance getFieldInstanceForListModify(@NotNull String name) throws NoSuchFieldException, IllegalAccessException, PropertyVetoException {
        FieldInstance field = getFieldInstanceForModify(name);
        if (field.getCollectionType() == null) {
            throw new PropertyVetoException(Message.bundleMessage(CANNOT_MODIFY_NON_COLLECTION, name));
        }
        return field;
    }

    private static class PropertyNameExtractor {
        Class clazz;
        List<String> allProperties;
        Deque<String> currentPropertyParents;
        String separator;

        PropertyNameExtractor(Class clazz, String separator) {
            this.clazz = clazz;
            this.separator = separator;
        }

        public String[] extractPropertyNames() {
            prepareBuffers();
            appendNamesFromFieldMap(FieldMapper.getFieldMap(clazz));
            return allProperties.toArray(new String[allProperties.size()]);
        }

        private void prepareBuffers() {
            allProperties = new LinkedList<String>();
            currentPropertyParents = new LinkedList<String>();
        }

        private void appendNamesFromFieldMap(FieldMap fieldMap) {
            for (Field field : fieldMap) {
                String fieldName = field.getName();
                if (field.hasChildFields()) {
                    currentPropertyParents.add(fieldName);
                    appendNamesFromFieldMap(field);
                    currentPropertyParents.pollLast();
                } else {
                    if (!field.isImmutable()) {
                        appendPropertyName(fieldName);
                    }
                }
            }
        }

        private void appendPropertyName(String name) {
            StringBuilder buffer = new StringBuilder();
            for (String parent : currentPropertyParents) {
                buffer.append(parent);
                buffer.append(separator);
            }
            buffer.append(name);
            allProperties.add(buffer.toString());
        }
    }
}
