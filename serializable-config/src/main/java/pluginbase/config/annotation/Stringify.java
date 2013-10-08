package pluginbase.config.annotation;

import pluginbase.config.properties.DefaultStringifier;
import pluginbase.config.properties.Stringifier;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface Stringify {
    Class<? extends Stringifier> withClass() default DefaultStringifier.class;
    boolean allowSetProperty() default true;
}
