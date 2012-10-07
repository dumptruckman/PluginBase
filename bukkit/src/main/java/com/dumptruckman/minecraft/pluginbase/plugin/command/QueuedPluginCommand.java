/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.plugin.command;

import com.dumptruckman.minecraft.pluginbase.locale.BundledMessage;
import com.dumptruckman.minecraft.pluginbase.plugin.AbstractBukkitPlugin;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Allows for a command to be queued and require a confirm command.
 */
public abstract class QueuedPluginCommand<P extends AbstractBukkitPlugin> extends PluginCommand<P> {

    private BundledMessage confirmMessage = null;
    private Map<CommandSender, Object> dataMap = new HashMap<CommandSender, Object>();

    public QueuedPluginCommand(P plugin) {
        super(plugin);
    }

    public final void setConfirmMessage(BundledMessage message) {
        this.confirmMessage = message;
    }

    @Override
    public final void runCommand(CommandSender sender, List<String> args) {
        preConfirm(sender, args);
        getPlugin().getCommandHandler().queueCommand(sender, this, args, confirmMessage);
    }

    public abstract void preConfirm(CommandSender sender, List<String> args);

    public abstract void onConfirm(CommandSender sender, List<String> args);

    public abstract void onExpire(CommandSender sender, List<String> args);

    public Object getData(CommandSender sender) {
        return dataMap.get(sender);
    }

    public void setData(CommandSender sender, Object data) {
        dataMap.put(sender, data);
    }

    public void clearData(CommandSender sender) {
        dataMap.remove(sender);
    }
}
