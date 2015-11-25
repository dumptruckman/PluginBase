package pluginbase.config.annotation;

import pluginbase.config.field.Validator;
import pluginbase.config.field.Field;
import pluginbase.config.properties.Properties;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to define what {@link Validator} to use to validate the value of a class field before it
 * is set via {@link Field#setValue(Object, Object)} which is utilized in {@link Properties} and Serializable-Config's
 * fallback serializer.
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateWith {
    Class<? extends Validator> value();
}
