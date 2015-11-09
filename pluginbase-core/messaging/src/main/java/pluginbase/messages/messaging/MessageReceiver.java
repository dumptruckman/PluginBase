/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pluginbase.messages.messaging;

import org.jetbrains.annotations.NotNull;

/**
 * An entity that can receive messages sent by {@link Messager}.
 */
public interface MessageReceiver {

    /**
     * Gets the name of this MessageReceiver.
     *
     * @return the name of this MessageReceiver.
     */
    @NotNull
    String getName();

    /**
     * Sends the message to the this MessageReceiver.
     *
     * @param message the message to send.
     */
    void sendMessage(@NotNull final String message);

    /**
     * Checks if this MessageReceiver is a player currently on the Minecraft server.
     *
     * @return true if this MessageReceiver is a player currently on the Minecraft server.
     */
    boolean isPlayer();
}
