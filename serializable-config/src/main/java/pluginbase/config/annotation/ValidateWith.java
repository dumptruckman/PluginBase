package pluginbase.config.annotation;

import pluginbase.config.field.Validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to define what {@link pluginbase.config.field.Validator} to use to validate the value of a class field before it is set via {@link pluginbase.config.properties.Properties#setProperty(Object, String...)}.
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateWith {
    Class<? extends Validator> value();
}
