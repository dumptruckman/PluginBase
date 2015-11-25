package pluginbase.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows for the possibility of comments to appear with the serialized form of a field if they are supported by
 * the {@link pluginbase.config.datasource.DataSource} used.
 * <p/>
 * Use multiple {@link String}s to have multi-lined comments.
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Comment {
    String[] value();
}
