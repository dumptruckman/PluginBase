package pluginbase.command;

import pluginbase.messages.BundledMessage;
import pluginbase.messages.PluginBaseException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A more specific command exception used for when the usage of a command is incorrect.
 */
public class CommandUsageException extends CommandException {

    private final List<String> usage;

    public CommandUsageException(@NotNull final BundledMessage languageMessage, final List<String> usage) {
        super(languageMessage);
        this.usage = usage;
    }

    public CommandUsageException(@NotNull final BundledMessage languageMessage, @NotNull final Throwable throwable, final List<String> usage) {
        super(languageMessage, throwable);
        this.usage = usage;
    }

    public CommandUsageException(@NotNull final BundledMessage languageMessage, @NotNull final PluginBaseException cause, final List<String> usage) {
        super(languageMessage, cause);
        this.usage = usage;
    }

    public CommandUsageException(@NotNull final PluginBaseException e, final List<String> usage) {
        super(e);
        this.usage = usage;
    }

    /**
     * Gets the usage text of the command throwing the exception.
     *
     * @return the usage text of the command throwing the exception.
     */
    public List<String> getUsage() {
        return usage;
    }
}
