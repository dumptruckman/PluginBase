package pluginbase.command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
            CommandTree commandTree = new CommandTree(this, initialArg);
            treeMap.put(initialArg, commandTree);
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
            key = tree.keyMap.get(args[i]);
            tree = tree.treeMap.get(args[i]);
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
