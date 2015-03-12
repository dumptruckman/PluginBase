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

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class PropertiesWrapper implements Properties {

    private static final transient String SEPARATOR = "";
    private static final transient String SEPARATOR_REGEX = "\\.";

    @Immutable
    @NotNull
    private final transient Object object;

    public static Properties wrapObject(@NotNull Object object) {
        return new PropertiesWrapper(object);
    }

    public static String[] getAllPropertyNames(Class clazz) {
        return new PropertyNameExtractor(clazz).extractPropertyNames();
    }

    private PropertiesWrapper(@NotNull Object object) {
        this.object = object;
    }

    protected PropertiesWrapper() {
        this.object = this;
    }

    @NotNull
    @Override
    public String[] getAllPropertyNames() {
        return getAllPropertyNames(object.getClass());
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
        try {
            field.getPropertyHandler().set(field, value);
        } catch (UnsupportedOperationException e) {
            throw new PropertyVetoException(Message.bundleMessage(CANNOT_SET_PROPERTY, name));
        }
    }

    @Override
    public void addProperty(@NotNull String name, @NotNull String value) throws IllegalAccessException, NoSuchFieldException, PropertyVetoException, IllegalArgumentException {
        FieldInstance field = getFieldInstanceForModify(name);
        try {
            field.getPropertyHandler().add(field, value);
        } catch (UnsupportedOperationException e) {
            throw new PropertyVetoException(Message.bundleMessage(CANNOT_MODIFY_NON_COLLECTION, name));
        }
    }

    @Override
    public void removeProperty(@NotNull String name, @NotNull String value) throws IllegalAccessException, NoSuchFieldException, PropertyVetoException, IllegalArgumentException {
        FieldInstance field = getFieldInstanceForModify(name);
        try {
            field.getPropertyHandler().remove(field, value);
        } catch (UnsupportedOperationException e) {
            throw new PropertyVetoException(Message.bundleMessage(CANNOT_MODIFY_NON_COLLECTION, name));
        }
    }

    @Override
    public void clearProperty(@NotNull String name, @Nullable String value) throws IllegalAccessException, NoSuchFieldException, PropertyVetoException, IllegalArgumentException {
        FieldInstance field = getFieldInstanceForModify(name);
        try {
            field.getPropertyHandler().clear(field, value);
        } catch (UnsupportedOperationException e) {
            throw new PropertyVetoException(Message.bundleMessage(CANNOT_MODIFY_NON_COLLECTION, name));
        }
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
    public boolean clearPropertyUnchecked(@NotNull String name, @Nullable String value) {
        try {
            clearProperty(name, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private FieldInstance getFieldInstance(@NotNull String name) throws NoSuchFieldException {
        String[] path = PropertyAliases.getPropertyName(object.getClass(), name);
        if (path == null) {
            path = name.split(SEPARATOR_REGEX);
        }
        FieldInstance field = Field.getInstance(object, path);
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

    private static class PropertyNameExtractor {
        Class clazz;
        List<String> allProperties;
        Deque<String> currentPropertyParents;

        PropertyNameExtractor(Class clazz) {
            this.clazz = clazz;
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
                buffer.append(SEPARATOR);
            }
            buffer.append(name);
            allProperties.add(buffer.toString());
        }
    }
}
