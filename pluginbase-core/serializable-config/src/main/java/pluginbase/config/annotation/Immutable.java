package pluginbase.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import pluginbase.config.field.Field;
import pluginbase.config.properties.PropertiesWrapper;

/**
 * Prevents setting of this field via {@link Field#setValue(Object, Object)} but still allows setting via
 * {@link Field#forceSet(Object, Object)}.
 * <br>
 * This means {@link PropertiesWrapper#setProperty(String, String)} and {@link PropertiesWrapper#setPropertyUnchecked(String, String)}
 * will fail to set the field but deserialization with the default serializer will still work.
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Immutable {
}
