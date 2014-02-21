/*
 * Copyright (C) 2010 sk89q <http://www.sk89q.com> and contributors
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
package pluginbase.command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.messages.Message;

import java.util.*;

/**
 * Contains information regarding the current usage of a command such as what args and flags were used.
 */
public final class CommandContext {

    private final String command;
    private final List<String> parsedArgs;
    private final List<Integer> originalArgIndices;
    private final Set<Character> booleanFlags = new HashSet<Character>();
    private final Map<Character, String> valueFlags = new HashMap<Character, String>();

    CommandContext(@NotNull String args) throws CommandException {
        this(args.split(" "), null);
    }

    CommandContext(@NotNull String[] args) throws CommandException {
        this(args, null);
    }

    CommandContext(@NotNull String args, @Nullable Set<Character> valueFlags) throws CommandException {
        this(args.split(" "), valueFlags);
    }

    /**
     * @param args An array with arguments including the command. Empty strings outside quotes will be removed.
     * @param valueFlags A set containing all value flags. Pass null to disable value flag parsing.
     * @throws CommandException This is thrown if flag fails for some reason.
     */
    CommandContext(@NotNull String[] args, @Nullable Set<Character> valueFlags) throws CommandException {
        if (valueFlags == null) {
            valueFlags = Collections.emptySet();
        }

        command = args[0];

        // Eliminate empty args and combine multi-word args first
        List<Integer> argIndexList = new ArrayList<Integer>(args.length);
        List<String> argList = new ArrayList<String>(args.length);
        for (int i = 1; i < args.length; ++i) {
            String arg = args[i];
            if (arg.length() == 0) {
                continue;
            }

            argIndexList.add(i);

            switch (arg.charAt(0)) {
                case '\'':
                case '"':
                    final StringBuilder build = new StringBuilder();
                    final char quotedChar = arg.charAt(0);

                    int endIndex;
                    for (endIndex = i; endIndex < args.length; ++endIndex) {
                        final String arg2 = args[endIndex];
                        if (arg2.charAt(arg2.length() - 1) == quotedChar && arg2.length() > 1) {
                            if (endIndex != i) build.append(' ');
                            build.append(arg2.substring(endIndex == i ? 1 : 0, arg2.length() - 1));
                            break;
                        } else if (endIndex == i) {
                            build.append(arg2.substring(1));
                        } else {
                            build.append(' ').append(arg2);
                        }
                    }

                    if (endIndex < args.length) {
                        arg = build.toString();
                        i = endIndex;
                    }

                    // In case there is an empty quoted string
                    if (arg.length() == 0) {
                        continue;
                    }
                    // else raise exception about hanging quotes?
            }
            argList.add(arg);
        }

        // Then flags

        this.originalArgIndices = new ArrayList<Integer>(argIndexList.size());
        this.parsedArgs = new ArrayList<String>(argList.size());

        for (int nextArg = 0; nextArg < argList.size(); ) {
            // Fetch argument
            String arg = argList.get(nextArg++);

            // Not a flag?
            if (arg.charAt(0) != '-' || arg.length() == 1 || !arg.matches("^-[a-zA-Z]+$")) {
                originalArgIndices.add(argIndexList.get(nextArg - 1));
                parsedArgs.add(arg);
                continue;
            }

            // Handle flag parsing terminator --
            if (arg.equals("--")) {
                while (nextArg < argList.size()) {
                    originalArgIndices.add(argIndexList.get(nextArg));
                    parsedArgs.add(argList.get(nextArg++));
                }
                break;
            }

            // Go through the flag characters
            for (int i = 1; i < arg.length(); ++i) {
                char flagName = arg.charAt(i);

                if (valueFlags.contains(flagName)) {
                    if (this.valueFlags.containsKey(flagName)) {
                        throw new CommandException(Message.bundleMessage(CommandHandler.VALUE_FLAG_ALREADY_GIVEN, flagName));
                    }

                    if (nextArg >= argList.size()) {
                        throw new CommandException(Message.bundleMessage(CommandHandler.NO_VALUE_FOR_VALUE_FLAG, flagName));
                    }

                    // If it is a value flag, read another argument and add it
                    this.valueFlags.put(flagName, argList.get(nextArg++));
                } else {
                    booleanFlags.add(flagName);
                }
            }
        }
    }

