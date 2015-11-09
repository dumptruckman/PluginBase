/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.minecraft;

import pluginbase.messages.messaging.MessageReceiver;
import pluginbase.permission.Perm;
import pluginbase.permission.Permissible;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a player/user currently on a Minecraft server.
 * <p/>
 * It may not always represent an actual player but could also represent a console user.  {@link #isPlayer()} is used
 * to determine this difference.
 */
public abstract class BasePlayer implements MessageReceiver, Permissible {

    /** {@inheritDoc} */
    @Override
    public boolean equals(@Nullable final Object other) {
        if (!(other instanceof BasePlayer)) {
            return false;
        }
        final BasePlayer other2 = (BasePlayer) other;
        return other2.getName().equals(getName());
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isPlayer() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasPerm(@NotNull final Perm perm) {
        return hasPermission(perm.getName());
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasPerm(@NotNull final Perm perm, @NotNull final String specific) {
        return hasPermission(perm.getName(specific));
    }

    @Override
    public String toString() {
        return getName();
    }
}
