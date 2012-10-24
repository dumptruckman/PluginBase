package com.dumptruckman.minecraft.pluginbase.plugin.command;

import com.dumptruckman.minecraft.pluginbase.entity.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.exception.CommandUsageException;
import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;
import com.dumptruckman.minecraft.pluginbase.plugin.command.builtin.BuiltInCommand;
import com.dumptruckman.minecraft.pluginbase.util.Logging;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class CommandHandler<P extends PluginBase> {

    protected final P plugin;
    protected final Map<String, String> commandMap;

    private final Map<String, CommandKey> commandKeys = new HashMap<String, CommandKey>();

    public CommandHandler(P plugin) {
        this.plugin = plugin;
        this.commandMap = new HashMap<String, String>();
    }

    //public boolean registerCommmands(String packageName) {

    //}

    public boolean registerCommand(Class<? extends Command> commandClass) {
        final CommandInfo cmdInfo = commandClass.getAnnotation(CommandInfo.class);
        if (cmdInfo == null) {
            throw new IllegalArgumentException("Command must be annotated with @CommandInfo");
        }
        final Command command = loadCommand(commandClass);
        if (command == null) {
            Logging.severe("Could not register: " + commandClass);
            return false;
        }

        final List<String> aliases;
        if (command instanceof BuiltInCommand) {
            aliases = new ArrayList<String>(cmdInfo.aliases().length + cmdInfo.prefixedAliases().length
                    + cmdInfo.directlyPrefixedAliases().length + ((BuiltInCommand) command).getStaticAliases().size()
                    + 1);
        } else {
            aliases = new ArrayList<String>(cmdInfo.aliases().length + cmdInfo.prefixedAliases().length
                    + cmdInfo.directlyPrefixedAliases().length + 1);
        }
        if (cmdInfo.directlyPrefixPrimary()) {
            aliases.add(plugin.getCommandPrefix() + cmdInfo.primaryAlias());
        } else if (cmdInfo.prefixPrimary())  {
            aliases.add(plugin.getCommandPrefix() + " " + cmdInfo.primaryAlias());
        } else {
            aliases.add(cmdInfo.primaryAlias());
        }
        if (commandMap.containsKey(aliases.get(0))) {
            throw new IllegalArgumentException("Command with the same primary alias has already been registered!");
        }
        for (final String alias : cmdInfo.aliases()) {
            if (!alias.isEmpty()) {
                aliases.add(alias);
            }
        }
        for (final String alias : cmdInfo.prefixedAliases()) {
            if (!alias.isEmpty()) {
                aliases.add(plugin.getCommandPrefix() + " " + alias);
            }
        }
        for (final String alias : cmdInfo.directlyPrefixedAliases()) {
            if (!alias.isEmpty()) {
                aliases.add(plugin.getCommandPrefix() + alias);
            }
        }
        if (command instanceof BuiltInCommand) {
            final BuiltInCommand builtInCommand = (BuiltInCommand) command;
            for (final String alias : builtInCommand.getStaticAliases()) {
                if (!alias.isEmpty()) {
                    aliases.add(alias);
                }
            }
        }
        final String[] permissions;
        if (command.getPerm() != null) {
            permissions = new String[1];
            permissions[0] = command.getPerm().getName();
        } else {
            permissions = new String[0];
        }
        final com.sk89q.bukkit.util.CommandInfo bukkitCmdInfo = new com.sk89q.bukkit.util.CommandInfo(cmdInfo.usage(), cmdInfo.desc(), aliases.toArray(new String[aliases.size()]), this, permissions);
        if (register(bukkitCmdInfo)) {
            Logging.fine("Registered command '%s' to: %s", aliases.get(0), commandClass);
            String split[] = aliases.get(0).split(" ");
            CommandKey key;
            if (split.length == 1) {
                key = newKey(split[0], true);
            } else {
                key = newKey(split[0], false);
                for (int i = 1; i < split.length; i++) {
                    key = key.newKey(split[i], (i == split.length - 1));
                }
            }
            commandMap.put(aliases.get(0), commandClass.getName());
            return true;
        }
        Logging.severe("Failed to register: " + commandClass);
        return false;
    }

    protected abstract boolean register(com.sk89q.bukkit.util.CommandInfo command);

    protected Command loadCommand(final Class clazz) {
        try {
            return (Command) clazz.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean locateAndRunCommand(BasePlayer player, String[] args) throws CommandException {
        args = commandDetection(args);
        final String className = commandMap.get(args[0]);
        if (className == null) {
            Logging.severe("Could not locate registered command '" + args[0] + "'");
            return false;
        }
        try {
            final Class<Command> commandClass = (Class<Command>) Class.forName(className);
            final Command command = loadCommand(commandClass);
            if (command == null) {
                Logging.severe("Could not load command for class: " + commandClass);
                return false;
            }
            final CommandInfo cmdInfo = commandClass.getAnnotation(CommandInfo.class);
            if (cmdInfo == null) {
                Logging.severe("Missing CommandInfo for command: " + args[0]);
                return false;
            }
            final Set<Character> valueFlags = new HashSet<Character>();

            char[] flags = cmdInfo.flags().toCharArray();
            final Set<Character> newFlags = new HashSet<Character>();
            for (int i = 0; i < flags.length; ++i) {
                if (flags.length > i + 1 && flags[i + 1] == ':') {
                    valueFlags.add(flags[i]);
                    ++i;
                }
                newFlags.add(flags[i]);
            }
            final CommandContext context = new CommandContext(args, valueFlags);
            if (context.argsLength() < cmdInfo.min()) {
                throw new CommandUsageException("Too few arguments.", getUsage(args, 0, command, cmdInfo));
            }
            if (cmdInfo.max() != -1 && context.argsLength() > cmdInfo.max()) {
                throw new CommandUsageException("Too many arguments.", getUsage(args, 0, command, cmdInfo));
            }
            if (!cmdInfo.anyFlags()) {
                for (char flag : context.getFlags()) {
                    if (!newFlags.contains(flag)) {
                        throw new CommandUsageException("Unknown flag: " + flag, getUsage(args, 0, command, cmdInfo));
                    }
                }
            }
            command.runCommand(plugin, player, context);
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected List<String> getUsage(final String[] args, final int level, final Command cmd, final CommandInfo cmdInfo) {
        final List<String> commandUsage = new ArrayList<String>();
        final StringBuilder command = new StringBuilder();
        command.append('/');
        for (int i = 0; i <= level; ++i) {
            command.append(args[i]);
            command.append(' ');
        }
        command.append(getArguments(cmdInfo));
        commandUsage.add(command.toString());

        final List<String> help = plugin.getMessager().getMessages(cmd.getHelp());
        if (help.size() > 0) {
            commandUsage.addAll(help);
        }

        return commandUsage;
    }

    protected CharSequence getArguments(final CommandInfo cmdInfo) {
        final String flags = cmdInfo.flags();

        final StringBuilder command2 = new StringBuilder();
        if (flags.length() > 0) {
            String flagString = flags.replaceAll(".:", "");
            if (flagString.length() > 0) {
                command2.append("[-");
                for (int i = 0; i < flagString.length(); ++i) {
                    command2.append(flagString.charAt(i));
                }
                command2.append("] ");
            }
        }

        command2.append(cmdInfo.usage());

        return command2;
    }

    public String[] commandDetection(String[] split) {
        CommandKey commandKey = getKey(split[0]);
        CommandKey lastActualCommand = null;
        if (commandKey == null) {
            return split;
        } else if (commandKey.isCommand()) {
            lastActualCommand = commandKey;
        }

        int i;
        int lastActualCommandIndex = 0;
        for (i = 1; i < split.length; i++) {
            commandKey = commandKey.getKey(split[i]);
            if (commandKey != null) {
                if (commandKey.isCommand()) {
                    lastActualCommand = commandKey;
                    lastActualCommandIndex = i;
                }
            } else {
                break;
            }
        }
        if (lastActualCommand != null) {
            String[] newSplit = new String[split.length - lastActualCommandIndex];
            newSplit[0] = lastActualCommand.getName();
            if (newSplit.length > 1 && lastActualCommandIndex + 1 < split.length) {
                System.arraycopy(split, lastActualCommandIndex + 1, newSplit, 1, split.length - lastActualCommandIndex - 1);
            }
            return newSplit;
        }
        return split;
    }

    protected CommandKey getKey(final String key) {
        return commandKeys.get(key);
    }

    protected CommandKey newKey(final String key, final boolean command) {
        if (commandKeys.containsKey(key)) {
            if (command) {
                commandKeys.put(key, new CommandKey(commandKeys.get(key)));
            }
            return commandKeys.get(key);
        } else {
            final CommandKey commandKey = new CommandKey(key, command);
            commandKeys.put(key, commandKey);
            return commandKey;
        }
    }
}
