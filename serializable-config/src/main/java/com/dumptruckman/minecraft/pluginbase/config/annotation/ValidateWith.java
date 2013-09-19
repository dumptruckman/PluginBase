package com.dumptruckman.minecraft.pluginbase.config.annotation;

import com.dumptruckman.minecraft.pluginbase.config.Validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to define what {@link me.main__.util.SerializationConfig.Validator} to use to validate the value of a class field.
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateWith {
    Class<? extends Validator> value();
}
