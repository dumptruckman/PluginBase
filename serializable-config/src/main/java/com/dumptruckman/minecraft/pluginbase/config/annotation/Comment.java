package com.dumptruckman.minecraft.pluginbase.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows for the possibility of comments to appear with the serialized form of a field if they are supported by
 * the {@link org.bukkit.configuration.Configuration}.
 * <p/>
 * Use multiple {@link String}s to have multi-lined comments.
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Comment {
    String[] value();
}
