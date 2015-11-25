package pluginbase.config.annotation;

import pluginbase.config.properties.PropertyHandler;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a property handler for a field or type.
 *
 * @see PropertyHandler
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface HandlePropertyWith {
    Class<? extends PropertyHandler> value();
}
