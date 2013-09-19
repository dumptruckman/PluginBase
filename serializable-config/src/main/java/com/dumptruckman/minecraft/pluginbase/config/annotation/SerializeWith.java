package com.dumptruckman.minecraft.pluginbase.config.annotation;

import com.dumptruckman.minecraft.pluginbase.config.Serializer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to define what {@link me.main__.util.SerializationConfig.Serializer} to use to serialize/deserialize the value of a class field to/from
 * its serialized form.
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SerializeWith {
    Class<? extends Serializer> value();
}