    /**
     * Gets the name of the command.
     *
     * @return the name of the command.
     */
    public String getCommand() {
        return command;
    }

    /**
     * Checks whether the given command matches this command, ignoring case.
     *
     * @param command the command to check
     * @return true if it matches the used command, ignoring case.
     */
    public boolean matches(String command) {
        return this.command.equalsIgnoreCase(command);
    }

    /**
     * Gets the arg at the given index.
     *
     * @param index the index of the arg to retrieve.
     * @return the arg at the given index.
     * @throws java.lang.IndexOutOfBoundsException if the given index does not exist in the list of args.
     */
    public String getString(int index) throws IndexOutOfBoundsException {
        return parsedArgs.get(index);
    }

    /**
     * Gets the arg at the given index or the given default if that index is not valid.
     *
     * @param index the index of the arg to retrieve.
     * @param def the default to use if the index is not valid.
     * @return the arg at the given index or the given default if that index is not valid.
     */
    public String getString(int index, String def) {
        return index < parsedArgs.size() ? parsedArgs.get(index) : def;
    }

    /**
     * Gets the integer arg at the given index.
     *
     * @param index the index of the integer arg to retrieve.
     * @return the integer arg at the given index.
     * @throws NumberFormatException if the arg at the given index is not an integer.
     * @throws java.lang.IndexOutOfBoundsException if the given index does not exist in the list of args.
     */
    public Integer getInteger(int index) throws NumberFormatException, IndexOutOfBoundsException {
        return Integer.parseInt(parsedArgs.get(index));
    }

    /**
     * Gets the integer arg at the given index or the given default if that index is not valid.
     *
     * @param index the index of the integer arg to retrieve.
     * @param def the default to use if the index is not valid.
     * @return the integer arg at the given index or the given default if that index is not valid.
     * @throws NumberFormatException if the arg at the given index is not an integer.
     */
    public Integer getInteger(int index, int def) throws NumberFormatException {
        return index < parsedArgs.size() ? Integer.parseInt(parsedArgs.get(index)) : def;
    }

    /**
     * Gets the long arg at the given index.
     *
     * @param index the index of the long arg to retrieve.
     * @return the long arg at the given index.
     * @throws NumberFormatException if the arg at the given index is not a long.
     * @throws java.lang.IndexOutOfBoundsException if the given index does not exist in the list of args.
     */
    public Long getLong(int index) throws NumberFormatException, IndexOutOfBoundsException {
        return Long.parseLong(parsedArgs.get(index));
    }

    /**
     * Gets the long arg at the given index or the given default if that index is not valid.
     *
     * @param index the index of the long arg to retrieve.
     * @param def the default to use if the index is not valid.
     * @return the long arg at the given index or the given default if that index is not valid.
     * @throws NumberFormatException if the arg at the given index is not a long.
     */
    public Long getLong(int index, int def) throws NumberFormatException {
        return index < parsedArgs.size() ? Long.parseLong(parsedArgs.get(index)) : def;
    }

    /**
     * Gets the double arg at the given index.
     *
     * @param index the index of the double arg to retrieve.
     * @return the double arg at the given index.
     * @throws NumberFormatException if the arg at the given index is not a double.
     * @throws java.lang.IndexOutOfBoundsException if the given index does not exist in the list of args.
     */
    public Double getDouble(int index) throws NumberFormatException {
        return Double.parseDouble(parsedArgs.get(index));
    }

    /**
     * Gets the double arg at the given index or the given default if that index is not valid.
     *
     * @param index the index of the double arg to retrieve.
     * @param def the default to use if the index is not valid.
     * @return the double arg at the given index or the given default if that index is not valid.
     * @throws NumberFormatException if the arg at the given index is not a double.
     */
    public Double getDouble(int index, double def) throws NumberFormatException {
        return index < parsedArgs.size() ? Double.parseDouble(parsedArgs.get(index)) : def;
    }

