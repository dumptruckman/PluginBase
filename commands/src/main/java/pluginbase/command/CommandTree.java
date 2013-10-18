package pluginbase.command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class CommandTree {

    @Nullable
    private String name;

    private final Map<String, CommandTree> treeMap = new HashMap<String, CommandTree>();
    private final Map<String, CommandKey> keyMap = new HashMap<String, CommandKey>();

    public CommandTree() {
        this.name = null;
    }

    private CommandTree(@NotNull CommandTree parent, @NotNull String name) {
        name = name.toLowerCase();
        this.name = parent.name != null ? parent.name + " " + name : name;
    }

    public void registerKeysForAlias(@NotNull String alias) {
        String[] args = CommandHandler.PATTERN_ON_SPACE.split(alias);
        registerKeysForArgs(args);
    }

    private void registerKeysForArgs(String[] args) {
        String initialArg = args[0];
        if (args.length == 1) {
            CommandKey commandKey = new CommandKey(getCommandKeyName(initialArg));
            keyMap.put(initialArg, commandKey);
        } else {
            CommandTree commandTree = treeMap.get(initialArg);
            if (commandTree == null) {
                commandTree = new CommandTree(this, initialArg);
                treeMap.put(initialArg, commandTree);
            }
            commandTree.registerKeysForArgs(removeInitialArg(args));
        }
    }

    private String getCommandKeyName(String arg) {
        return name != null ? name + " " + arg : arg;
    }

    String[] removeInitialArg(String[] args) {
        String[] newArgs = new String[args.length - 1];
        System.arraycopy(args, 1, newArgs, 0, args.length - 1);
        return newArgs;
    }

    public String[] joinArgsForKnownCommands(String[] args) {
        CommandTree tree = this;
        CommandKey key = null;
        int commandArgLength = 0;
        for (int i = 0; i < args.length; i++) {
            commandArgLength++;
            String lowerName = args[i].toLowerCase();
            key = tree.keyMap.get(lowerName);
            tree = tree.treeMap.get(lowerName);
            if (tree == null) {
                break;
            }
        }
        if (key != null) {
            String[] newArgs = new String[args.length - (commandArgLength - 1)];
            newArgs[0] = key.getName();
            if (args.length > 1) {
                System.arraycopy(args, commandArgLength, newArgs, 1, args.length - commandArgLength);
            }
            return newArgs;
        } else {
            return args;
        }
    }

    public CommandTree getTreeAt(String path) {
        String[] args = CommandHandler.PATTERN_ON_SPACE.split(path);
        CommandTree tree = this;
        for (String arg : args) {
            tree = tree.treeMap.get(arg.toLowerCase());
        }
        return tree;
    }

    @Override
    public String toString() {
        return "CommandTree{" +
                "name='" + name + '\'' +
                ", treeMap=" + treeMap +
                ", keyMap=" + keyMap +
                '}';
    }

    public Set<String> getSubCommandSet() {
        Set<String> subCommands = new HashSet<String>(treeMap.size() + keyMap.size());
        for (CommandTree key : treeMap.values()) {
            subCommands.add(key.name);
        }
        for (CommandKey key : keyMap.values()) {
            subCommands.add(key.getName());
        }
        return subCommands;
    }

    private static class CommandKey {

        @NotNull
        protected final String name;

        CommandKey(@NotNull final String name) {
            this.name = name.toLowerCase();
        }

        @NotNull
        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "CommandKey{" +
                    "name='" + getName() + '\'' +
                    '}';
        }
    }
}
