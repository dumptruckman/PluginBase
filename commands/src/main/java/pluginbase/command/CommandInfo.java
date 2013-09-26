package pluginbase.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation must be used on all commands to help define them and show how they are used.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInfo {

    /**
     * The primary alias of the command.
     * <p/>
     * This shows the primary string required to execute the command.  By default this string must be prefixed by the
     * {@link pluginbase.command.CommandProvider#getCommandPrefix()} followed by a space
     * UNLESS {@link #prefixPrimary()} is set to false.
     * <p/>
     * Additionally if {@link #directlyPrefixPrimary()} is set to true, the space between the command prefix and this
     * string is not required.
     *
     * @return the primary alias of the command.
     */
    String primaryAlias();

    /**
     * Whether the {@link pluginbase.command.CommandProvider#getCommandPrefix()} is required
     * before the primary alias.
     *
     * @return true if the {@link pluginbase.command.CommandProvider#getCommandPrefix()} is
     * required before the primary alias.
     */
    boolean prefixPrimary() default true;

    /**
     * Whether the primary alias is directly (no space) prefixed by the {@link pluginbase.command.CommandProvider#getCommandPrefix()}.
     * <p/>
     * If {@link #prefixPrimary()} is false, this setting is ignored.
     *
     * @return true if the primary alias must be directly prefixed by the {@link pluginbase.command.CommandProvider#getCommandPrefix()}.
     */
    boolean directlyPrefixPrimary() default false;

    /**
     * These are alternate aliases for this command with no predefined prefix.
     * <p/>
     * These aliases must be entered by the user exactly as shown here to active this command.
     *
     * @return alternate aliases for this command.
     */
    String[] aliases() default "";

    /**
     * These are alternate aliases for this command that utilize the {@link pluginbase.command.CommandProvider#getCommandPrefix()} followed by a space.
     * <p/>
     * These aliases must be entered by the user following the command prefix and a space to activate this command.
     *
     * @return alternate aliases for this command that utilize the {@link pluginbase.command.CommandProvider#getCommandPrefix()} followed by a space.
     */
    String[] prefixedAliases() default "";

    /**
     * These are alternate aliases for this command that utilize the {@link pluginbase.command.CommandProvider#getCommandPrefix()} with no space.
     * <p/>
     * These aliases must be entered by the user directly following the command prefix to activate this command.
     *
     * @return alternate aliases for this command that utilize the {@link pluginbase.command.CommandProvider#getCommandPrefix()} with no space.
     */
    String[] directlyPrefixedAliases() default "";

    /**
     * Describes the usage of this command beyond just the alias.
     * <p/>
     * This should demonstrate only the parameters that are used for the command.
     * <p/>
     * example: {@code <required> [optional 1] [optional 2]}
     *
     * @return the usage of this command beyond just the alias.
     */
    String usage() default "";

    /**
     * A short description of this command.
     *
     * @return a short description of this command.
     */
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
     * Gets the flags for the command.
     * <p/>
     * Flags allow special processing for flags such as -h in the command,
     * allowing users to easily turn on a flag.
     * <p/>
     * This is a string with each character being a flag. Use A-Z and a-z as possible flags.
     *  <p/>
     * Appending a flag with a : makes the flag character before a value flag,
     * meaning that if it is given it must have a value
     *
     * @return Flags matching a-zA-Z
     */
    String flags() default "";

    /**
     * Indicates whether any flag can be provided to the command regardless of it being specified in {@link #flags()}.
     *
     * @return Whether any flag can be provided to the command, even if it is not in {@link #flags()}
     */
    boolean anyFlags() default false;
}
