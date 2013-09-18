package com.dumptruckman.minecraft.pluginbase.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Virtual {
    /**
     * If this property is a {@link me.main__.util.SerializationConfig.VirtualProperty}{@code <TYPE>} you have
     * to set this to {@code TYPE.class} because Java generics somehow suck.
     */
    Class<?> type() default Object.class;
    /**
     * If the field with this annotation is a {@link me.main__.util.SerializationConfig.VirtualProperty} you can set
     * this to {@code true} if you want the property to be saved in the config.
     */
    boolean persist() default false;
}
