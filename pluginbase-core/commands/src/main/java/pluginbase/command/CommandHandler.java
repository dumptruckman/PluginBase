package pluginbase.command;

import org.jetbrains.annotations.Nullable;
import pluginbase.logging.PluginLogger;
import pluginbase.messages.BundledMessage;
import pluginbase.messages.Message;
import pluginbase.messages.Messages;
import pluginbase.messages.Theme;
import pluginbase.messages.messaging.SendablePluginBaseException;
import pluginbase.minecraft.BasePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * This class is responsible for handling commands.
 * <br>
 * This entails everything from registering them to detecting executed commands and delegating them
 * to the appropriate command class.
 * <br>
 * This must be implemented fully for a specific Minecraft server implementation.
 */
public abstract class CommandHandler {

    protected static final Pattern PATTERN_ON_SPACE = Pattern.compile(" ", Pattern.LITERAL);

    @NotNull
    protected final CommandProvider commandProvider;
    @NotNull
    protected final Map<String, Class<? extends Command>> registeredCommandClasses;
    protected final Map<Class<? extends Command>, CommandProvider> commandProviderMap;
    private final CommandTree commandTree = new CommandTree();
    @NotNull
    private final Map<BasePlayer, QueuedCommand> queuedCommands = new HashMap<BasePlayer, QueuedCommand>();
    @NotNull
    private Map<CommandInfo, String> usageMap = new HashMap<CommandInfo, String>();

    /**
     * Creates a new command handler.
     * <br>
     * Typically you only want one of these per plugin.
     *
     * @param commandProvider the provider of commands for this handler.
     */
    protected CommandHandler(@NotNull final CommandProvider commandProvider) {
        this.commandProvider = commandProvider;
        this.registeredCommandClasses = new HashMap<String, Class<? extends Command>>();
        this.commandProviderMap = new HashMap<Class<? extends Command>, CommandProvider>();
        Messages.registerMessages(commandProvider, CommandHandler.class);
        commandProviderMap.put(DirectoryCommand.class, commandProvider);
    }

    /**
     * Retrieves a PluginLogger for this CommandHandler which is inherited from the plugin passed in during construction.
     *
     * @return a PluginLogger for this CommandHandler.
     */
    @NotNull
    protected PluginLogger getLog() {
        return commandProvider.getLog();
    }

    //public boolean registerCommmands(String packageName) {

    //}

    /**
     * Registers the command represented by the given command class for the command provider that belongs to this
     * command handler.
     *
     * @param commandClass the command class to register.
     * @return true if command registered successfully.
     * @throws IllegalArgumentException if there was some problem with the command class passed in.
     */
    public boolean registerCommand(@NotNull Class<? extends Command> commandClass) throws IllegalArgumentException {
        return registerCommand(commandProvider, commandClass);
    }

    /**
     * Registers the command represented by the given command class for the given command provider.
     * <br>
     * Generally you can use the single arg version of this method as your commands should typically be using the
     * same command provider this CommandHandler was initially set up with.
     *
     * @param commandProvider the commandProvider to register the command for.  This command provider will be used
     *                        for instantiation of command instances.
     * @param commandClass the command class to register.
     * @return true if command registered successfully.
     * @throws IllegalArgumentException if there was some problem with the command class passed in.
     */
    public boolean registerCommand(@NotNull CommandProvider commandProvider, @NotNull Class<? extends Command> commandClass) throws IllegalArgumentException {
        CommandBuilder commandBuilder = new CommandBuilder(commandProvider, commandClass);
        CommandRegistration commandRegistration = commandBuilder.createCommandRegistration();
        assertNotAlreadyRegistered(commandClass, commandRegistration);
        Command command = commandBuilder.getCommand();
        if (register(commandRegistration, command)) {
            registerRootCommands(commandRegistration);
            cacheUsageString(commandBuilder);
            String[] aliases = commandRegistration.getAliases();
            for (String alias : aliases) {
                configureCommandKeys(alias);
                registeredCommandClasses.put(alias, commandClass);
            }
            commandProviderMap.put(commandClass, commandProvider);
            // Register language in the command class if any.
            Messages.registerMessages(this.commandProvider, commandClass);
            getLog().fine("Registered command '%s' to: %s", aliases[0], commandClass);
            return true;
        }

        getLog().severe("Failed to register: " + commandClass);
        return false;
    }

    private static final String SUB_COMMAND_HELP = "Displays a list of sub-commands.";

