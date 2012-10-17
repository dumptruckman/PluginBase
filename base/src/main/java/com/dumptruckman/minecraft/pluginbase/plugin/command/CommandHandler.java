package com.dumptruckman.minecraft.pluginbase.plugin.command;

import com.dumptruckman.minecraft.pluginbase.entity.BasePlayer;
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

    public boolean locateAndRunCommand(BasePlayer player, String[] args) {
        final String className = commandMap.get(args[0]);
        if (className == null) {
            Logging.severe("Could not locate registered command '" + args[0] + "'");
            return false;
        }
        try {
            final Class<Command> commandClass = (Class<Command>) Class.forName(className);
            final Command command = loadCommand(commandClass);
            final CommandInfo cmdInfo = commandClass.getAnnotation(CommandInfo.class);
            if (cmdInfo == null) {
                Logging.severe("Missing CommandInfo for command: " + args[0]);
                return false;
            }
            final Set<Character> flags = new HashSet<Character>();
            for (char flag : cmdInfo.flags().toCharArray()) {
                flags.add(flag);
            }
            try {
                command.runCommand(plugin, player, new CommandContext(args));
                //command.runCommand(plugin, player, new CommandContext(args, flags));
                return true;
            } catch (CommandException e) {
                e.printStackTrace();
                return false;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
