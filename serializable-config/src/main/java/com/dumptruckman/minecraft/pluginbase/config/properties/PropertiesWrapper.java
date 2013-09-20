package com.dumptruckman.minecraft.pluginbase.config.properties;

import com.dumptruckman.minecraft.pluginbase.config.field.Field;
import com.dumptruckman.minecraft.pluginbase.config.field.FieldInstance;
import com.dumptruckman.minecraft.pluginbase.config.field.FieldMap;
import com.dumptruckman.minecraft.pluginbase.config.field.FieldMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyVetoException;

public class PropertiesWrapper implements Properties {

    @NotNull
    private Object object;
    @NotNull
    private FieldMap fieldMap;

    public static Properties wrapObject(@NotNull Object object) {
        return new PropertiesWrapper(object);
    }

    private PropertiesWrapper(@NotNull Object object) {
        this.object = object;
        this.fieldMap = FieldMapper.getFieldMap(object.getClass());
    }

    protected PropertiesWrapper() {
        this.object = this;
        this.fieldMap = FieldMapper.getFieldMap(object.getClass());
    }

    @Nullable
    @Override
    public Object getProperty(@NotNull String... name) throws NoSuchFieldException, IllegalArgumentException {
        FieldInstance field = Field.locateField(object, name);
        if (field == null) {
            throw new NoSuchFieldException("No property by that name exists.");
        }
        return field.getFieldValue();
    }

    @Override
    public void setProperty(@Nullable Object value, @NotNull String... name) throws NoSuchFieldException, PropertyVetoException, IllegalArgumentException {
        FieldInstance field = Field.locateField(object, name);
        if (field == null) {
            throw new NoSuchFieldException("No property by that name exists.");
        }
        Object oldValue = field.getFieldValue();
        value = field.getValidator().validateChange(value, oldValue);
        field.setFieldValue(value);
    }

    @Nullable
    @Override
    public Object getPropertyUnchecked(@NotNull String... name) throws IllegalArgumentException {
        FieldInstance field = Field.locateField(object, name);
        if (field == null) {
            return null;
        }
        return field.getFieldValue();
    }

    @Override
    public boolean setPropertyUnchecked(@Nullable Object value, @NotNull String... name) throws IllegalArgumentException {
        FieldInstance field = Field.locateField(object, name);
        if (field != null) {
            try {
                Object oldValue = field.getFieldValue();
                value = field.getValidator().validateChange(value, oldValue);
                field.setFieldValue(value);
                return true;
            } catch (PropertyVetoException e) {
                return false;
            }
        } else {
            return false;
        }
    }
}
