/******************************************************************************
 * Multiverse 2 Copyright (c) the Multiverse Team 2011.                       *
 * Multiverse 2 is licensed under the BSD License.                            *
 * For more information please check the README.md file included              *
 * with this project.                                                         *
 ******************************************************************************/

package com.dumptruckman.minecraft.plugin.command;

import com.dumptruckman.minecraft.locale.CommandMessages;
import com.dumptruckman.minecraft.permission.Perm;
import com.dumptruckman.minecraft.plugin.AbstractBukkitPlugin;
import com.pneumaticraft.commandhandler.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays a nice help menu.
 */
public class HelpCommand<P extends AbstractBukkitPlugin> extends PaginatedPluginCommand<Command, P> {

    public HelpCommand(P plugin) {
        super(plugin);
        this.setName(messager.getMessage(CommandMessages.HELP_NAME, plugin.getPluginName()));
        this.setCommandUsage(messager.getMessage(CommandMessages.HELP_USAGE, plugin.getCommandPrefixes().get(0)));
        this.setArgRange(0, 2);
        for (String prefix : (List<String>) plugin.getCommandPrefixes()) {
            this.addKey(prefix);
            this.addKey(prefix + "h");
            this.addKey(prefix + "help");
            this.addKey(prefix + " help");
            this.addKey(prefix + "search");
            this.addKey(prefix + " search");
        }
        this.addCommandExample("/" + plugin.getCommandPrefixes().get(0) + " help ?");
        this.setPermission(Perm.COMMAND_HELP.getPermission());
        this.setItemsPerPage(7); // SUPPRESS CHECKSTYLE: MagicNumberCheck
    }

    @Override
    protected List<Command> getFilteredItems(List<Command> availableItems, String filter) {
        List<Command> filtered = new ArrayList<Command>();

        for (Command c : availableItems) {
            if (stitchThisString(c.getKeyStrings()).matches("(?i).*" + filter + ".*")) {
                filtered.add(c);
            } else if (c.getCommandName().matches("(?i).*" + filter + ".*")) {
                filtered.add(c);
            } else if (c.getCommandDesc().matches("(?i).*" + filter + ".*")) {
                filtered.add(c);
            } else if (c.getCommandUsage().matches("(?i).*" + filter + ".*")) {
                filtered.add(c);
            } else {
                for (String example : c.getCommandExamples()) {
                    if (example.matches("(?i).*" + filter + ".*")) {
                        filtered.add(c);
                        break;
                    }
                }
            }
        }
        return filtered;
    }

    @Override
    protected String getItemText(Command item) {
        return ChatColor.AQUA + item.getCommandUsage();
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        messager.normal(CommandMessages.HELP_TITLE, sender, plugin.getPluginName());

        FilterObject filterObject = this.getPageAndFilter(args);

        List<Command> availableCommands = new ArrayList<Command>(this.plugin.getCommandHandler().getCommands(sender));
        if (filterObject.getFilter().length() > 0) {
            availableCommands = this.getFilteredItems(availableCommands, filterObject.getFilter());
            if (availableCommands.size() == 0) {
                messager.normal(CommandMessages.HELP_NONE_FOUND, sender, filterObject.getFilter());
                return;
            }
        }

        if (!(sender instanceof Player)) {
            messager.normal(CommandMessages.HELP_MORE_INFO, sender);
            for (Command c : availableCommands) {
                sender.sendMessage(ChatColor.AQUA + c.getCommandUsage());
            }
            return;
        }

        int totalPages = (int) Math.ceil(availableCommands.size() / (this.itemsPerPage + 0.0));

        if (filterObject.getPage() > totalPages) {
            filterObject.setPage(totalPages);
        }

        messager.normal(CommandMessages.HELP_PAGES, sender, filterObject.getPage(), totalPages);
        messager.normal(CommandMessages.HELP_MORE_INFO, sender);

        this.showPage(filterObject.getPage(), sender, availableCommands);
    }
}
