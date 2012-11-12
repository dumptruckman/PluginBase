package com.dumptruckman.minecraft.pluginbase.messaging;

/**
 * The main exception class for PluginBase.  This exception allows for localized messages via {@link BundledMessage}.
 */
public class PluginBaseException extends Exception {

    private final BundledMessage languageMessage;

    public PluginBaseException(BundledMessage b) {
        super(String.format(ChatColor.translateAlternateColorCodes('&', b.getMessage().getDefault().get(0)), b.getArgs()));
        this.languageMessage = b;
    }

    public PluginBaseException(BundledMessage b, Throwable throwable) {
        super(String.format(ChatColor.translateAlternateColorCodes('&', b.getMessage().getDefault().get(0)), b.getArgs()), throwable);
        this.languageMessage = b;
    }

    /**
     * Gets the {@link BundledMessage} used this in exception.
     *
     * @return The {@link BundledMessage} used this in exception.
     */
    public BundledMessage getBundledMessage() {
        return this.languageMessage;
    }
}
