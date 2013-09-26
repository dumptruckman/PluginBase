package pluginbase.command;

import org.jetbrains.annotations.NotNull;

/**
 * An interface required for using the commands provided by PluginBase.
 *
 * Indicates that commands will be provided.
 */
public interface CommandProvider {

    /**
     * Gets the prefix for commands used by this CommandProvider.
     * <p/>
     * This prefix will be use on all command primary aliases but is optional on alternate aliases.
     *
     * @return the prefix for commands used by this CommandProvider.
     */
    @NotNull
    String getCommandPrefix();

    /**
     * Gets the handler object for commands provided by this CommandProvider.
     *
     * @return the handler object for commands provided by this CommandProvider.
     */
    @NotNull
    CommandHandler getCommandHandler();

    /**
     * Schedules a queued command to be run later in order to deal with it's expiration when left unconfirmed.
     * <p/>
     * This method should simply run the QueuedCommand (which implements {@link Runnable}) an amount of seconds later.
     * The amount is specified with {@link pluginbase.command.QueuedCommand#getExpirationDuration()}.
     * <p/>
     * This will automatically be called by the command handler when the queued command is used initially in order to
     * schedule its expiration.
     * </p>
     * If {@link #useQueuedCommands()} returns false this method can happily do nothing.
     *
     * @param queuedCommand the queued command to schedule expiration for.
     */
    void scheduleQueuedCommandExpiration(@NotNull final QueuedCommand queuedCommand);

    /**
     * Whether or not the command provider offers queued commands.
     * <p/>
     * This is important to note as it determines whether or not the provider should have a built in confirm command.
     * <p/>
     * Simply implement this method to return true to offer queued commands.
     *
     * @return true means this provider offers queued commands and has a built in confirm command.
     */
    boolean useQueuedCommands();
}
