package pluginbase.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Tells the serializer to ignore fields from any parent interfaces/classes and only use the fields declared in the
 * annotated class.
 * <p/>
 * If this is present on a parent of a child class it will include the fields of both the parent and child but will not
 * include any from the parent's parent.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreParentFields {
}
