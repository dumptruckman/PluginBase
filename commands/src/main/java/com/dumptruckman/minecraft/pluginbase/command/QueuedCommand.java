/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.command;

import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import com.dumptruckman.minecraft.pluginbase.messages.BundledMessage;
import com.dumptruckman.minecraft.pluginbase.messages.messaging.Messaging;
import com.dumptruckman.minecraft.pluginbase.minecraft.BasePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Allows for a command to be queued and require to be confirmed before executing.
 */
public abstract class QueuedCommand<P extends CommandProvider & Messaging> extends Command<P> implements Runnable {

    protected QueuedCommand(@NotNull final P plugin) {
        super(plugin);
    }

    private BasePlayer sender;
    private CommandContext context;

    /**
     * This method should return a user friendly message explaining that this command
     * requires confirmation using the appropriate confirm command.
     * <p/>
     * The confirm command is "/" + {@link com.dumptruckman.minecraft.pluginbase.command.CommandProvider#getCommandPrefix()} + "confirm" by default.
     *
     * @return The confirm required message or null to use the default {@link CommandHandler#MUST_CONFIRM}.
     */
    @Nullable
    protected abstract BundledMessage getConfirmMessage();

    final void confirm() {
        Logging.finer("Confirming queued command '%s' for '%s' with '%s'", this, sender, context);
        onConfirm(sender, context);
        getPlugin().getCommandHandler().removedQueuedCommand(sender, this);
    }

    private void expire() {
        Logging.finer("Expiring queued command '%s' for '%s' with '%s'", this, sender, context);
        onExpire(sender, context);
        getPlugin().getCommandHandler().removedQueuedCommand(sender, this);
    }

    public abstract long getExpirationDuration();

    protected abstract boolean preConfirm(@NotNull final BasePlayer sender, @NotNull final CommandContext context);

    protected abstract void onConfirm(@NotNull final BasePlayer sender, @NotNull final CommandContext context);

    protected abstract void onExpire(@NotNull final BasePlayer sender, @NotNull final CommandContext context);

    @Override
    public final boolean runCommand(@NotNull final BasePlayer sender, @NotNull final CommandContext context) {
        this.sender = sender;
        this.context = context;
        getPlugin().scheduleQueuedCommandExpiration(this);
        return preConfirm(sender, context);
    }

    @Override
    public void run() {
        expire();
    }
}
