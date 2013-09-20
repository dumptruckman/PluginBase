package com.dumptruckman.minecraft.pluginbase.config.annotation;

import com.dumptruckman.minecraft.pluginbase.config.field.Validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to define what {@link Validator} to use to validate the value of a class field before it is set via {@link com.dumptruckman.minecraft.pluginbase.config.properties.Properties#setProperty(Object, String...)}.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateWith {
    Class<? extends Validator> value();
}
