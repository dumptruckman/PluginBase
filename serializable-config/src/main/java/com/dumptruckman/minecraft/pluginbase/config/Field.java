package com.dumptruckman.minecraft.pluginbase.config;

import com.dumptruckman.minecraft.pluginbase.config.annotation.SerializeWith;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Modifier;

public class Field extends FieldMap {

    @NotNull
    private final java.lang.reflect.Field field;
    private final boolean persistable;

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

    public Serializer getSerializer() {
        SerializeWith serializeWith = field.getAnnotation(SerializeWith.class);
        if (serializeWith != null) {
            return Serializers.getSerializer(serializeWith.value());
        }
        return Serializers.getSerializer(DefaultSerializer.class);
    }

    public Object getValue(@NotNull Object object) {
        boolean accessible = field.isAccessible();
        if (!accessible) {
            field.setAccessible(true);
        }
        try {
            return field.get(object);
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

    public void setValue(@NotNull Object object, Object value) {
        boolean accessible = field.isAccessible();
        if (!accessible) {
            field.setAccessible(true);
        }
        try {
            field.set(object, value);
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

    public Class getType() {
        return field.getType();
    }

    @Override
    public String toString() {
        return field.toGenericString();
    }
}
