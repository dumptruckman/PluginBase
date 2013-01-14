package com.dumptruckman.minecraft.pluginbase.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInfo {

    String primaryAlias();

    boolean prefixPrimary() default true;

    boolean directlyPrefixPrimary() default false;

    String[] aliases() default "";

    String[] prefixedAliases() default "";

    String[] directlyPrefixedAliases() default "";

    String usage() default "";

    String desc();

    /**
     * The minimum number of arguments. This should be 0 or above.
     *
     * @return the minimum number of arguments
     */
    int min() default 0;

    /**
     * The maximum number of arguments. Use -1 for an unlimited number
     * of arguments.
     *
     * @return the maximum number of arguments
     */
    int max() default -1;

    /**
     * Flags allow special processing for flags such as -h in the command,
     * allowing users to easily turn on a flag. This is a string with
     * each character being a flag. Use A-Z and a-z as possible flags.
     * Appending a flag with a : makes the flag character before a value flag,
     * meaning that if it is given it must have a value
     *
     * @return Flags matching a-zA-Z
     */
    String flags() default "";

    /**
     *
     *
     * @return Whether any flag can be provided to the command, even if it is not in {@link #flags()}
     */
    boolean anyFlags() default false;
}
