/*
 * WorldEdit
 * Copyright (C) 2011 sk89q <http://www.sk89q.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * Copied and modified on March 9, 2013 by dumptruckman.
*/
package pluginbase.bukkit.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.Plugin;
import pluginbase.messages.messaging.Messaging;

import java.util.Arrays;
import java.util.List;

class DynamicPluginCommand<P extends Messaging> extends org.bukkit.command.Command implements PluginIdentifiableCommand {

    protected final TabExecutor executor;
    protected final P registeredWith;
    protected final Plugin owningPlugin;
    protected String[] permissions = new String[0];

    DynamicPluginCommand(String[] aliases, String desc, String usage, TabExecutor executor, P registeredWith, Plugin plugin) {
        super(aliases[0], desc, usage, Arrays.asList(aliases));
        this.executor = executor;
        this.owningPlugin = plugin;
        this.registeredWith = registeredWith;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        return executor.onCommand(sender, this, label, args);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return executor.onTabComplete(sender, this, alias, args);
    }

    public void setPermissions(String[] permissions) {
        this.permissions = permissions;
        if (permissions != null) {
            super.setPermission(StringUtil.joinString(permissions, ";"));
        }
    }

    @Override
    public Plugin getPlugin() {
        return owningPlugin;
    }

    @Override
    public boolean testPermissionSilent(CommandSender sender) {
        if (permissions == null || permissions.length == 0) {
            return true;
        }

        for (String permission : permissions) {
            if (sender.hasPermission(permission)) {
                return true;
            }
        }
        return false;
    }
}
