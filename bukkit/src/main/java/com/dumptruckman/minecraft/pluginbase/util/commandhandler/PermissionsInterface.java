/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.dumptruckman.minecraft.pluginbase.util.commandhandler;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface PermissionsInterface {
    public boolean hasPermission(CommandSender sender, String node, boolean isOpRequired);

    public boolean hasAnyPermission(CommandSender sender, List<String> allPermissionStrings, boolean opRequired);

    public boolean hasAllPermission(CommandSender sender, List<String> allPermissionStrings, boolean opRequired);
}
