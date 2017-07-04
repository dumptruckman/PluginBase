package pluginbase.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This tells the serializer to treat this class like an enum.
 * <br>
 * This <b>requires</b> the class to have a <code>public static T valueOf(String)</code> factory method as well as a
 * <code>public String name()</code> method which will output the name which can be used to retrieve the same or an
 * equal object from the aforementioned <code>valueOf</code> method.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FauxEnum {
}
