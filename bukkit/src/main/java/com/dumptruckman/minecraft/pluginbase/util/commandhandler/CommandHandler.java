package com.dumptruckman.minecraft.pluginbase.util.commandhandler;

import com.dumptruckman.minecraft.pluginbase.locale.BundledMessage;
import com.dumptruckman.minecraft.pluginbase.locale.CommandMessages;
import com.dumptruckman.minecraft.pluginbase.plugin.BukkitPlugin;
import com.dumptruckman.minecraft.pluginbase.plugin.command.QueuedPluginCommand;
import com.dumptruckman.minecraft.pluginbase.util.shellparser.ShellParser;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CommandHandler {

    protected BukkitPlugin plugin;

    protected Map<CommandSender, QueuedCommand> queuedCommands;
    protected List<Command> allCommands;

    protected PermissionsInterface permissions;

    public CommandHandler(BukkitPlugin plugin, PermissionsInterface permissions) {
        this.plugin = plugin;

        this.allCommands = new ArrayList<Command>();
        this.queuedCommands = new HashMap<CommandSender, QueuedCommand>();
        this.permissions = permissions;
    }

    public List<Command> getCommands(CommandSender sender) {
        List<Command> permissiveCommands = new ArrayList<Command>();
        for (Command c : this.allCommands) {
            if (this.permissions.hasAnyPermission(sender, c.getAllPermissionStrings(), c.isOpRequired())) {
                permissiveCommands.add(c);
            }
        }
        return permissiveCommands;
    }

    public List<Command> getAllCommands() {
        return this.allCommands;
    }

    public boolean locateAndRunCommand(CommandSender sender, List<String> args) {
        return this.locateAndRunCommand(sender, args, true); // Notify sender by default
    }

    public boolean locateAndRunCommand(CommandSender sender, List<String> args, boolean notifySender) {
        List<String> parsedArgs = parseAllQuotedStrings(args);
        CommandKey key = null;

        Iterator<Command> iterator = this.allCommands.iterator();
        Command foundCommand = null;
        // Initialize a list of all commands that match:
        List<Command> foundCommands = new ArrayList<Command>();
        List<CommandKey> foundKeys = new ArrayList<CommandKey>();

        while (iterator.hasNext()) {
            foundCommand = iterator.next();
            key = foundCommand.getKey(parsedArgs);
            if (key != null) {
                foundCommands.add(foundCommand);
                foundKeys.add(key);
            }
        }

        processFoundCommands(foundCommands, foundKeys, sender, parsedArgs, notifySender);
        return true;
    }

    private void processFoundCommands(List<Command> foundCommands, List<CommandKey> foundKeys, CommandSender sender, List<String> parsedArgs) {
        this.processFoundCommands(foundCommands, foundKeys, sender, parsedArgs, true); // Notify sender by default
    }

    /**
     * The purpose of this method is to determine the most specific command matching the args and execute it.
     *
     * @param foundCommands A list of all matching commands.
     * @param foundKeys     A list of the key that was matched the command.
     * @param sender        The sender of the original command
     * @param parsedArgs    The arguments who have been combined, ie: "The world" is one argument
     * @param notifySender  Whether to send optional messages to the command sender
     */
    private void processFoundCommands(List<Command> foundCommands, List<CommandKey> foundKeys, CommandSender sender, List<String> parsedArgs, boolean notifySender) {

        if (foundCommands.size() == 0) {
            return;
        }
        Command bestMatch = null;
        CommandKey matchingKey = null;
        int bestMatchInt = 0;

        for (int i = 0; i < foundCommands.size(); i++) {
            List<String> parsedCopy = new ArrayList<String>(parsedArgs);
            foundCommands.get(i).removeKeyArgs(parsedCopy, foundKeys.get(i).getKey());

            if (foundCommands.get(i).getNumKeyArgs(foundKeys.get(i).getKey()) > bestMatchInt) {
                bestMatch = foundCommands.get(i);
                matchingKey = foundKeys.get(i);
                bestMatchInt = bestMatch.getNumKeyArgs(matchingKey.getKey());
            } else if (foundCommands.get(i).getNumKeyArgs(foundKeys.get(i).getKey()) == bestMatchInt && (foundKeys.get(i).hasValidNumberOfArgs(parsedCopy.size()))) {
                // If the number of matched items was the same as a previous one
                // AND the new one has a valid number of args, it will be accepted
                // and will replace the previous one as the best command.
                bestMatch = foundCommands.get(i);
                matchingKey = foundKeys.get(i);
            }
        }

        if (bestMatch != null) {
            bestMatch.removeKeyArgs(parsedArgs, matchingKey.getKey());
            // Special case:
            // If the ONLY param is a '?' show them the usage.
            if (parsedArgs.size() == 1 && parsedArgs.get(0).equals("?") && this.permissions.hasAnyPermission(sender, bestMatch.getAllPermissionStrings(), bestMatch.isOpRequired())) {
                bestMatch.showHelp(sender);
            } else {
                checkAndRunCommand(sender, parsedArgs, bestMatch, notifySender);
            }
        }
    }

    public void registerCommand(Command command) {
        this.allCommands.add(command);
    }

    /**
     * Combines all quoted strings
     *
     * @param args
     *
     * @return
     */
    private List<String> parseAllQuotedStrings(List<String> args) {
        StringBuilder arg = null;
        if (args.size() == 0) {
            arg = new StringBuilder("");
        } else {
            arg = new StringBuilder(args.get(0));
            for (int i = 1; i < args.size(); i++) {
                arg.append(" ").append(args.get(i));
            }
        }

        List<String> result = ShellParser.safeParseString(arg.toString());
        if (result == null) {
            return new ArrayList<String>();
        } else {
            return result;
        }
    }

    /**
     * "The command " + ChatColor.RED + commandName + ChatColor.WHITE + " has been halted due to the fact that it could
     * break something!" "If you still wish to execute " + ChatColor.RED + commandName + ChatColor.WHITE
     */
    public void queueCommand(CommandSender sender, QueuedPluginCommand command, List<String> args, BundledMessage confirmMessage, int confirmWait) {
        this.queuedCommands.put(sender, new QueuedCommand(command, args, System.currentTimeMillis() + (confirmWait * 1000)));

        String commandName = command.getKeyStrings().get(0);
        String confirmCommand = plugin.getCommandPrefixes().get(0) + " confirm";

        if (confirmMessage == null) {
            confirmMessage = new BundledMessage(CommandMessages.CONFIRM_MESSAGE, commandName);
        }
        plugin.getMessager().normal(confirmMessage, sender);
        plugin.getMessager().normal(CommandMessages.CONFIRM_MESSAGE_2, sender, confirmCommand, confirmWait);
    }

    public void queueCommand(CommandSender sender, QueuedPluginCommand command, List<String> args, BundledMessage confirmMessage) {
        this.queueCommand(sender, command, args, confirmMessage, 10);
    }

    /**
     * Tries to fire off the command
     *
     * @param sender
     *
     * @return
     */
    public void confirmQueuedCommand(CommandSender sender) {
        QueuedCommand command = this.queuedCommands.get(sender);
        if (command == null) {
            plugin.getMessager().normal(CommandMessages.QUEUED_NONE, sender);
            return;
        }
        if (System.currentTimeMillis() > command.getExpirationTime()) {
            plugin.getMessager().bad(CommandMessages.QUEUED_EXPIRED, sender);
            command.getCommand().onExpire(sender, command.getArgs());
            this.queuedCommands.remove(sender);
        } else {
            command.getCommand().onConfirm(sender, command.getArgs());
            this.queuedCommands.remove(sender);
        }
    }

    /**
     * Returns the given flag value
     *
     * @param flag A param flag, like -s or -g
     * @param args All arguments to search through
     *
     * @return A string with flag data or null if no flag/flag data
     */
    public static String getFlag(String flag, List<String> args) {
        int i = 0;
        for (String s : args) {
            if (s.equalsIgnoreCase(flag)) {
                if (args.size() > i + 1) {
                    return args.get(i + 1);
                } else {
                    return null;
                }
            }
            i++;
        }
        return null;
    }

    public static boolean hasFlag(String flag, List<String> args) {
        for (String s : args) {
            if (s.equalsIgnoreCase(flag)) {
                return true;
            }
        }
        return false;
    }

    private void checkAndRunCommand(CommandSender sender, List<String> parsedArgs, Command foundCommand, boolean notifySender) {
        if (this.permissions.hasAnyPermission(sender, foundCommand.getAllPermissionStrings(), foundCommand.isOpRequired())) {
            if (foundCommand.checkArgLength(parsedArgs)) {
                foundCommand.runCommand(sender, parsedArgs);
            } else {
                foundCommand.showHelp(sender);
            }
        } else {
            if(notifySender) {
                sender.sendMessage("You do not have any of the required permission(s):");
                for (String perm : foundCommand.getAllPermissionStrings()) {
                    sender.sendMessage(" - \u00a7a" + perm);
                }
            }
        }
    }

    private void checkAndRunCommand(CommandSender sender, List<String> parsedArgs, Command foundCommand) {
        this.checkAndRunCommand(sender, parsedArgs, foundCommand, true); // Notify sender by default
    }
}