    /**
     * Checks if the args include the given flag.
     *
     * @param flag the flag to check for.
     * @return true if the flag is present in the args.
     */
    public boolean hasFlag(char flag) {
        return booleanFlags.contains(flag) || valueFlags.containsKey(flag);
    }

    /**
     * Gets the set of flags used with the command.
     *
     * @return the set of flags used with the command.
     */
    public Set<Character> getFlags() {
        return booleanFlags;
    }

    /**
     * A mapping of any value flags used and their values.
     *
     * @return a map with the keys as the flag used and the value as the value of those flags.
     */
    public Map<Character, String> getValueFlags() {
        return valueFlags;
    }

    /**
     * Gets the value of the given value flag.
     *
     * @param flag the flag to get the value for.
     * @return the value of the given flag or null if the flag is not present.
     */
    public String getFlag(char flag) {
        return valueFlags.get(flag);
    }

    /**
     * Gets the value of the given value flag or the given default if the flag is not present.
     *
     * @param flag the flag to get the value for.
     * @param def the default to use if the flag is not present.
     * @return the value of the given flag or the given default if the flag is not present.
     */
    public String getFlag(char flag, String def) {
        final String value = valueFlags.get(flag);
        return value != null ? value : def;
    }

    /**
     * Gets the integer value of the given value flag.
     *
     * @param flag the flag to get the integer value for.
     * @return the integer value of the given flag or null if the flag is not present.
     * @throws NumberFormatException if the flag value is not an integer.
     */
    public Integer getFlagInteger(char flag) throws NumberFormatException {
        return Integer.parseInt(valueFlags.get(flag));
    }

    /**
     * Gets the integer value of the given value flag or the given default if the flag is not present.
     *
     * @param flag the flag to get the integer value for.
     * @param def the default to use if the flag is not present.
     * @return the integer value of the given flag or the given default if the flag is not present.
     * @throws NumberFormatException if the flag value is not an integer.
     */
    public Integer getFlagInteger(char flag, int def) throws NumberFormatException {
        final String value = valueFlags.get(flag);
        return value != null ? Integer.parseInt(value) : def;
    }

    /**
     * Gets the long value of the given value flag.
     *
     * @param flag the flag to get the long value for.
     * @return the long value of the given flag or null if the flag is not present.
     * @throws NumberFormatException if the flag value is not a long.
     */
    public Long getFlagLong(char flag) throws NumberFormatException {
        return Long.parseLong(valueFlags.get(flag));
    }

    /**
     * Gets the long value of the given value flag or the given default if the flag is not present.
     *
     * @param flag the flag to get the long value for.
     * @param def the default to use if the flag is not present.
     * @return the long value of the given flag or the given default if the flag is not present.
     * @throws NumberFormatException if the flag value is not a long.
     */
    public Long getFlagLong(char flag, long def) throws NumberFormatException {
        final String value = valueFlags.get(flag);
        return value != null ? Long.parseLong(value) : def;
    }

    /**
     * Gets the double value of the given value flag.
     *
     * @param flag the flag to get the double value for.
     * @return the double value of the given flag or null if the flag is not present.
     * @throws NumberFormatException if the flag value is not a double.
     */
    public Double getFlagDouble(char flag) throws NumberFormatException {
        return Double.parseDouble(valueFlags.get(flag));
    }

    /**
     * Gets the double value of the given value flag or the given default if the flag is not present.
     *
     * @param flag the flag to get the double value for.
     * @param def the default to use if the flag is not present.
     * @return the double value of the given flag or the given default if the flag is not present.
     * @throws NumberFormatException if the flag value is not a double.
     */
    public Double getFlagDouble(char flag, double def) throws NumberFormatException {
        final String value = valueFlags.get(flag);
        return value != null ? Double.parseDouble(value) : def;
    }

    /**
     * Gets the number of arguments used with the command not including flags.
     *
     * @return the number of arguments used with the command not including flags.
     */
    public int argsLength() {
        return parsedArgs.size();
    }
}
