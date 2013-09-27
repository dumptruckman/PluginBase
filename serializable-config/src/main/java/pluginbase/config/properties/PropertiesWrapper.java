package pluginbase.config.properties;

import pluginbase.config.field.Field;
import pluginbase.config.field.FieldInstance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.config.field.PropertyVetoException;

public class PropertiesWrapper implements Properties {

    @NotNull
    private transient Object object;

    public static Properties wrapObject(@NotNull Object object) {
        return new PropertiesWrapper(object);
    }

    private PropertiesWrapper(@NotNull Object object) {
        this.object = object;
    }

    protected PropertiesWrapper() {
        this.object = this;
    }

    @Nullable
    @Override
    public Object getProperty(@NotNull String... name) throws NoSuchFieldException, IllegalArgumentException {
        FieldInstance field = Field.getInstance(object, name);
        if (field == null) {
            throw new NoSuchFieldException("No property by that name exists.");
        }
        return field.getValue();
    }

    @Override
    public void setProperty(@Nullable Object value, @NotNull String... name) throws IllegalAccessException, NoSuchFieldException, PropertyVetoException, IllegalArgumentException {
        FieldInstance field = Field.getInstance(object, name);
        if (field == null) {
            throw new NoSuchFieldException("No property by that name exists.");
        }
        if (field.isFinal()) {
            throw new IllegalAccessException("You may not modify this property.");
        }
        field.setValue(value);
    }

    @Nullable
    @Override
    public Object getPropertyUnchecked(@NotNull String... name) throws IllegalArgumentException {
        FieldInstance field = Field.getInstance(object, name);
        if (field == null) {
            return null;
        }
        return field.getValue();
    }

    @Override
    public boolean setPropertyUnchecked(@Nullable Object value, @NotNull String... name) throws IllegalArgumentException {
        FieldInstance field = Field.getInstance(object, name);
        if (field != null) {
            try {
                field.setValue(value);
                return true;
            } catch (PropertyVetoException e) {
                return false;
            }
        } else {
            return false;
        }
    }
}
