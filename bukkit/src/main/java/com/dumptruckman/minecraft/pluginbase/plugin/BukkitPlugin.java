/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.plugin;

import com.dumptruckman.minecraft.pluginbase.entity.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.messaging.BukkitMessager;
import com.dumptruckman.minecraft.pluginbase.plugin.command.BukkitCommandHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface BukkitPlugin extends PluginBase, Plugin {

    BukkitMessager getMessager();

    BukkitCommandHandler getCommandHandler();

    BasePlayer wrapPlayer(Player player);

    BasePlayer wrapSender(CommandSender sender);

    String getPermissionName();
}