    private void registerRootCommands(CommandRegistration commandRegistration) {
        String[] aliases = commandRegistration.getAliases();
        Command command = new DirectoryCommand(commandProvider);
        for (String alias : aliases) {
            String[] args = PATTERN_ON_SPACE.split(alias);
            if (args.length > 1) {
                List<String> directoryAliases = new ArrayList<String>(args.length - 1);
                StringBuilder directoryAliasBuilder = new StringBuilder();
                for (int i = 0; i < args.length - 1; i++) {
                    if (i != 0) {
                        directoryAliasBuilder.append(" ");
                    }
                    directoryAliasBuilder.append(args[i]);
                    String directoryAlias = directoryAliasBuilder.toString();
                    if (!registeredCommandClasses.containsKey(directoryAlias)) {
                        directoryAliases.add(directoryAlias);
                        registeredCommandClasses.put(directoryAlias, DirectoryCommand.class);
                        configureCommandKeys(directoryAlias);
                        getLog().finer("Registered directory command '%s'", directoryAlias);
                    }
                }
                if (!directoryAliases.isEmpty()) {
                    CommandRegistration directoryCommandRegistration = new CommandRegistration(SUB_COMMAND_HELP, SUB_COMMAND_HELP,
                            directoryAliases.toArray(new String[directoryAliases.size()]), commandProvider);
                    register(directoryCommandRegistration, command);
                }
            }
        }
    }

    private void assertNotAlreadyRegistered(Class commandClass, CommandRegistration commandRegistration) {
        String[] aliases = commandRegistration.getAliases();
        for (String alias : aliases) {
            if (registeredCommandClasses.containsKey(alias)) {
                throw new IllegalArgumentException("The alias '" + alias + "' for '" + commandClass + "' has already been registered by '" + registeredCommandClasses.get(alias) + "'");
            }
        }
    }

    void configureCommandKeys(String primaryAlias) {
        commandTree.registerKeysForAlias(primaryAlias);
    }

    /**
     * Tells the server implementation to register the given command information as a command so that
     * someone using the command will delegate the execution to this plugin/command handler.
     *
     * @param commandInfo the info for the command to register.
     * @return true if successfully registered.
     */
    protected abstract boolean register(@NotNull final CommandRegistration commandInfo, @NotNull final Command command);

    void removedQueuedCommand(@NotNull final BasePlayer player, @NotNull final QueuedCommand command) {
        if (queuedCommands.containsKey(player) && queuedCommands.get(player).equals(command)) {
            queuedCommands.remove(player);
        }
    }

    /** Message used when a users tries to confirm a command but has not queued one or the queued one has expired. */
    public static final Message NO_QUEUED_COMMANDS = Messages.createMessage("commands.queued.none_queued",
            Theme.SORRY + "Sorry, but you have not used any commands that require confirmation.");
    /** Default message used when the user must confirm a queued command. */
    public static final Message MUST_CONFIRM = Messages.createMessage("commands.queued.must_confirm",
            Theme.DO_THIS + "You must confirm the previous command by typing " + Theme.CMD_HIGHLIGHT + "%s"
                    + "\n" + Theme.INFO + "You have %s to comply.");

