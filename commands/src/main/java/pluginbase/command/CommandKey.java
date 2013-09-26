package pluginbase.command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

class CommandKey {

    @NotNull
    private final String key;
    private final boolean command;
    private Map<String, CommandKey> commandKeys;

    CommandKey(@NotNull final String key, final boolean command) {
        this.key = key;
        this.command = command;
    }

    CommandKey(@NotNull final CommandKey previousKey) {
        this.key = previousKey.key;
        this.command = true;
        this.commandKeys = previousKey.commandKeys;
    }

    @NotNull
    String getName() {
        return key;
    }

    boolean isCommand() {
        return command;
    }

    private void initMap() {
        if (commandKeys == null) {
            commandKeys = new HashMap<String, CommandKey>();
        }
    }

    @Nullable
    CommandKey getKey(@NotNull final String key) {
        initMap();
        return commandKeys.get(key);
    }

    @NotNull
    CommandKey newKey(@NotNull final String key, final boolean command) {
        initMap();
        if (commandKeys.containsKey(key)) {
            if (command) {
                commandKeys.put(key, new CommandKey(commandKeys.get(key)));
            }
            return commandKeys.get(key);
        } else {
            final CommandKey commandKey = new CommandKey(getName() + " " + key, command);
            commandKeys.put(key, commandKey);
            return commandKey;
        }
    }
}
