package pluginbase.command;

import org.jetbrains.annotations.NotNull;
import pluginbase.logging.LoggablePlugin;
import pluginbase.messages.messaging.Messaging;

/**
 * An interface required for using the commands provided by PluginBase.
 *
 * Indicates that commands will be provided.
 *
 * @param <P> the plugin that this command provider belongs to.
 */
public interface CommandProvider<P> extends Messaging, LoggablePlugin {

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

    /**
     * Provides additional aliases that commands should use.
     * <p/>
     * This is useful for when the CommandProvider does not have access to the Command class in order to add them normally.
     *
     * @return an array of additional command aliases.
     */
    @NotNull
    String[] getAdditionalCommandAliases(@NotNull Class<? extends Command> commandClass);

    /**
     * Adds an additional alias to a command.  Any amount may be added by calling this method multiple times.
     * <p/>
     * This must be called before command registration occurs!
     *
     * @param commandClass the command class to add aliases for.
     * @param alias the alias to add.
     */
    void addCommandAlias(@NotNull Class<? extends Command> commandClass, @NotNull String alias);

    /**
     * Gets the plugin that this command provider belongs to.
     *
     * @return the plugin that this command provider belongs to.
     */
    P getPlugin();
}
