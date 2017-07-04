package pluginbase.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This will cause the type to be serialized without using a serialized type key to identify it within the serialized
 * data.  This may only be applied to final types.
 * <br>
 * The serialized type key is what allows the default serialization to correctly identify the class to instantiate when
 * deserializing data.  Without the type key on a non-final type, it is impossible to tell if the serialized data should
 * be made into the class indicated by a field or if it is suppose to be a subclass of that type.
 * <br>
 * <b>Note:</b> What is important to remember about this annotation is that if in the future, the annotated type is made
 * no longer final, then previously serialized data may not deserialize properly or at all.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoTypeKey {
}
