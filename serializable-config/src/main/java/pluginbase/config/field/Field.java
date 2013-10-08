package pluginbase.config.field;

import com.googlecode.gentyref.GenericTypeReflector;
import pluginbase.config.annotation.Comment;
import pluginbase.config.annotation.Description;
import pluginbase.config.annotation.Immutable;
import pluginbase.config.annotation.Name;
import pluginbase.config.annotation.SerializeWith;
import pluginbase.config.annotation.Stringify;
import pluginbase.config.annotation.ValidateWith;
import pluginbase.config.properties.PropertyAliases;
import pluginbase.config.properties.Stringifier;
import pluginbase.config.properties.Stringifiers;
import pluginbase.config.serializers.Serializer;
import pluginbase.config.serializers.Serializers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Collection;

public class Field extends FieldMap {

    @NotNull
    private final java.lang.reflect.Field field;
    private final boolean persistable;
    private final boolean immutable;
    private final String name;
    private final Type type;
    private final Class typeClass;
    private final Class collectionType;
    private final Stringify stringify;

    @Nullable
    public static FieldInstance getInstance(@NotNull Object object, @NotNull String... name) {
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
        Name name = field.getAnnotation(Name.class);
        if (name == null) {
            name = field.getType().getAnnotation(Name.class);
        }
        if (name != null) {
            this.name = name.value();
        } else {
            this.name = field.getName();
        }
        this.immutable = field.getAnnotation(Immutable.class) != null;
        this.type = determineActualType();
        if (type instanceof Class) {
            typeClass = (Class) type;
        } else {
            if (type instanceof WildcardType) {
                typeClass = Object.class;
            } else {
                typeClass = GenericTypeReflector.erase(type);
            }
        }
        this.collectionType = determineCollectionType();
        this.stringify = field.getAnnotation(Stringify.class);
    }

    private Type determineActualType() {
        Type type = field.getGenericType();
        Class clazz = GenericTypeReflector.erase(type);
        if (VirtualField.class.isAssignableFrom(clazz)) {
            type = GenericTypeReflector.getExactSuperType(type, VirtualField.class);
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                type = parameterizedType.getActualTypeArguments()[0];
            }
        }
        return type;
    }

    private Class determineCollectionType() {
        if (Collection.class.isAssignableFrom(getType())) {
            Type collectionType = GenericTypeReflector.getExactSuperType(type, Collection.class);
            if (collectionType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) collectionType;
                collectionType = parameterizedType.getActualTypeArguments()[0];
                if (collectionType instanceof Class) {
                    return (Class) collectionType;
                }
            }
            if (collectionType instanceof WildcardType) {
                return Object.class;
            }
            return GenericTypeReflector.erase(collectionType);
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public boolean hasChildFields() {
        return fieldMap != null;
    }

    public boolean isPersistable() {
        return persistable;
    }

    public boolean isImmutable() {
        return immutable;
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
            if (VirtualField.class.isAssignableFrom(field.getType())) {
                VirtualField vProp = (VirtualField) field.get(object);
                return vProp != null ? vProp.get() : null;
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
        if (isImmutable()) {
            return;
        }
        forceSet(object, value);
    }

    public void forceSet(@NotNull Object object, @Nullable Object value) throws PropertyVetoException {
        boolean accessible = field.isAccessible();
        if (!accessible) {
            field.setAccessible(true);
        }
        try {
            if (VirtualField.class.isAssignableFrom(field.getType())) {
                setVirtualProperty((VirtualField) field.get(object), value);
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

    private void setVirtualProperty(@Nullable VirtualField vProp, @Nullable Object value) throws PropertyVetoException {
        if (vProp == null) {
            return;
        }
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

    @NotNull
    public Class getType() {
        return typeClass;
    }

    @Nullable
    public Class getCollectionType() {
        return collectionType;
    }

    @NotNull
    public Stringifier getStringifier() {
        return stringify != null ? Stringifiers.getStringifier(stringify.withClass()) : Stringifiers.getDefaultStringifier();
    }

    public boolean isAllowingSetProperty() {
        return stringify == null || stringify.allowSetProperty();
    }

    @Nullable
    public String getDescription() {
        Description description = field.getAnnotation(Description.class);
        if (description != null) {
            return description.value();
        } else {
            description = field.getType().getAnnotation(Description.class);
            if (description != null) {
                return description.value();
            }
        }
        return null;
    }

    @Nullable
    public String[] getComments() {
        Comment comment = field.getAnnotation(Comment.class);
        if (comment != null) {
            return comment.value();
        } else {
            comment = field.getType().getAnnotation(Comment.class);
            if (comment != null) {
                return comment.value();
            }
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
