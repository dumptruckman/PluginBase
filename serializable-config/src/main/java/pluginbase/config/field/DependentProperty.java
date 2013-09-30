package pluginbase.config.field;

import org.jetbrains.annotations.Nullable;

/**
 * An implementation of VirtualProperty that acts as a proxy to a dependent object but also supplies a backup value in
 * that object's absence.
 *
 * @param <T> The type for virtual property.
 * @param <D> The type of the dependent object.
 */
public abstract class DependentProperty<T, D> implements VirtualProperty<T> {

    /** This is the value used when {@link #getDependency()} returns null. */
    @Nullable
    protected T backupValue;

    /**
     * Constructs a new DepdendentProperty with the given initial value.
     *
     * @param initialValue
     */
    public DependentProperty(@Nullable T initialValue) {
        this.backupValue = initialValue;
    }

    /**
     * Returns the value from the dependency if it is available, otherwise returns a backup value.
     */
    @Nullable
    @Override
    public final T get() {
        if (getDependency() == null) {
            return backupValue;
        } else {
            return getDependentValue();
        }
    }

    /**
     * Sets the value for the dependency if it is available.
     * <p/>
     * Also will always set the backup value if {@link #alwaysSetBackupValue()} returns true.
     *
     * @param value the new value to use.
     */
    @Override
    public final void set(@Nullable T value) {
        if (alwaysSetBackupValue() && !isBackupStatic()) {
            this.backupValue = value;
        }
        if (getDependency() != null) {
            setDependentValue(value);
        } else if (!alwaysSetBackupValue() && !isBackupStatic()) {
            this.backupValue = value;
        }
    }

    /**
     * Indicates that {@link #set(Object)} should always set the backup value regardless of if the dependency is available.
     *
     * @return true to always set the backup value when using {@link #set(Object)}.
     */
    protected boolean alwaysSetBackupValue() {
        return true;
    }

    /**
     * Indicates that the backup value should never be changed via the {@link #set(Object)} method.
     *
     * @return true to never change the backup value via {@link #set(Object)}.
     */
    protected boolean isBackupStatic() {
        return false;
    }

    /**
     * Retrieves the dependent object for this virtual property.
     *
     * @return the dependent object for this virtual property. Should this return null, {@link #get()} and {@link #set(Object)}
     * will refer to the backup value.
     */
    @Nullable
    protected abstract D getDependency();

    /**
     * Retrieves the value from the dependent object.  This will be called only when {@link #getDependency()} does not return null.
     */
    protected abstract T getDependentValue();

    /**
     * Sets the value for the dependent object.  This will be called only when {@link #getDependency()} does not return null.
     */
    protected abstract void setDependentValue(@Nullable T value);
}
