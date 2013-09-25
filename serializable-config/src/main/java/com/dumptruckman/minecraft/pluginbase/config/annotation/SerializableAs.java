package com.dumptruckman.minecraft.pluginbase.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents an "alias" that a class may be stored as.  If this is not present on a class, it will use the
 * fully qualified name of the class.
 * <p/>
 * This value will be stored in the configuration so that the configuration deserialization
 * can determine what type it is.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SerializableAs {
    /**
     * This is the name your class will be stored and retrieved as.
     * <p/>
     * This name MUST be unique.
     *
     * @return Name to serialize the class as.
     */
    public String value();
}
