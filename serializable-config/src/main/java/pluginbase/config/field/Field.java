package pluginbase.config.field;

import pluginbase.config.annotation.Comment;
import pluginbase.config.annotation.Description;
import pluginbase.config.annotation.SerializeWith;
import pluginbase.config.annotation.ValidateWith;
import pluginbase.config.properties.PropertyAliases;
import pluginbase.config.serializers.Serializer;
import pluginbase.config.serializers.Serializers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyVetoException;
import java.lang.reflect.Modifier;

public class Field extends FieldMap {

    @NotNull
    private final java.lang.reflect.Field field;
    private final boolean persistable;

    @Nullable
    public static FieldInstance locateField(@NotNull Object object, @NotNull String... name) {
        if (name.length == 0) {
            throw new IllegalArgumentException("name cannot be 0 length array.");
        }
        if (name.length == 1) {
            String[] actualName = PropertyAliases.getPropertyName(object.getClass(), name[0]);
            if (actualName != null) {
                name = actualName;
            }
        }
        return new FieldInstance(object, name).locateField();
    }

    Field(@NotNull java.lang.reflect.Field field) {
        this(field, null);
    }

    Field(@NotNull java.lang.reflect.Field field, @Nullable FieldMap children) {
        super(children == null ? null : children.fieldMap);
        this.field = field;
        persistable = !Modifier.isTransient(field.getModifiers());
    }

    public String getName() {
        return field.getName();
    }

    public boolean hasChildFields() {
        return fieldMap != null;
    }

    public boolean isPersistable() {
        return persistable;
    }

    @NotNull
    public Serializer getSerializer() {
        SerializeWith serializeWith = field.getAnnotation(SerializeWith.class);
        if (serializeWith != null) {
            return Serializers.getSerializer(serializeWith.value());
        } else {
            serializeWith = field.getType().getAnnotation(SerializeWith.class);
            if (serializeWith != null) {
                return Serializers.getSerializer(serializeWith.value());
            }
        }
        return Serializers.getDefaultSerializer();
    }

    @Nullable
    public Validator getValidator() {
        ValidateWith validateWith = field.getAnnotation(ValidateWith.class);
        if (validateWith != null) {
            return Validators.getValidator(validateWith.value());
        }
        return null;
    }

    @Nullable
    public Object getValue(@NotNull Object object) {
        boolean accessible = field.isAccessible();
        if (!accessible) {
            field.setAccessible(true);
        }
        try {
            if (VirtualProperty.class.isAssignableFrom(field.getType())) {
                VirtualProperty vProp = (VirtualProperty) field.get(object);
                return vProp.get();
            } else {
                return field.get(object);
            }
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError("This should never happen.");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("The specified object does not contain this field.", e);
        } finally {
            if (!accessible) {
                field.setAccessible(false);
            }
        }
    }

    public void setValue(@NotNull Object object, @Nullable Object value) throws PropertyVetoException {
        boolean accessible = field.isAccessible();
        if (!accessible) {
            field.setAccessible(true);
        }
        try {
            if (VirtualProperty.class.isAssignableFrom(field.getType())) {
                setVirtualProperty((VirtualProperty) field.get(object), value);
            } else {
                setProperty(object, value);
            }
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError("This should never happen.");
        } catch (IllegalArgumentException e) {
            try {
                FieldMap fieldMap = FieldMapper.getFieldMap(object.getClass());
                if (fieldMap.hasField(getName())) {
                    throw new IllegalArgumentException("The specified value is not an instance of type " + getType(), e);
                }
            } catch (IllegalArgumentException ignore) { }
            throw new IllegalArgumentException("The specified object does not contain " + this, e);
        } finally {
            if (!accessible) {
                field.setAccessible(false);
            }
        }
    }

    private void setVirtualProperty(@NotNull VirtualProperty vProp, @Nullable Object value) throws PropertyVetoException {
        Validator validator = getValidator();
        if (validator != null) {
            vProp.set(validator.validateChange(value, vProp.get()));
        } else {
            vProp.set(value);
        }
    }

    private void setProperty(@NotNull Object object, @Nullable Object value) throws IllegalAccessException, PropertyVetoException {
        Validator validator = getValidator();
        if (validator != null) {
            field.set(object, validator.validateChange(value, field.get(object)));
        } else {
            field.set(object, value);
        }
    }

    public Class getType() {
        return field.getType();
    }

    @Nullable
    public String getDescription() {
        Description description = field.getAnnotation(Description.class);
        if (description != null) {
            return description.value();
        }
        return null;
    }

    @Nullable
    public String[] getComments() {
        Comment comment = field.getAnnotation(Comment.class);
        if (comment != null) {
            return comment.value();
        }
        return null;
    }

    @Override
    public String toString() {
        return "Field{" +
                "field=" + field +
                '}';
    }
}
