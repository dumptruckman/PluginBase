/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.command;

import pluginbase.messages.BundledMessage;
import pluginbase.messages.Message;
import pluginbase.minecraft.BasePlayer;
import org.jetbrains.annotations.NotNull;
import pluginbase.util.time.Duration;

/**
 * A PluginBase user queued command that requires confirmation before executing.
 * <p/>
 * This is for commands to be used on the server by the server operator or the players on the server.
 * A queued command waits for the user to execute a confirmation command before performing the actual
 * command.  A queued command will have an expiration time after which it will stop waiting for the confirm
 * command and simply not run.  A queued command that is expired must be typed in again and confirmed.
 * <br/>
 * Queued commands for PluginBase's command handler <b>must</b> implement this class AND annotate it with the
 * {@link CommandInfo} annotation.
 *
 * @param <P> the plugin that this command belongs to.
 */
public abstract class QueuedCommand<P> extends Command<P> implements Runnable {

    /**
     * Constructs a queued command.
     * <p/>
     * You will never need to call this constructor.  It is used by {@link CommandHandler}
     *
     * @param plugin your plugin.
     */
    protected QueuedCommand(@NotNull final CommandProvider<P> plugin) {
        super(plugin);
    }

    private BasePlayer sender;
    private CommandContext context;

    /**
     * This method should return a user friendly message explaining that this command
     * requires confirmation using the appropriate confirm command.
     * <p/>
     * The confirm command is "/" + {@link pluginbase.command.CommandProvider#getCommandPrefix()} + " confirm" by default.
     * <br/>
     * This returns a built in message and must be overridden to provide a custom message.
     *
     * @return The confirm required message.
     */
    @NotNull
    protected BundledMessage getConfirmMessage() {
        return CommandHandler.MUST_CONFIRM.bundle(Duration.valueOf(getExpirationDuration()).asVerboseString());
    }

    final void confirm() {
        getCommandProvider().getLog().finer("Confirming queued command '%s' for '%s' with '%s'", this, sender, context);
        onConfirm(sender, context);
        getCommandProvider().getCommandHandler().removedQueuedCommand(sender, this);
    }

    private void expire() {
        getCommandProvider().getLog().finer("Expiring queued command '%s' for '%s' with '%s'", this, sender, context);
        onExpire(sender, context);
        getCommandProvider().getCommandHandler().removedQueuedCommand(sender, this);
    }

    /**
     * How long the queued command will wait for confirmation before expiring.
     *
     * @return the expiration time in seconds.
     */
    public abstract long getExpirationDuration();

    /**
     * Gives the command a chance to do something before the confirm command has been used.
     * <p/>
     * This is called internally and must simply be implemented.  It is okay for this method to be blank.
     *
     * @param sender the command sender.
     * @param context the command context containing details about the command's usage.
     * @return true to indicate the command was used as indicated and false to indicate improper usage.
     */
    protected abstract boolean preConfirm(@NotNull final BasePlayer sender, @NotNull final CommandContext context);

    /**
     * This runs the actual command once it is confirmed.
     * <p/>
     * Implement with what the command should actually do.
     *
     * @param sender the command sender.
     * @param context the command context containing details about the command's usage.
     */
    protected abstract void onConfirm(@NotNull final BasePlayer sender, @NotNull final CommandContext context);

    /**
     * Gives the command a chance to do something when the user has missed the confirmation window.
     * <p/>
     * This is called internally and must simply be implemented.  It is okay for this method to be blank.
     * <br/>
     * If you desire for the command sender to know when the command has expired, you must use this to send a
     * message as no other warning is given.
     *
     * @param sender the command sender.
     * @param context the command context containing details about the command's usage.
     */
    protected abstract void onExpire(@NotNull final BasePlayer sender, @NotNull final CommandContext context);

    /** {@inheritDoc} */
    @Override
    public final boolean runCommand(@NotNull final BasePlayer sender, @NotNull final CommandContext context) throws CommandException {
        this.sender = sender;
        this.context = context;
        getCommandProvider().scheduleQueuedCommandExpiration(this);
        return preConfirm(sender, context);
    }

    /**
     * This will cause the expiration of the queued command.
     * <p/>
     * This is mostly for use internally.
     */
    @Override
    public final void run() {
        expire();
    }
}
