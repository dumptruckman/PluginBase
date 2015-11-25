package pluginbase.config.annotation;

import pluginbase.config.SerializableConfig;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents an "alias" that a class may be stored as.  If this is not present on a class, serializers will generally
 * use the fully qualified name of the class instead.
 * <p/>
 * This name MUST be unique. A non-unique name may result in undesirable behavior.
 * <p/>
 * <strong>Note:</strong> To use this annotation, the class <strong>must</strong> be registered via
 * {@link SerializableConfig#registerSerializableAsClass(Class)} before any serialization occurs or
 * problems may arise.
 *
 * @see NoTypeKey
 * @see Name
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SerializableAs {
    String value();
}
