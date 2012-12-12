/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.messaging;

import org.bukkit.util.ChatPaginator;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * A Bukkit specific implementation of {@link Messager}.
 *
 * Provides word wrapping on messages too long to fit on one line.
 */
public class BukkitMessager extends SimpleMessager {

    public BukkitMessager(@NotNull File dataFolder) {
        super(dataFolder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void message(@NotNull MessageReceiver sender, @NotNull String message) {
        sendMessages(sender, ChatPaginator.wordWrap(message, ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH));
    }
}