    public static final Message PERMISSION_DENIED = Messages.createMessage("commands.permission-denied",
            Theme.SORRY + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.");

    /**
     * Confirms any queued command for the given player.
     *
     * @param player the player to confirm queued commands for.
     * @return true if there was a queued command.
     */
    public boolean confirmCommand(@NotNull final BasePlayer player) {
        final QueuedCommand queuedCommand = queuedCommands.get(player);
        if (queuedCommand != null) {
            queuedCommand.confirm();
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    protected final Command getCommand(@NotNull String[] args) {
        args = commandDetection(args);
        return _getCommand(args[0]);
    }

    @Nullable
    private Command _getCommand(@NotNull String baseCommandArg) {
        final Class<? extends Command> commandClass = registeredCommandClasses.get(baseCommandArg);
        if (commandClass == null) {
            getLog().severe("Could not locate registered command '" + baseCommandArg + "'");
            return null;
        }
        final CommandProvider commandProviderForCommand = commandProviderMap.get(commandClass);
        if (commandProviderForCommand == null) {
            throw new IllegalStateException("CommandProvider not registered for " + commandClass);
        }
        return CommandLoader.loadCommand(commandProviderForCommand, commandClass);
    }

    /**
     * Locates and runs a command executed by a user.
     *
     * @param player the user executing the command.
     * @param args the space separated arguments of the command including the base command itself.
     * @return true if the command executed successfully.
     * @throws SendablePluginBaseException if there were any exceptions brought about by the usage of the command.
     * <br>
     * The causes are many fold and include things such as using an improper amount of parameters or attempting to
     * use a flag not recognized by the command.
     * TODO This needs to throw an extended PluginBaseException
     */
    public boolean locateAndRunCommand(@NotNull final BasePlayer player, @NotNull String[] args) throws CommandException {
        args = commandDetection(args);
        getLog().finest("'%s' is attempting to use command '%s'", player, Arrays.toString(args));
        if (this.commandProvider.useQueuedCommands()
                && !this.registeredCommandClasses.containsKey(this.commandProvider.getCommandPrefix() + "confirm")
                && args.length == 2
                && args[0].equalsIgnoreCase(this.commandProvider.getCommandPrefix())
                && args[1].equalsIgnoreCase("confirm")) {
            getLog().finer("No confirm command registered, using built in confirm...");
            if (!confirmCommand(player)) {
                this.commandProvider.getMessager().message(player, NO_QUEUED_COMMANDS);
            }
            return true;
        }
        final Command command = _getCommand(args[0]);
        if (command == null) {
            return false;
        }
        if (command instanceof DirectoryCommand) {
            ((DirectoryCommand) command).runCommand(player, args[0], commandTree.getTreeAt(args[0]));
            return true;
        }
        if (command.getPerm() != null && !command.getPerm().hasPermission(player)) {
            BundledMessage permissionMessage = command.getPermissionMessage();
            if (permissionMessage == null) {
                permissionMessage = PERMISSION_DENIED.bundle();
            }
            commandProvider.getMessager().message(player, permissionMessage);
            return false;
        }
        final CommandInfo cmdInfo = command.getClass().getAnnotation(CommandInfo.class);
        if (cmdInfo == null) {
            getLog().severe("Missing CommandInfo for command: " + args[0]);
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
            throw new CommandUsageException(TOO_FEW_ARGUMENTS.bundle(), getUsage(args, 0, command, cmdInfo));
        }
        if (cmdInfo.max() != -1 && context.argsLength() > cmdInfo.max()) {
            throw new CommandUsageException(TOO_MANY_ARGUMENTS.bundle(), getUsage(args, 0, command, cmdInfo));
        }
        if (!cmdInfo.anyFlags()) {
            for (char flag : context.getFlags()) {
                if (!newFlags.contains(flag)) {
                    throw new CommandUsageException(UNKNOWN_FLAG.bundle(flag), getUsage(args, 0, command, cmdInfo));
                }
            }
        }
        if (!command.runCommand(player, context)) {
            throw new CommandUsageException(USAGE_ERROR.bundle(), getUsage(args, 0, command, cmdInfo));
        }
        if (command instanceof QueuedCommand) {
            final QueuedCommand queuedCommand = (QueuedCommand) command;
            getLog().finer("Queueing command '%s' for '%s'", queuedCommand, player);
            queuedCommands.put(player, queuedCommand);
            final BundledMessage confirmMessage = queuedCommand.getConfirmMessage();
            this.commandProvider.getMessager().message(player, confirmMessage);
        }
        return true;
    }

    public String[] commandDetection(@NotNull final String[] split) {
        return commandTree.joinArgsForKnownCommands(split);
    }

    public static final Message TOO_FEW_ARGUMENTS = Messages.createMessage("commands.usage.too_few_arguments",
            Theme.ERROR + "Too few arguments.");
    public static final Message TOO_MANY_ARGUMENTS = Messages.createMessage("commands.usage.too_many_arguments",
            Theme.ERROR + "Too many arguments.");
    public static final Message UNKNOWN_FLAG = Messages.createMessage("commands.usage.unknown_flag", Theme.ERROR + "Unknown flag: " + Theme.VALUE + "%s");
    public static final Message USAGE_ERROR = Messages.createMessage("commands.usage.usage_error", Theme.ERROR + "Usage error...");

    public static final Message VALUE_FLAG_ALREADY_GIVEN = Messages.createMessage("commands.usage.value_flag_already_given",
            Theme.ERROR + "Value flag '" + Theme.VALUE + "%s" + Theme.ERROR + "' already given");
    public static final Message NO_VALUE_FOR_VALUE_FLAG = Messages.createMessage("commands.usage.must_specify_value_for_value_flag",
            Theme.ERROR + "No value specified for the '" + Theme.VALUE + "-%s" + Theme.ERROR + "' flag.");

    public static final Message SUB_COMMAND_LIST = Messages.createMessage("commands.sub_command_list",
            Theme.INFO + "The following is a list of sub-commands for '" + Theme.VALUE + "%s" + Theme.INFO + "':\n%s");

    /**
     * Returns a list of strings detailing the usage of the given command.
     *
     * @param args
     * @param level
     * @param cmd
     * @param cmdInfo
     * @return
     */
    protected List<String> getUsage(@NotNull final String[] args, final int level, final Command cmd, @NotNull final CommandInfo cmdInfo) {
        final List<String> commandUsage = new ArrayList<String>();
        final StringBuilder command = new StringBuilder();
        command.append(Theme.CMD_USAGE);
        command.append('/');
        for (int i = 0; i <= level; ++i) {
            command.append(args[i]);
            command.append(' ');
        }
        command.append(getArguments(cmdInfo));
        commandUsage.add(command.toString());

        final String help;
        final Message helpMessage = cmd.getHelp();
        if (helpMessage != null) {
            help = commandProvider.getMessager().getLocalizedMessage(helpMessage);
        } else {
            help = "";
        }
        if (!help.isEmpty()) {
            commandUsage.add(help);
        }

        return commandUsage;
    }

    private void cacheUsageString(CommandBuilder commandBuilder) {
        usageMap.put(commandBuilder.getCommandInfo(), commandBuilder.getCommandUsageString());
    }

    protected String getArguments(@NotNull final CommandInfo cmdInfo) {
        return usageMap.containsKey(cmdInfo) ? usageMap.get(cmdInfo) : "";
    }

    protected static class CommandRegistration {

        private final String[] aliases;
        private final CommandProvider registeredWith;
        private final String usage, desc;
        private final String[] permissions;

        CommandRegistration(String usage, String desc, String[] aliases, CommandProvider registeredWith) {
            this(usage, desc, aliases, registeredWith, null);
        }

        CommandRegistration(String usage, String desc, String[] aliases, CommandProvider registeredWith, String[] permissions) {
            this.usage = usage;
            this.desc = desc;
            this.aliases = aliases;
            this.permissions = permissions;
            this.registeredWith = registeredWith;
        }

        public String[] getAliases() {
            return aliases;
        }

        public String getName() {
            return aliases[0];
        }

        public String getUsage() {
            return usage;
        }

        public String getDesc() {
            return desc;
        }

        public String[] getPermissions() {
            return permissions;
        }

        public CommandProvider getRegisteredWith() {
            return registeredWith;
        }
    }

    public List<String> tabComplete(@NotNull final BasePlayer player, @NotNull String[] args) {
        if (args.length > 1) {
            String[] newArgs = new String[args.length - 1];
            System.arraycopy(args, 0, newArgs, 0, args.length - 1);
            String lastArg = args[args.length - 1];
            newArgs = commandDetection(newArgs);
            args = new String[newArgs.length + 1];
            System.arraycopy(newArgs, 0, args, 0, newArgs.length);
            args[args.length - 1] = lastArg;
        }
        final Command command = _getCommand(args[0]);
        if (command != null) {
            if (args.length == 2 && command instanceof DirectoryCommand) {
                return tabCompleteDirectory(player, args);
            } else if (args.length > 1 && (command.getPerm() == null || command.getPerm().hasPermission(player))) {
                final CommandInfo cmdInfo = command.getClass().getAnnotation(CommandInfo.class);
                if (cmdInfo != null) {
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
                    try {
                        CommandContext context = new CommandContext(args, valueFlags);
                        return command.tabComplete(player, context);
                    } catch (CommandException ignore) { }
                }
            }
        }
        return Collections.emptyList();
    }

    private List<String> tabCompleteDirectory(@NotNull final BasePlayer player, @NotNull String[] args) {
        CommandTree directoryTree = commandTree.getTreeAt(args[0]);
        Set<String> subDirectories = directoryTree.getSubDirectories();
        Set<String> subCommands = directoryTree.getSubCommands();
        Set<String> tabCompleteSet = new HashSet<String>(subDirectories.size() + subCommands.size() + 1);

        args[1] = args[1].trim().toLowerCase();

        for (String subDirectory : subDirectories) {
            if (subDirectory.startsWith(args[1])) {
                tabCompleteSet.add(subDirectory);
            }
        }

        List<String> potentialSubCommands = new ArrayList<String>(subCommands.size());
        for (String subCommand : subCommands) {
            if (subCommand.startsWith(args[1])) {
                potentialSubCommands.add(subCommand);
            }
        }

        for (String potentialSubCommand : potentialSubCommands) {
            Command subCommand = _getCommand(args[0] + " " + potentialSubCommand);
            if (subCommand != null && (!player.isPlayer() || (subCommand.getPerm() == null || player.hasPerm(subCommand.getPerm())))) {
                tabCompleteSet.add(potentialSubCommand);
            }
        }

        if (commandProvider.useQueuedCommands()
                && !registeredCommandClasses.containsKey(commandProvider.getCommandPrefix() + "confirm")
                && commandProvider.getCommandPrefix().equalsIgnoreCase(args[0])
                && "confirm".startsWith(args[1])) {
            tabCompleteSet.add("confirm");
        }

        List<String> tabCompleteList = new ArrayList<String>(tabCompleteSet);

        if (tabCompleteList.size() == 1 && tabCompleteList.get(0).equals(args[1])) {
            return Collections.emptyList();
        }

        Collections.sort(tabCompleteList);
        return tabCompleteList;
    }
}
