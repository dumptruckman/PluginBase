package pluginbase.command;

import org.jetbrains.annotations.NotNull;
import pluginbase.messages.Theme;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class CommandBuilder {

    private static final Pattern OPTIONAL_ARGS_PATTERN = Pattern.compile("\\[.+?\\]");
    private static final Pattern REQUIRED_ARGS_PATTERN = Pattern.compile("\\{.+?\\}");

    private CommandProvider commandProvider;
    private CommandInfo commandInfo;
    private Command command;
    List<String> aliases;
    String[] permissions;
    String usageString;

    CommandBuilder(@NotNull CommandProvider commandProvider, @NotNull Class<? extends Command> commandClass) {
        this.commandProvider = commandProvider;
        commandInfo = gatherCommandInfo(commandClass);
        command = CommandLoader.loadCommand(commandProvider, commandClass);
        aliases = gatherAliases(commandProvider, command, commandInfo);
        permissions = gatherPermissions(command);
        usageString = gatherUsageString();
    }

    @NotNull
    private CommandInfo gatherCommandInfo(Class<? extends Command> commandClass) {
        CommandInfo commandInfo = commandClass.getAnnotation(CommandInfo.class);
        if (commandInfo == null) {
            throw new IllegalArgumentException("Command must be annotated with @CommandInfo");
        }
        return commandInfo;
    }

    private List<String> gatherAliases(CommandProvider commandProvider, Command command, CommandInfo cmdInfo) {
        CommandAliases aliases = new CommandAliases(commandProvider, cmdInfo, command);
        return aliases.gatherAliases();
    }

    private String[] gatherPermissions(Command command) {
        String[] permissions;
        if (command.getPerm() != null) {
            permissions = new String[1];
            permissions[0] = command.getPerm().getName();
        } else {
            permissions = new String[0];
        }
        return permissions;
    }

    private String gatherUsageString() {
        final String flags = commandInfo.flags();

        final StringBuilder command2 = new StringBuilder();
        command2.append(parseUsage(commandInfo.usage()));

        for (int i = 0; i < flags.length(); ++i) {
            command2.append(" ");
            command2.append(Theme.OPT_ARG).append("[").append(Theme.CMD_FLAG).append("-");
            command2.append(flags.charAt(i));
            if (flags.length() > (i + 1) && flags.charAt(i + 1) == ':') {
                command2.append(Theme.REQ_ARG).append(" {VALUE}");
                i++;
            }
            command2.append(Theme.OPT_ARG).append("]");
        }
        return command2.toString();
    }

    private CharSequence parseUsage(@NotNull String usageString) {
        if (usageString.isEmpty()) {
            return usageString;
        }
        // Add required arg theme before required args
        StringBuilder usage = new StringBuilder(usageString.length() + 10);
        Matcher matcher = REQUIRED_ARGS_PATTERN.matcher(usageString);
        int lastIndex = 0;
        while (matcher.find()) {
            if (matcher.start() > lastIndex) {
                // Add the initial part of the string if the required arg isn't first position
                usage.append(usageString.subSequence(lastIndex, matcher.start()));
            }
            usage.append(Theme.REQ_ARG);
            usage.append(matcher.group());
            lastIndex = matcher.end();
        }
        // Add what is left over in the string
        usage.append(usageString.subSequence(lastIndex, usageString.length()));

        // Replace initial string with builder that contains colored required args
        usageString = usage.toString();

        // Add optional arg theme before optional args
        usage = new StringBuilder(usageString.length() + 10);
        matcher = OPTIONAL_ARGS_PATTERN.matcher(usageString);
        lastIndex = 0;
        while (matcher.find()) {
            if (matcher.start() > lastIndex) {
                // Add the initial part of the string if the optional arg isn't first position
                usage.append(usageString.subSequence(lastIndex, matcher.start()));
            }
            usage.append(Theme.OPT_ARG);
            usage.append(matcher.group());
            lastIndex = matcher.end();
        }
        // Add what is left over in the string
        usage.append(usageString.subSequence(lastIndex, usageString.length()));
        return usage;
    }

    public String getPrimaryAlias() {
        return aliases.get(0);
    }

    public CommandInfo getCommandInfo() {
        return commandInfo;
    }

    public CommandHandler.CommandRegistration createCommandRegistration() {
        return new CommandHandler.CommandRegistration(getCommandUsageString(), commandInfo.desc(), aliases.toArray(new String[aliases.size()]), commandProvider, permissions);
    }

    public String getCommandUsageString() {
        return usageString;
    }

    public Command getCommand() {
        return command;
    }

    private static class CommandAliases {

        private final CommandProvider commandProvider;
        private final CommandInfo cmdInfo;
        private final Command command;

        private CommandAliases(final CommandProvider commandProvider, final CommandInfo cmdInfo, final Command command) {
            this.commandProvider = commandProvider;
            this.cmdInfo = cmdInfo;
            this.command = command;
        }

        private List<String> aliases;

        public List<String> gatherAliases() {
            return buildUpAliasList();
        }

        private List<String> buildUpAliasList() {
            int totalAliasCount = getTotalAliasCount();
            aliases = new ArrayList<String>(totalAliasCount);
            addPrimaryAlias();
            addRegularAliases();
            addPrefixedAliases();
            addDirectlyPrefixedAliases();
            addAdditionalAliases();
            return aliases;
        }

        private int getTotalAliasCount() {
            return cmdInfo.aliases().length
                    + cmdInfo.prefixedAliases().length
                    + cmdInfo.directlyPrefixedAliases().length
                    + commandProvider.getAdditionalCommandAliases(command.getClass()).length
                    + 1;
        }

        private void addPrimaryAlias() {
            if (cmdInfo.directlyPrefixPrimary()) {
                aliases.add(commandProvider.getCommandPrefix() + cmdInfo.primaryAlias());
            } else if (cmdInfo.prefixPrimary())  {
                aliases.add(commandProvider.getCommandPrefix() + " " + cmdInfo.primaryAlias());
            } else {
                aliases.add(cmdInfo.primaryAlias());
            }
        }

        private void addRegularAliases() {
            for (final String alias : cmdInfo.aliases()) {
                if (!alias.isEmpty()) {
                    aliases.add(alias);
                }
            }
        }

        private void addPrefixedAliases() {
            for (final String alias : cmdInfo.prefixedAliases()) {
                if (!alias.isEmpty()) {
                    aliases.add(commandProvider.getCommandPrefix() + " " + alias);
                }
            }
        }

        private void addDirectlyPrefixedAliases() {
            for (final String alias : cmdInfo.directlyPrefixedAliases()) {
                if (!alias.isEmpty()) {
                    aliases.add(commandProvider.getCommandPrefix() + alias);
                }
            }
        }

        private void addAdditionalAliases() {
            for (final String alias : commandProvider.getAdditionalCommandAliases(command.getClass())) {
                if (!alias.isEmpty()) {
                    aliases.add(alias);
                }
            }
        }
    }
}
