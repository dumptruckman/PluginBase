package com.dumptruckman.minecraft.pluginbase.messages;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The main exception class for PluginBase.
 * <p/>
 * This exception allows for localized messages via {@link BundledMessage}.
 */
public class PluginBaseException extends Exception {

    @NotNull
    private final BundledMessage languageMessage;

    @Nullable
    private PluginBaseException cause;

    /**
     * Constructs a PluginBase exception with the given bundled message.
     *
     * @param languageMessage the bundled message to use for this exception.
     */
    public PluginBaseException(@NotNull final BundledMessage languageMessage) {
        super(String.format(ChatColor.translateAlternateColorCodes('&', languageMessage.getMessage().getDefault()), languageMessage.getArgs()));
        this.languageMessage = languageMessage;
    }

    /**
     * Constructs a PluginBase exception with the given bundled message.
     *
     * @param languageMessage the bundled message to use for this exception.
     */
    public PluginBaseException(@NotNull final BundledMessage languageMessage, @NotNull final Throwable throwable) {
        super(String.format(ChatColor.translateAlternateColorCodes('&', languageMessage.getMessage().getDefault()), languageMessage.getArgs()), throwable);
        this.languageMessage = languageMessage;
    }

    /**
     * Creates a PluginBase exception with the given bundled message that was caused by another PluginBase exception.
     * <p/>
     * This other exception will be retrievable via {@link #getCauseException()} and {@link #getCause()}.
     *
     * @param languageMessage the bundled message to use for this exception.
     * @param cause the cause exception.
     */
    public PluginBaseException(@NotNull final BundledMessage languageMessage, @NotNull final PluginBaseException cause) {
        this(languageMessage, (Throwable) cause);
        this.cause = cause;
    }

    /**
     * Copy constructor for PluginBase exceptions.
     *
     * @param e The exception to copy.
     */
    public PluginBaseException(@NotNull final PluginBaseException e) {
        this(e.getBundledMessage(), e.getCause());
    }

    /**
     * Gets the {@link BundledMessage} used this in exception.
     *
     * @return The {@link BundledMessage} used this in exception.
     */
    public BundledMessage getBundledMessage() {
        return this.languageMessage;
    }

    /**
     * Gets the PluginBase exception that caused this exception if any.
     *
     * @return the PluginBase exception that caused this exception if any.
     */
    @Nullable
    public PluginBaseException getCauseException() {
        return this.cause;
    }
}
