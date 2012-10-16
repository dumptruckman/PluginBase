package com.dumptruckman.minecraft.pluginbase.plugin.command;

import com.dumptruckman.minecraft.pluginbase.entity.BasePlayer;
import com.dumptruckman.minecraft.pluginbase.plugin.PluginBase;
import com.dumptruckman.minecraft.pluginbase.plugin.command.builtin.BuiltInCommand;
import com.dumptruckman.minecraft.pluginbase.util.Logging;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class CommandHandler<P extends PluginBase> {

    protected final P plugin;
    protected final Map<String, String> commandMap;

    public CommandHandler(P plugin) {
        this.plugin = plugin;
        this.commandMap = new HashMap<String, String>();
    }

    public boolean registerCommand(Class<? extends Command> commandClass) {
        final CommandInfo cmdInfo = commandClass.getAnnotation(CommandInfo.class);
        if (cmdInfo == null) {
            throw new IllegalArgumentException("Command must be annotated with @CommandInfo");
        }
        final Command command = loadCommand(commandClass);
        if (command == null) {
            return false;
        }

        final String[] aliases;
        if (command instanceof BuiltInCommand) {
            aliases = new String[cmdInfo.aliases().length + cmdInfo.prefixedAliases().length
                    + cmdInfo.directlyPrefixedAliases().length + ((BuiltInCommand) command).getStaticAliases().size()
                    + 1];
        } else {
            aliases = new String[cmdInfo.aliases().length + cmdInfo.prefixedAliases().length
                    + cmdInfo.directlyPrefixedAliases().length + 1];
        }
        if (cmdInfo.directlyPrefixPrimary()) {
            aliases[0] = plugin.getCommandPrefix() + cmdInfo.primaryAlias();
        } else if (cmdInfo.prefixPrimary())  {
            aliases[0] = plugin.getCommandPrefix() + " " + cmdInfo.primaryAlias();
        } else {
            aliases[0] = cmdInfo.primaryAlias();
        }
        if (commandMap.containsKey(aliases[0])) {
            throw new IllegalArgumentException("Command with the same primary alias has already been registered!");
        }
        System.arraycopy(cmdInfo.aliases(), 0, aliases, 1, cmdInfo.aliases().length);
        int start = 1 + cmdInfo.aliases().length;
        for (int i = 0; i < cmdInfo.prefixedAliases().length; i++) {
            aliases[start + i] = plugin.getCommandPrefix() + " " + cmdInfo.prefixedAliases()[i];
        }
        start = 1 + cmdInfo.aliases().length + cmdInfo.prefixedAliases().length;
        for (int i = 0; i < cmdInfo.directlyPrefixedAliases().length; i++) {
            aliases[start + i] = plugin.getCommandPrefix() + " " + cmdInfo.directlyPrefixedAliases()[i];
        }
        if (command instanceof BuiltInCommand) {
            final BuiltInCommand builtInCommand = (BuiltInCommand) command;
            start = 1 + cmdInfo.aliases().length + cmdInfo.prefixedAliases().length
                    + cmdInfo.directlyPrefixedAliases().length;
            for (int i = 0; i < builtInCommand.getStaticAliases().size(); i++) {
                aliases[start + i] = builtInCommand.getStaticAliases().get(i);
            }
        }
        final String[] permissions;
        if (command.getPerm() != null) {
            permissions = new String[1];
            permissions[0] = command.getPerm().getName();
        } else {
            permissions = new String[0];
        }
        final com.sk89q.bukkit.util.CommandInfo bukkitCmdInfo = new com.sk89q.bukkit.util.CommandInfo(cmdInfo.usage(), cmdInfo.desc(), aliases, this, permissions);
        if (register(bukkitCmdInfo)) {
            commandMap.put(aliases[0], commandClass.getName());
            return true;
        }
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
                command.runCommand(plugin, player, new CommandContext(args, flags));
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
